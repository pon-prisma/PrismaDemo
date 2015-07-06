package it.prisma.businesslayer.bizlib.rest.apiclients.monitoring;

import it.prisma.domain.dsl.cloudify.response.ErrorResponse;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
import it.prisma.utils.web.ws.rest.apiencoding.decode.RestResponseDecodeStrategy;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.javatuples.Pair;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Generic Rest Decode startegy for the Cloudify 'protocol'.
 * Currently for error mapping class only !
 * @author l.biava
 *
 */
public class MonitoringRRDStrategy implements RestResponseDecodeStrategy {

	@Override
	public JavaType getModelClass(RestMessage msg)
			throws NoMappingModelFoundException {
		Pair<StatusType,JavaType> result= strategy(msg);
		return result.getValue1();
	}

	@Override
	public StatusType getStatus(RestMessage msg)
			throws NoMappingModelFoundException {
		Pair<StatusType,JavaType> result= strategy(msg);
		return result.getValue0();
	}
	
	private Pair<StatusType,JavaType> strategy(RestMessage msg) throws NoMappingModelFoundException {
		StatusType status;
		JavaType clazz;
		
		int httpStatusCode=msg.getHttpStatusCode();
		
		if (httpStatusCode>=400) {
			//ERROR CLASS
			status=Status.INTERNAL_SERVER_ERROR;
			ObjectMapper mapper = new ObjectMapper();
			clazz = mapper.getTypeFactory().constructType(ErrorResponse.class);
			
			//clazz= new JavaType<ErrorResponse>() {};
		} else {
			throw new NoMappingModelFoundException(msg);
		}
		
		return new Pair<StatusType,JavaType>(status,clazz);
	}

}
