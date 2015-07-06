package it.prisma.utils.web.ws.rest.apiclients.prisma.openstackV3;

import it.prisma.domain.dsl.iaas.openstack.ErrorWrapper;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.RestResponseDecodeStrategy;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.javatuples.Pair;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Generic Rest Decode strategy for the Openstack 'protocol'.
 * 
 * @author l.biava
 * 
 */
public class OpenstackRRDStrategy<APIResponseType> implements
		RestResponseDecodeStrategy {

	private Class<APIResponseType> targetClass;

	public OpenstackRRDStrategy(Class<APIResponseType> c) {
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

		if (httpStatusCode >= 200 && httpStatusCode < 300 ) {
			// Response class type
			clazz = mapper.getTypeFactory().constructType(targetClass);
			
		} else if (httpStatusCode >= 400) {
			status = Status.INTERNAL_SERVER_ERROR;
			clazz = mapper.getTypeFactory()
					.constructType(ErrorWrapper.class);
		} else {
			throw new ServerErrorResponseException("SERVER_ERROR_RESPONSE",
					null, msg, httpStatusCode);
		}

		return new Pair<StatusType, JavaType>(status, clazz);
	}

}