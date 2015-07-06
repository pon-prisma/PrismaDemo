package it.prisma.businesslayer.bizlib.rest.apiclients.monitoring.api;

import it.prisma.businesslayer.bizlib.rest.apiclients.monitoring.MonitoringRRDStrategy;
import it.prisma.businesslayer.bizlib.rest.apiclients.monitoring.dsl.CheckHostResponse;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.javatuples.Pair;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Specific Rest Decode strategy for the mapping of API (Application Deployment) Response.
 * @author l.biava
 *
 */
public class MonitoringAPICheckHostRRDStrategy extends
		MonitoringRRDStrategy {

	@Override
	public JavaType getModelClass(RestMessage msg)
			throws NoMappingModelFoundException {
		try {
			Pair<StatusType, JavaType> result = strategy(msg);
			return result.getValue1();
		} catch (NoMappingModelFoundException e) {
			return super.getModelClass(msg);
		}
	}

	@Override
	public StatusType getStatus(RestMessage msg)
			throws NoMappingModelFoundException {
		try {
			Pair<StatusType, JavaType> result = strategy(msg);
			return result.getValue0();
		} catch (NoMappingModelFoundException e) {
			return super.getStatus(msg);
		}
	}

	private Pair<StatusType, JavaType> strategy(RestMessage msg)
			throws NoMappingModelFoundException {
		StatusType status;
		JavaType clazz;

		int httpStatusCode = msg.getHttpStatusCode();

		if (httpStatusCode == 200) {
			status = Status.OK;
			
			ObjectMapper mapper = new ObjectMapper();
			clazz = mapper.getTypeFactory().constructType(CheckHostResponse.class);
//			clazz = new JavaType<CheckHostResponse>() {
//			};
		} else {
			throw new NoMappingModelFoundException(msg);
		}

		return new Pair<StatusType, JavaType>(status, clazz);
	}
}
