package it.prisma.businesslayer.bizlib.rest.apiclients.monitoring.zabbix.api;
//package it.prisma.businesslayer.bizlib.rest.Zabbix.api;
//
//import it.prisma.businesslayer.bizlib.rest.Zabbix.dsl.response.AuthenticationResponse;
//
//import org.javatuples.Pair;
//
//import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
//import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
//import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult.StatusType;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//
///**
// * Specific Rest Decode strategy for the mapping of API (Application Deployment) Response.
// * @author l.biava
// *
// */
//public class ZabixAPIAuthenticationRRDStrategy extends
//		ZabbixRRDStrategy {
//
//	@Override
//	public TypeReference getModelClass(RestMessage msg)
//			throws NoMappingModelFoundException {
//		try {
//			Pair<StatusType, TypeReference> result = strategy(msg);
//			return result.getValue1();
//		} catch (NoMappingModelFoundException e) {
//			return super.getModelClass(msg);
//		}
//	}
//
//	@Override
//	public StatusType getStatus(RestMessage msg)
//			throws NoMappingModelFoundException {
//		try {
//			Pair<StatusType, TypeReference> result = strategy(msg);
//			return result.getValue0();
//		} catch (NoMappingModelFoundException e) {
//			return super.getStatus(msg);
//		}
//	}
//
//	private Pair<StatusType, TypeReference> strategy(RestMessage msg)
//			throws NoMappingModelFoundException {
//		StatusType status;
//		TypeReference clazz;
//
//		int httpStatusCode = msg.getHttpStatusCode();
//
//		if (httpStatusCode == 200) {
//			status = StatusType.OK;
//			clazz = new TypeReference<AuthenticationResponse>() {
//			};
//		} else {
//			throw new NoMappingModelFoundException(msg);
//		}
//
//		return new Pair<StatusType, TypeReference>(status, clazz);
//	}
//}
