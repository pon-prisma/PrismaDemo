package it.monitoringpillar.wrapper;

import it.monitoringpillar.adapter.zabbix.handler.GroupIDByName;
import it.monitoringpillar.adapter.zabbix.handler.HostIDByName;
import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.monitoringpillar.adapter.zabbix.iaas.ZabbixAdapterIaas;
import it.monitoringpillar.adapter.zabbix.metrics.ZabbixAdapterSetter;
import it.monitoringpillar.config.TimestampMonitoring;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.MonitPillarEventDescriptor;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.MonitPillarEventResponse;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MetricsHistoryTimeRequest;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV2_4;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
import it.prisma.utils.web.ws.rest.apiclients.zabbix.APIErrorException;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;

@Stateless
public class WrapperMonitoringEvents {

	private final String IAAS = "iaas";
	private final String METRICS = "metrics";
	private final String WATCHER = "watcher";
	private String triggerid;
	private String requestTime;
	private String hostid2Zabbix;
	private String hostNameEvent;
	private String groupNameEvent;
	private ArrayList<MonitPillarEventDescriptor> eventDescript;
	private final String PROBLEMEVENT = "1";
	

	@Inject
	private ZabbixAdapterSetter zabAdapPaas;

	@Inject
	private ZabbixAdapterIaas zabAdapIaas;

	@Inject 
	private HostIDByName hostidfromZabbix;

	@Inject 
	private GroupIDByName hostgroupidfromZabbix;

	@Inject 
	private TimestampMonitoring timeMonit;


	@PostConstruct
	public void init(){

		this.triggerid = null;
		this.requestTime = null;
		this.hostid2Zabbix = null;
		this.hostNameEvent =null;
		this.groupNameEvent = null;
		this.eventDescript = new ArrayList<>();

	}


	public MonitPillarEventResponse getEvent(
			String testbedType, 
			String url,
			String token, 
			String vmuuid, 
			String service_category_id,
			String tag_service,
			String server_type, 
			String requestTime
			) throws MappingException, NoMappingModelFoundException, ServerErrorResponseException, APIErrorException, RestClientException, Exception{

		switch(server_type){
		case IAAS:
			return getWrappedEvent( 
					testbedType, 
					url, 
					token,
					vmuuid, 
					service_category_id,
					tag_service,
					server_type, 
					requestTime
					);

		case METRICS:
			return getWrappedEvent( 
					testbedType,
					url, 
					token, 
					vmuuid, 
					service_category_id,
					tag_service,
					server_type, 
					requestTime
					);

		case WATCHER:

			return getWrappedEvent( 
					testbedType, 
					url,
					token,
					vmuuid, 
					service_category_id,
					tag_service,
					server_type, 
					requestTime
					);

		}
		throw new NamingException("WRONG SERVER TYPE INSERTED: it must be one of iaas, metrics, watcher");
	}

	public MonitPillarEventResponse getWrappedEvent(
			String testbedType,
			String url, 
			String token,
			String vmuuid, 
			String service_category_id,
			String tag_service,
			String server_type, 
			String requestTime
			) throws NamingException, Exception{
		MonitPillarEventResponse wrappedEvent = new MonitPillarEventResponse();
		
		MetricsHistoryTimeRequest timerequest = new MetricsHistoryTimeRequest(); 
		ArrayList<ZabbixItemResponse> events = new ArrayList<ZabbixItemResponse>();

		if(!(vmuuid==null)){
			ArrayList<ZabbixMonitoredHostResponseV2_4> hostInfo = 
					zabAdapPaas.getMonitoredHosts(testbedType, url, token, ZabbixMethods.HOST.getzabbixMethod(), null, null, vmuuid);
			hostid2Zabbix = hostidfromZabbix.getHostIDV2_4(testbedType, url, token, null, vmuuid, tag_service, hostInfo);

			
		}
		else hostid2Zabbix = null;

		events = zabAdapPaas.getMetricsList(
				testbedType,
				url, 
				token, 
				hostid2Zabbix, 
				null,
				null,
				triggerid,
				ZabbixMethods.EVENT.getzabbixMethod(),
				requestTime );

		for(int countEvents=0; countEvents<events.size(); countEvents++){
			
			eventDescript = new ArrayList<>();
			
			events.get(countEvents).getValue();
			triggerid = events.get(countEvents).getObjectid();
			events.get(countEvents).getAcknowledged();
			
			ArrayList<ZabbixItemResponse> metricBytrigger = new ArrayList<ZabbixItemResponse>();
			metricBytrigger = zabAdapPaas.getMetricsList(
					testbedType, 
					url, 
					token, 
					hostid2Zabbix, 
					null,
					null,
					triggerid,
					ZabbixMethods.METRIC.getzabbixMethod(),
					requestTime);
			for (int countMetrics=0; countMetrics<metricBytrigger.size();countMetrics++){
				String metricName = metricBytrigger.get(countMetrics).getName();

				if (!(vmuuid==null)) hostNameEvent = vmuuid;
				else{
					ArrayList<ZabbixMonitoredHostsResponse> hostInfo = zabAdapPaas.getMonitoredHosts(
							testbedType, url, token, ZabbixMethods.HOST.getzabbixMethod(), hostid2Zabbix, null, vmuuid);

					hostNameEvent = hostInfo.get(countMetrics).getName();
				}

				if(groupNameEvent==null){
					ArrayList<ZabbixHostGroupResponse> hostGroupInfo = 
							zabAdapPaas.getHostGroupListinMetrics(
									testbedType, url, token, ZabbixMethods.HOSTGROUP.getzabbixMethod(), null, tag_service, hostid2Zabbix);

					groupNameEvent = hostGroupInfo.get(countMetrics).getName();
				}

				if(events.get(countMetrics).getValue().equals(PROBLEMEVENT)){
					MonitPillarEventDescriptor wrappedEventDescritp = new MonitPillarEventDescriptor();


					/*
					 * Logic for processing the data to return in function of requestTime
					 */
					String clockEvent = timeMonit.decodUnixTime2Date(Long.parseLong(events.get(countMetrics).getClock()));
					//					if(!(requestTime==null)){
					try{
						String datefrom = timerequest.getDateFrom().getYear()+"-"+
								timerequest.getDateFrom().getMonth()+"-"+
								timerequest.getDateFrom().getDay();
						String dateto = timerequest.getDateTo().getYear()+"-"+
								timerequest.getDateTo().getMonth()+"-"+
								timerequest.getDateTo().getDay();
						if(clockEvent.substring(0,9).contains(datefrom) || clockEvent.contains(dateto)){
							String clockEventFiletered = clockEvent;
							wrappedEventDescritp.setClock(clockEventFiletered);
						}
					}
					catch(NullPointerException ne){

						wrappedEventDescritp.setClock(timeMonit.decodUnixTime2Date(Long.parseLong(events.get(countMetrics).getClock())));
						if(!(events.get(countMetrics).getDescription()==null)){
							wrappedEventDescritp.setDescription(events.get(countMetrics).getDescription());
						}
						else wrappedEventDescritp.setDescription(
								"The metric " + metricName + " belonging to the host: " + 
										hostNameEvent + " into the group: " + 
										groupNameEvent + " has triggered the Event Problem"
								);
						String key = metricName +"."+ hostNameEvent +"."+ groupNameEvent;
						wrappedEventDescritp.setKey(key);

						eventDescript.add(wrappedEventDescritp);
					}
				}	
			}	
		}
		wrappedEvent.setEvents(eventDescript);
		return wrappedEvent;
	}
}
