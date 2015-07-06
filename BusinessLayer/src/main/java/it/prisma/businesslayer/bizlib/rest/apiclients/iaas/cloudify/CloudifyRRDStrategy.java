package it.prisma.businesslayer.bizlib.rest.apiclients.iaas.cloudify;

import java.io.IOException;

import it.prisma.domain.dsl.cloudify.response.ResponseWrapper;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.RestResponseDecodeStrategy;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.javatuples.Pair;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Generic Rest Decode strategy for the Cloudify 'protocol'.
 * 
 * @author l.biava
 * 
 */
public class CloudifyRRDStrategy<APIResponseType> implements
		RestResponseDecodeStrategy {

	private Class<APIResponseType> targetClass;

	public CloudifyRRDStrategy(Class<APIResponseType> c) {
		targetClass = c;
	}

	@Override
	public JavaType getModelClass(RestMessage msg)
			throws NoMappingModelFoundException, ServerErrorResponseException {
		Pair<StatusType, JavaType> result = strategy(msg);
		return result.getValue1();
	}

	@Override
	public StatusType getStatus(RestMessage msg)
			throws NoMappingModelFoundException, ServerErrorResponseException {
		Pair<StatusType, JavaType> result = strategy(msg);
		return result.getValue0();
	}

	private Pair<StatusType, JavaType> strategy(RestMessage msg)
			throws NoMappingModelFoundException, ServerErrorResponseException {
		StatusType status = Status.OK;
		JavaType clazz;
		ObjectMapper mapper = new ObjectMapper();

		int httpStatusCode = msg.getHttpStatusCode();

		if (httpStatusCode == 200) {
			// ResponseWrapper
			clazz = mapper.getTypeFactory().constructParametricType(
					ResponseWrapper.class, targetClass);
			/*
			 * clazz = new
			 * TypeReference<PrismaResponseWrapper<APIResponseType>>() { };
			 */
		} else if (httpStatusCode == 400) {
			status = Status.BAD_REQUEST;
			clazz = mapper.getTypeFactory()
					.constructType(ResponseWrapper.class);
		} else {
			status = Status.fromStatusCode(httpStatusCode);
			clazz = mapper.getTypeFactory()
					.constructType(ResponseWrapper.class);
			if (msg.getBody() != null && (status.getFamily().equals(Status.Family.CLIENT_ERROR)
					|| status.getFamily().equals(Status.Family.SERVER_ERROR)))
				try {
					// Try to see if 
					new ObjectMapper().readValue((String) msg.getBody(), clazz);
				} catch (Exception e) {
					throw new ServerErrorResponseException("SERVER_ERROR_RESPONSE",
						null, msg, httpStatusCode);
				}
			else {
				throw new ServerErrorResponseException("SERVER_ERROR_RESPONSE",
						null, msg, httpStatusCode);
			}
		}

		return new Pair<StatusType, JavaType>(status, clazz);
	}

}