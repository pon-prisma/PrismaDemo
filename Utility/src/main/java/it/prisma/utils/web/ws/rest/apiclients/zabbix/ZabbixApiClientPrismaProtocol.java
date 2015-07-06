package it.prisma.utils.web.ws.rest.apiclients.zabbix;

import it.prisma.domain.dsl.configuration.PlatformConfiguration;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.JSONRPCRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.request.ZabbixParamItemRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.prisma.domain.dsl.prisma.prismaprotocol.PrismaResponseWrapper;
import it.prisma.utils.web.ws.rest.apiclients.AbstractAPIClient;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.PrismaRestResponseDecoder;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.restclient.RestClientHelper;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


/**
 * This class contains Prisma BizLib Rest API requests implementation.
 * 
 * 
 * 
 */
public class ZabbixApiClientPrismaProtocol extends AbstractAPIClient {

	public ZabbixApiClientPrismaProtocol(String baseURL) {
		super(baseURL);
		// TODO Auto-generated constructor stub
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Generated("org.jsonschema2pojo")
	@JsonPropertyOrder({ "body" })
	public class NoBody {

		@JsonProperty("body")
		private String body;
		@JsonIgnore
		private Map<String, Object> additionalProperties = new HashMap<String, Object>();

		@JsonProperty("body")
		public String getBody() {
			return body;
		}

		@JsonProperty("body")
		public void setBody(String body) {
			this.body = body;
		}

		@JsonAnyGetter
		public Map<String, Object> getAdditionalProperties() {
			return this.additionalProperties;
		}

		@JsonAnySetter
		public void setAdditionalProperty(String name, Object value) {
			this.additionalProperties.put(name, value);
		}

	}



	/**
	 * Consumes getPlatformConfiguration API from Biz Layer.
	 * 
	 * @param request
	 *            the request containing the list of key to retrieve.
	 * @return the response of the API, a list {@link PlatformConfiguration} if
	 *         no error occurred.
	 * @throws MappingException
	 *             if there
	 * @throws RestClientException
	 *             if there was an exception during the request.
	 * @throws APIErrorException
	 *             if the API reported an error.
	 * @throws ServerErrorResponseException
	 *             if the error responded with an error.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ZabbixItemResponse getItemPrismaProtocol(JSONRPCRequest<ZabbixParamItemRequest> request,
			String auth_data) throws MappingException,
			NoMappingModelFoundException, ServerErrorResponseException,
			APIErrorException, RestClientException, JsonParseException,
			JsonMappingException, IOException {

		String URL = baseWSUrl;
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();

		GenericEntity<String> ge = new RestClientHelper.JsonEntityBuilder()
		.build(request);

		BaseRestResponseResult result = restClient.postRequest(URL, headers,
				ge, RestClientHelper.JsonEntityBuilder.MEDIA_TYPE,
				new PrismaRestResponseDecoder<ZabbixItemResponse>(
						ZabbixItemResponse.class), null);

		if (result.getStatus() == Status.OK) {
			try {
				return ((PrismaResponseWrapper<ZabbixItemResponse>) result
						.getResult()).getResult();
			} catch (Exception e) {
				throw new MappingException("Unexpected response type.", null,
						result.getOriginalRestMessage());
			}
		} else {
			throw new APIErrorException("API_ERROR", null,
					result.getOriginalRestMessage(),
					((PrismaResponseWrapper) result.getResult()).getError());
		}
	}
}