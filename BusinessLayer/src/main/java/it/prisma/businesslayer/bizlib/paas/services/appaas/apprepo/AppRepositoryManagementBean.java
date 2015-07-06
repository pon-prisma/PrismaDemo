package it.prisma.businesslayer.bizlib.paas.services.appaas.apprepo;

import it.prisma.businesslayer.bizlib.common.exceptions.DuplicatedResourceException;
import it.prisma.businesslayer.bizlib.infrastructure.AppRepoLookupInfo;
import it.prisma.businesslayer.bizlib.infrastructure.InfrastructureManagerBean;
import it.prisma.businesslayer.bizlib.infrastructure.InfrastructureManagerBean.AppRepoEndpoint;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.MainOpenstackAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.MainOpenstackAPIClientImpl;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.ObjectStorageOpenstackAPIClient;
import it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.OpenstackEndpointType;
import it.prisma.businesslayer.bizws.iaas.OpenstackHelper;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.dal.dao.paas.services.appaas.ApplicationRepositoryEntryDAO;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entities.paas.services.appaas.ApplicationRepositoryEntry;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
import it.prisma.domain.dsl.iaas.openstack.identity.response.Endpoint;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.commons.io.IOUtils;
import org.javatuples.Pair;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Payloads;

@Stateless
@Local(AppRepositoryManagement.class)
public class AppRepositoryManagementBean implements AppRepositoryManagement {

	@Inject
	private EnvironmentConfig envCfg;

	@Inject
	private OpenstackHelper openstackHelper;

	@Inject
	private ApplicationRepositoryEntryDAO appRepoEntryDAO;

	@Inject
	private InfrastructureManagerBean infrastructureBean;

	@Resource
	private UserTransaction userTransaction;

	private static final String CONTAINER_NAME_PUBLIC = "apprepo_public";
	private static final String CONTAINER_NAME_PRIVATE = "PrivateAppRepo";

	public Pair<Long, String> addAppSrcFile(UserAccount userAccount,
			Workgroup workgroup, String applicationName, String tag,
			String description, boolean public_, String fileName, InputStream is)
			throws Exception {

		try {

			// userTransaction.begin();

			// TODO set fileName
			byte[] fileData=IOUtils.toByteArray(is);
			ApplicationRepositoryEntry appRepoEntry = new ApplicationRepositoryEntry(
					userAccount, workgroup, applicationName, tag, description,
					public_, fileName, "NOT_CREATED_YET", (long) fileData.length, null);
			appRepoEntryDAO.create(appRepoEntry);


			// ADD TO SWIFT

			// MainOpenstackAPIClient mainClient = new
			// MainOpenstackAPIClientImpl(
			// envCfg.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_IDENTITY_URL),
			// null,
			// "v2.0",
			// envCfg.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_NAME),
			// envCfg.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_USERNAME),
			// envCfg.getSvcEndpointProperty(EnvironmentConfig.SVCEP_OS_TENANT_ORC_PASSWORD));

			// ObjectStorageOpenstackAPIClient swiftClient = mainClient
			// .getObjectStorageClient(mainClient.getServiceEndpoints(
			// OpenstackEndpointType.OBJECT_STORAGE).get(0));

			AppRepoEndpoint endpoint;
			if (public_)
				endpoint = infrastructureBean
						.getPublicAppRepoEndpoint(new AppRepoLookupInfo());
			else
				endpoint = infrastructureBean
						.getPrivateAppRepoEndpoint(new AppRepoLookupInfo(
								workgroup.getId()));

			OSClient os = OpenstackHelper.getOSClient(
					endpoint.getOSIdentityURL(), endpoint.getTenantName(),
					endpoint.getUsername(), endpoint.getPassword());
			
			if (endpoint.getOSRegion() != null)
				os.useRegion(endpoint.getOSRegion());

			// String account = os.objectStorage().account().get().toString();
			String container;
			if (public_)
				container = CONTAINER_NAME_PUBLIC;
			else
				container = CONTAINER_NAME_PRIVATE;

			String OSfileName = appRepoEntry.getId() + "-" + fileName;
			// String filePath = swiftClient.createObject(account, container,
			// OSfileName, null, file);

			os.objectStorage().objects()
					.put(container, OSfileName, Payloads.create(new ByteArrayInputStream(fileData)));

			// TODO
			// Map<String,String> map = new HashMap<String, String>();
			// map.put("OSIdentityURL", endpoint.getOSIdentityURL());
			// map.put("OSRegion", endpoint.getOSRegion());
			// map.put("OSTenantName", endpoint.getTenantName());
			// map.put("OSContainer", container);
			// map.put("OSFilename", OSfileName);
			// String filePath=JsonUtility.serializeJson(map);
			String filePath = OSfileName;
			// String filePath = account + "/" + container + "/" + fileName;

			// UPDATE DB ENTRY
			appRepoEntry.setFilePath(filePath);
			appRepoEntryDAO.update(appRepoEntry);

			userTransaction.commit();

			return new Pair<Long, String>(appRepoEntry.getId(), filePath);
		} catch (Exception ex) {
			userTransaction.rollback();

			if (ex.getMessage().contains("ConstraintViolation"))
				throw new DuplicatedResourceException(
						ApplicationRepositoryEntry.class, "Workgroup", "Tag",
						"ApplicationName");

			throw ex;
		}

	}

	/**
	 * 200 MB
	 */
	public static final long MAX_FILE_SIZE = 1024 * 1024 * 200;

	@Override
	public Pair<Long, String> addAppSrcFileByUrl(UserAccount userAccount,
			Workgroup workgroup, String applicationName, String tag,
			String description, boolean public_, String fileName, URL url)
			throws Exception {

		try {
			// TODO: Check preconditions before retrieving the file
			if (getFileSize(url) > MAX_FILE_SIZE)
				throw new IllegalArgumentException(
						"Given file is too large. Exceeding " + MAX_FILE_SIZE
								+ " bytes.");

			return addAppSrcFile(userAccount, workgroup, applicationName, tag,
					description, public_, fileName, url.openStream());

		} catch (Exception e) {
			throw e;
		}
	}

	protected int getFileSize(URL url) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("HEAD");
			return conn.getContentLength();
		} catch (IOException e) {
			return -1;
		} finally {
			conn.disconnect();
		}
	}

	public StoredObject getAPPaaSEnvAppSrcFile(Workgroup workgroup, AppaaS app,
			AppaaSEnvironment env) throws Exception {

		return getAPPaaSEnvAppSrcFile(false, workgroup, app, env);
	}

	public StoredObject getAPPaaSEnvAppSrcFileMeta(Workgroup workgroup,
			AppaaS app, AppaaSEnvironment env) throws Exception {
		return getAPPaaSEnvAppSrcFile(true, workgroup, app, env);
	}

	protected StoredObject getAPPaaSEnvAppSrcFile(boolean meta,
			Workgroup workgroup, AppaaS app, AppaaSEnvironment env)
			throws Exception {

		if (env.getAppaaS().getId() != app.getId()
				|| app.getWorkgroup().getId() != workgroup.getId())
			throw new IllegalArgumentException("Bad workgroup, app or env.");

		ApplicationRepositoryEntry appRepoEntry = env
				.getApplicationRepositoryEntry();

		String container;
		AppRepoEndpoint endpoint;
		if (appRepoEntry.isShared()) {
			endpoint = infrastructureBean
					.getPublicAppRepoEndpoint(new AppRepoLookupInfo());
			container = CONTAINER_NAME_PUBLIC;
		} else {
			endpoint = infrastructureBean
					.getPrivateAppRepoEndpoint(new AppRepoLookupInfo(workgroup
							.getId()));
			container = CONTAINER_NAME_PRIVATE;
		}

		OSClient os = OpenstackHelper.getOSClient(endpoint.getOSIdentityURL(),
				endpoint.getTenantName(), endpoint.getUsername(),
				endpoint.getPassword());
		if (endpoint.getOSRegion() != null)
			os.useRegion(endpoint.getOSRegion());

		//Tenant t = os.identity().tenants().getByName(endpoint.getTenantName());

		MainOpenstackAPIClient mainOSclient = new MainOpenstackAPIClientImpl(
				endpoint.getOSIdentityURL(), null, "",
				endpoint.getTenantName(), endpoint.getUsername(),
				endpoint.getPassword());

		// Get file from swift
		List<Endpoint> endpoints = mainOSclient
				.getServiceEndpoints(OpenstackEndpointType.OBJECT_STORAGE);
		ObjectStorageOpenstackAPIClient objClient = mainOSclient
				.getObjectStorageClient(endpoints.get(0));

		String filePath = appRepoEntry.getFilePath();

		it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack.ObjectStorageOpenstackAPIClient.StoredObject obj;
		String objStoreEndpoint = endpoints.get(0).getPublicURL();
		String fileUrl = objStoreEndpoint + "/" + container + "/"
				+ appRepoEntry.getFilePath();
		if (meta) {
			obj = objClient.getObjectMeta(fileUrl, null);
		} else {
			obj = objClient.getObject(fileUrl, null);
		}

		return new StoredObject(obj.getInputStream(), obj.getMetadata());

		// TODO: Wait to be fixed, issue #222 Openstack4j
		// String OSfileName = filePath;
		//
		// StoredObject object = new StoredObject(null,
		// new HashMap<String, Object>());
		//
		// SwiftObject swiftObj = os.objectStorage().objects()
		// .get(container, OSfileName);
		// object.getMetadata().put("Content-Type", swiftObj.getMimeType());
		// object.getMetadata().put("Content-Length",
		// swiftObj.getSizeInBytes().toString());
		//
		// Map<String, String> metadata = os.objectStorage().objects()
		// .getMetadata(container, OSfileName);
		//
		// for (Map.Entry<String, String> item : metadata.entrySet()) {
		// object.getMetadata().put(item.getKey(), item.getValue());
		// }
		//
		// if (!meta) {
		// DLPayload payload = swiftObj.download();
		// // Save to temp file
		// // TODO Auto file delete
		// // String tmpFilePath = "tmp_" + UUID.randomUUID() + "_"
		// // + System.currentTimeMillis();
		// // File tmpFile = new File(tmpFilePath);
		// // FileOutputStream out = new FileOutputStream(tmpFile);
		// // IOUtils.copy(payload.getInputStream(), out);
		// // out.close();
		//
		// // object.setInputStream(new FileInputStream(tmpFilePath));
		// // payload.getInputStream().read();
		// // payload.getInputStream().reset();
		// object.setInputStream(payload.getInputStream());
		// }
		//
		// return object;

	}

	@Override
	public Collection<ApplicationRepositoryEntry> getPublicAppSrcFiles()
			throws Exception {
		return appRepoEntryDAO.findAllPublic();
	}

	@Override
	public Collection<ApplicationRepositoryEntry> getPrivateAppSrcFiles(
			Long workgroupId) throws Exception {
		return appRepoEntryDAO.findAllPrivate(workgroupId);
	}
}
