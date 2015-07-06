package it.prisma.utils.web.ws.rest.apiclients.zabbix;

import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.JSONRPCResponse;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.RestResponseDecodeStrategy;

import java.util.ArrayList;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.javatuples.Pair;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Generic Rest Decode strategy for Prisma Rest Protocol.
 * 
 * @param <APIResponseType>
 *            The expected ResponseType.
 * @author l.biava
 * 
 */
public class ZabbixRRDStrategy<APIResponseType> implements
		RestResponseDecodeStrategy {

	private Class<APIResponseType> targetClass;
	private JSONRPCResponse<ArrayList<APIResponseType>> targetJSONRpc;

	public ZabbixRRDStrategy(Class<APIResponseType> c) {
		targetClass = c;
	}

	public ZabbixRRDStrategy(JSONRPCResponse<ArrayList<APIResponseType>> c, String t) {
		targetJSONRpc = c;
		String param = t;
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

		int httpStatusCode = msg.getHttpStatusCode();

		if (httpStatusCode == 200) {
			// ResponseWrapper
			ObjectMapper mapper = new ObjectMapper();
			clazz = mapper.getTypeFactory().constructParametricType(
					JSONRPCResponse.class, targetClass);
			/*
			 * clazz = new
			 * TypeReference<PrismaResponseWrapper<APIResponseType>>() { };
			 */
		} else {
			throw new ServerErrorResponseException("SERVER_ERROR_RESPONSE",
					null, msg, httpStatusCode);
		}

		return new Pair<StatusType, JavaType>(status, clazz);
	}
}
