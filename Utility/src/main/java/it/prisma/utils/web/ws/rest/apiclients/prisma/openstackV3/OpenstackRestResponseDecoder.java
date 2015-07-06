package it.prisma.utils.web.ws.rest.apiclients.prisma.openstackV3;

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
public class OpenstackRestResponseDecoder<APIResponseType> extends
		BaseRestResponseDecoder {

	public OpenstackRestResponseDecoder(Class<APIResponseType> c) {
		super(new OpenstackRRDStrategy<APIResponseType>(c));
	}

	public OpenstackRestResponseDecoder(
			RestResponseDecodeStrategy defaultDecodeStrategy) {
		super(defaultDecodeStrategy);
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
		// FIX for Openstack not responding with Content-type if JSON body !
		if (msg.getHeaders().containsKey("Content-Type")
				& !((String) (msg.getHeaders().getFirst("Content-Type")))
						.contains("application/json")) {
			String contentType = (String) msg.getHeaders().getFirst(
					"Content-Type");
			if (contentType.contains("text"))
				return new BaseRestResponseResult(status, msg.getBody(),
						null, msg);// TODO Fix !

			throw new MappingException("Not JSON encoded body.", null, msg);
		}

		// TODO: Jackson Mapping
		/*
		 * Object result = (Object) JsonUtility.deserializeJson(msg.getBody(),
		 * mappingClass);
		 */
		Object result;
		try {
			result = new ObjectMapper().readValue((String) msg.getBody(), mappingClass);

			// //Check if result is error
			// if(((PrismaResponseWrapper) result).isAPIError())
			// status=StatusType.ERROR;
			// else
			// status=StatusType.OK;
		} catch (IOException e) {
			throw new MappingException(e.getMessage(), e, msg);
		}

		return new BaseRestResponseResult(status, result, mappingClass, msg);
	}

}
