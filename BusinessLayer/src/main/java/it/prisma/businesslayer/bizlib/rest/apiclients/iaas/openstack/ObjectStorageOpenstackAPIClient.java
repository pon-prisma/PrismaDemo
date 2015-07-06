package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.openstack;

import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClient.RestMethod;
import it.prisma.utils.web.ws.rest.restclient.RestClientFactory;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ObjectStorageOpenstackAPIClient extends AbstractOpenstackAPIClient {

	/**
	 * Represents an object in OpenStack Object Storage, also with related
	 * metadata.
	 * 
	 * @author l.biava
	 *
	 */
	public class StoredObject {
		private InputStream inputStream;
		private Map<String, Object> metadata = new HashMap<String, Object>();

		public StoredObject(InputStream inputStream,
				Map<String, Object> metadata) {
			super();
			this.inputStream = inputStream;
			this.metadata = metadata;
		}

		public StoredObject(InputStream inputStream) {
			super();
			this.inputStream = inputStream;
		}

		/**
		 * 
		 * @return the input stream to read the file from.
		 */
		public InputStream getInputStream() {
			return inputStream;
		}

		public void setInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		/**
		 * 
		 * @return the metadata associated to the file. <b>Currently all the
		 *         response headers !</b>.
		 */
		public Map<String, Object> getMetadata() {
			return metadata;
		}

		public void setMetadata(Map<String, Object> metadata) {
			this.metadata = metadata;
		}
	}

	/**
	 * 
	 * @param baseURL
	 * @param restClientFactory
	 * @param openstackServiceVersion
	 */
	protected ObjectStorageOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory, String openstackServiceVersion) {
		super(baseURL, restClientFactory, openstackServiceVersion);
	}

	public ObjectStorageOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory,
			String openstackServiceVersion, String tokenID) {
		super(baseURL, restClientFactory, openstackServiceVersion, tokenID);

	}

	public ObjectStorageOpenstackAPIClient(String baseURL,
			RestClientFactory restClientFactory,
			String openstackServiceVersion, String tenant, String user,
			String pwd, IdentityOpenstackAPIClient idOSClient)
			throws OpenstackAuthenticationException {
		super(baseURL, restClientFactory, openstackServiceVersion, tenant,
				user, pwd, idOSClient);
	}

	/**
	 * BETA
	 * 
	 * @param account
	 *            The unique name for the account. An account is also known as
	 *            the project or tenant.
	 * @param container
	 *            The unique name for the container.
	 * @param fileName
	 *            The unique name for the object.
	 * 
	 * @return
	 * @throws MappingException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws OpenstackAPIErrorException
	 * @throws RestClientException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String createObject(String account, String container,
			String fileName, MultivaluedMap<String, Object> headerMap, File file)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, OpenstackAPIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/" + container + "/" + fileName;

		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(tokenID);
		headers.putSingle("X-Detect-Content-Type", true);
		// headers.putSingle("Content-Disposition", "form-data");
		// headers.putSingle("Content-Type", "application/octet-stream");

		if (headerMap != null)
			headers.putAll(headerMap);

		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		String mimeType = mimeTypesMap.getContentType(file);
		MediaType mediaType = MediaType.valueOf(mimeType);

		GenericEntity<byte[]> ge = new GenericEntity<byte[]>(
				FileUtils.readFileToByteArray(file)) {
		};

		// GenericEntity<MultipartFormDataOutput> ge = new
		// RestClientHelper.FormDataEntityBuilder()
		// .addFormData(FilenameUtils.getBaseName(fileName), file,
		// MediaType.APPLICATION_OCTET_STREAM_TYPE, fileName)
		// .build();

		BaseRestResponseResult result = restClient.putRequest(URL, headers, ge,
				mediaType, new OpenstackRestResponseDecoder<String>(
						String.class), null);

		if (result.getStatus().getFamily() == Family.SUCCESSFUL)
			return URL;

		return handleResult(result);
	}

	public String deleteObject(String account, String container,
			String fileName, Map<String, String> headerMap)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, OpenstackAPIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/" + getServiceVersion() + "/" + account
				+ "/" + container + "/" + fileName;

		MultivaluedMap<String, Object> headers = getHeaders(headerMap);

		BaseRestResponseResult result = restClient.deleteRequest(URL, headers,
				null, null, new OpenstackRestResponseDecoder<String>(
						String.class), null);
		return handleResult(result);
	}

	public StoredObject getObject(String objectURL,
			Map<String, String> headerMap) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			OpenstackAPIErrorException, RestClientException,
			JsonParseException, JsonMappingException, IOException {

		String URL = objectURL;

		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(tokenID);
		headers.putAll(getHeaders(headerMap));

		try {
			RestMessage msg = restClient.doRequest(RestMethod.GET, URL,
					headers, null, null, null, null, File.class);
			// URL, headers);
			if (msg.getHttpStatusCode() == Status.OK.getStatusCode()) {
				StoredObject object = new StoredObject(new FileInputStream(
						(File) msg.getBody()));
				for (Map.Entry<String, List<Object>> item : msg.getHeaders()
						.entrySet()) {
					object.getMetadata().put(item.getKey(),
							item.getValue().get(0));
				}

				return object;
			}
			// return handleResult(result);
			return null;
		} catch (Exception e) {
			// InputStream is = new ByteArrayInputStream((byte
			// [])e.getResponseMessage().getBody());
			// return is;
		}
		return null;
	}
	
	public StoredObject getObjectMeta(String objectURL,
			Map<String, String> headerMap) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			OpenstackAPIErrorException, RestClientException,
			JsonParseException, JsonMappingException, IOException {

		String URL = objectURL;

		MultivaluedMap<String, Object> headers = getAuthenticationHeaders(tokenID);
		headers.putAll(getHeaders(headerMap));

		try {
			RestMessage msg = restClient.doRequest(RestMethod.HEAD, URL,
					headers, null, null, null, null, File.class);
			// URL, headers);
			if (msg.getHttpStatusCode() == Status.OK.getStatusCode()) {
				StoredObject object = new StoredObject(null);
				for (Map.Entry<String, List<Object>> item : msg.getHeaders()
						.entrySet()) {
					object.getMetadata().put(item.getKey(),
							item.getValue().get(0));
				}

				return object;
			}
			// return handleResult(result);
			return null;
		} catch (Exception e) {
			// InputStream is = new ByteArrayInputStream((byte
			// [])e.getResponseMessage().getBody());
			// return is;
		}
		return null;
	}

	public StoredObject getObject(String account, String container,
			String fileName, Map<String, String> headerMap)
			throws MappingException, NoMappingModelFoundException,
			ServerErrorResponseException, OpenstackAPIErrorException,
			RestClientException, JsonParseException, JsonMappingException,
			IOException {

		String URL = baseWSUrl + "/" + getServiceVersion() + "/" + account
				+ "/" + container + "/" + fileName;

		return getObject(URL, headerMap);
	}

}
