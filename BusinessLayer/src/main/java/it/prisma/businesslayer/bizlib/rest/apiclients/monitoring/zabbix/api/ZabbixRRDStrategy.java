package it.prisma.businesslayer.bizlib.rest.apiclients.monitoring.zabbix.api;
//package it.prisma.businesslayer.bizlib.rest.Zabbix.api;
//
//import it.prisma.businesslayer.bizlib.rest.Zabbix.dsl.response.ErroreResponse;
//
//import org.javatuples.Pair;
//
//import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
//import it.prisma.utils.web.ws.rest.apiencoding.RestMessage;
//import it.prisma.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult.StatusType;
//import it.prisma.utils.web.ws.rest.apiencoding.decode.RestResponseDecodeStrategy;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//
///**
// * Generic Rest Decode startegy for the Cloudify 'protocol'.
// * Currently for error mapping class only !
// * @author l.biava
// *
// */
//public class ZabbixRRDStrategy implements RestResponseDecodeStrategy {
//
//	@Override
//	public TypeReference getModelClass(RestMessage msg)
//			throws NoMappingModelFoundException {
//		Pair<StatusType,TypeReference> result= strategy(msg);
//		return result.getValue1();
//	}
//
//	@Override
//	public StatusType getStatus(RestMessage msg)
//			throws NoMappingModelFoundException {
//		Pair<StatusType,TypeReference> result= strategy(msg);
//		return result.getValue0();
//	}
//	
//	private Pair<StatusType,TypeReference> strategy(RestMessage msg) throws NoMappingModelFoundException {
//		StatusType status;
//		TypeReference clazz;
//		
//		int httpStatusCode=msg.getHttpStatusCode();
//		
//		if (httpStatusCode>=400) {
//			//ERROR CLASS
//			status=StatusType.ERROR;
//			clazz= new TypeReference<ErroreResponse>() {};
//		} else {
//			throw new NoMappingModelFoundException(msg);
//		}
//		
//		return new Pair<StatusType,TypeReference>(status,clazz);
//	}
//
//}
