package it.prisma.utils.web.ws.rest.apiclients.zabbix;

import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseDecoder;
import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.prisma.utils.web.ws.rest.apiencoding.decode.RestResponseDecodeStrategy;

import java.io.IOException;

import javax.ws.rs.core.Response.StatusType;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Basic implementation of a Rest response Decoder. This implementation uses
 * Jackson JSON mapping system.
 * 
 * @author l.biava
 * 
 */
public class ZabbixRestResponseDecoder<APIResponseType> extends 
		BaseRestResponseDecoder {

	public ZabbixRestResponseDecoder(Class<APIResponseType> c) {
		super();
		this.defaultDecodeStrategy = new ZabbixRRDStrategy<APIResponseType>(c);
	}

	/**
	 * <b>Supports only default strategy. MUST pass null in strategy field !</b><br/>
	 * {@inheritDoc}
	 */
	@Override
	public BaseRestResponseResult decode(RestMessage msg,
			RestResponseDecodeStrategy strategy)
			throws NoMappingModelFoundException, MappingException,
			ServerErrorResponseException {

		if (strategy != null)
			throw new UnsupportedOperationException(
					"Only default decoding strategy can be used with this decoder.");

		return decode(msg);
	}

	@Override
	public BaseRestResponseResult decode(RestMessage msg)
			throws NoMappingModelFoundException, MappingException,
			ServerErrorResponseException {

		StatusType status = defaultDecodeStrategy.getStatus(msg);
		JavaType mappingClass = defaultDecodeStrategy.getModelClass(msg);

		// TODO:Check also content type (application/json)
		// FIX for Cloudify not responding with Content-type if JSON body !
		if (msg.getHeaders().containsKey("Content-Type")
				& !"application/json".equals(msg.getHeaders().getFirst(
						"Content-Type")))
			throw new MappingException("Not JSON encoded body.", null, msg);

		// TODO: Jackson Mapping
		/*
		 * Object result = (Object) JsonUtility.deserializeJson(msg.getBody(),
		 * mappingClass);
		 */
		Object result;
		try {
			result = new ObjectMapper().readValue((String) msg.getBody(), mappingClass);
			
//			//Check if result is error
//			if(((JSONRPCResponse) result).isAPIError())
//				status=Status.ERROR;
//			else
//				status=Status.OK;
		} catch (IOException e) {
			throw new MappingException(e.getMessage(), e, msg);
		}

		return new BaseRestResponseResult(status, result, mappingClass, msg);
	}

}
