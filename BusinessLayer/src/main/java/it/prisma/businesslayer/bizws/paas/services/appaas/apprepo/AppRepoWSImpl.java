package it.prisma.businesslayer.bizws.paas.services.appaas.apprepo;

import it.prisma.businesslayer.bizlib.paas.services.appaas.apprepo.AppRepositoryManagement;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.exceptions.BadRequestException;
import it.prisma.businesslayer.utils.mappinghelpers.MappingHelper;
import it.prisma.dal.dao.accounting.UserAccountDAO;
import it.prisma.dal.dao.accounting.WorkgroupDAO;
import it.prisma.dal.dao.paas.services.appaas.APPaaSDAO;
import it.prisma.dal.dao.paas.services.appaas.APPaaSEnvironmentDAO;
import it.prisma.dal.dao.paas.services.appaas.ApplicationRepositoryEntryDAO;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.appaas.ApplicationRepositoryEntry;
import it.prisma.domain.dsl.paas.services.appaas.response.ApplicationRepositoryEntryRepresentation;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.request.AddAppSrcFileRequest;
import it.prisma.domain.dsl.prisma.paas.services.appaas.apprepo.response.AddAppSrcFileResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.PathParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

public class AppRepoWSImpl extends BaseWS implements AppRepoWS {

	public static Logger prismaLog = LogManager.getLogger(AppRepoWSImpl.class);

	@Inject
	UserAccountDAO userDAO;

	@Inject
	WorkgroupDAO workgroupDAO;

	@Inject
	ApplicationRepositoryEntryDAO appRepoEntryDAO;

	@Inject
	APPaaSEnvironmentDAO appaaSEnvironmentDAO;

	@Inject
	APPaaSDAO appaaSDAO;

	@Inject
	private MappingHelper<ApplicationRepositoryEntry, ApplicationRepositoryEntryRepresentation> appRepoEntryMH;

	@EJB
	private AppRepositoryManagement appRepoBean;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public AddAppSrcFileResponse addAppSrcFile(final Long workgroupId,
			AppSrcFileMultipartForm form) throws Exception {

		try {
			try {
				AddAppSrcFileRequest request = form.getAddAppSrcFileRequest();

				if (form.getFileData() == null)
					throw new BadRequestException();

				UserAccount userAccount = userDAO.findById(request.getUserId());
				Workgroup workgroup = workgroupDAO.findById(request
						.getWorkgroupId());

				// TODO In memory file !
				// FileOutputStream fileOuputStream = new FileOutputStream(
				// request.getFileName());
				// fileOuputStream.write(form.getFileData());
				// fileOuputStream.close();
				//
				// File file = new File(request.getFileName());

				InputStream is = new ByteArrayInputStream(form.getFileData());

				Pair<Long, String> result = appRepoBean.addAppSrcFile(
						userAccount, workgroup, request.getAppName(),
						request.getTag(), request.getAppDescription(),
						request.getPublic(), request.getFileName(), is);

				AddAppSrcFileResponse resp = new AddAppSrcFileResponse();
				resp.setAppSrcFileId(result.getValue0());
				resp.setFilePath(result.getValue1());

				return resp;

			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}

	}

	public AddAppSrcFileResponse addAppSrcFileFromURL(
			@PathParam("workgroupId") final Long workgroupId,
			AddAppSrcFileRequest request) throws Exception {

		try {
			try {

				UserAccount userAccount = userDAO.findById(request.getUserId());
				Workgroup workgroup = workgroupDAO.findById(request
						.getWorkgroupId());

				Pair<Long, String> result = appRepoBean.addAppSrcFileByUrl(
						userAccount, workgroup, request.getAppName(), request
								.getTag(), request.getAppDescription(), request
								.getPublic(), request.getFileName(), new URL(
								request.getURL()));

				AddAppSrcFileResponse resp = new AddAppSrcFileResponse();
				resp.setAppSrcFileId(result.getValue0());
				resp.setFilePath(result.getValue1());

				return resp;

			} catch (EJBException ejbe) {
				throw ejbe.getCausedByException();
			}
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public Collection<ApplicationRepositoryEntryRepresentation> getPrivateAppSrcFiles(
			final Long workgroupId) {

		try {
			Collection<ApplicationRepositoryEntry> files = appRepoBean
					.getPrivateAppSrcFiles(workgroupId);

			return appRepoEntryMH.getDSL(files);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public Collection<ApplicationRepositoryEntryRepresentation> getPublicAppSrcFiles() {

		try {
			Collection<ApplicationRepositoryEntry> files = appRepoBean
					.getPublicAppSrcFiles();

			return appRepoEntryMH.getDSL(files);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}
}