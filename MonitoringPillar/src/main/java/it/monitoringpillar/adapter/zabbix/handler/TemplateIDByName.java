package it.monitoringpillar.adapter.zabbix.handler;

import it.monitoringpillar.adapter.zabbix.MonitoringServerRouter;
import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ServerType;
import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.monitoringpillar.adapter.zabbix.metrics.ZabbixAdapterSetter;
import it.monitoringpillar.config.MonitoringConfig;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.PaaSMetric;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.apache.velocity.exception.ResourceNotFoundException;

@Stateless
public class TemplateIDByName implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String IAASTEMPLATE = "TemplateIaaS";
	private static final String TEMPLATE_RADIX = "Template";

	@Inject
	private ZabbixAdapterSetter zabAdapMetrics;

	@Inject
	private MonitoringConfig monitConf;

	@Inject
	private MonitoringServerRouter router;

	public List<String> getTemplateID(
			String testBedType, 
			String url,
			String token,
			String servertype,
			List<String> service
			) throws NotFoundException, Exception { 


		if(servertype.equals(ServerType.SERVERMETRICS.getServerType())){
			//			zabAdapMetrics.initPaaS(testBedType, serverType.SERVERMETRICS.getServerType()); 
			router.getURL(testBedType, ServerType.SERVERMETRICS.getServerType());
		}
		else if(servertype.equals(ServerType.SERVERWATCHER.getServerType())){
			//			zabAdapMetrics.initPaaS(testBedType, serverType.SERVERWATCHER.getServerType()); 
			router.getURL(testBedType, ServerType.SERVERWATCHER.getServerType());
		}
		else throw new ResourceNotFoundException("Wrong Server Type inserted or not existing");

		@SuppressWarnings("unchecked")
		ArrayList<ZabbixTemplateResponse> templateList = 
		(ArrayList<ZabbixTemplateResponse>) zabAdapMetrics.getTemplateIdListMetrics(
				testBedType,
				url,
				token,
				ZabbixMethods.TEMPLATE.getzabbixMethod());

		ArrayList<String> templateID = new ArrayList<String>(); 

		//		for (int j=0; j<templateList.size(); j++) {
		//			String templateName = templateList.get(j).getName();
		//			if(templateName.equalsIgnoreCase(monitConf.getIaaSTemplate())){
		//				templateID.add(templateList.get(j).getTemplateid());
		//			}

		//FOR EACH TEMPLATE
		for (int j=0; j<templateList.size(); j++) {
			String templateName = templateList.get(j).getName();
			//ADD IAAS TEMPLATE NO MATTER WHAT..
			if(
//					templateName.equalsIgnoreCase(IAASTEMPLATE
//					monitConf.getIaaSTemplate()
//					Pattern.compile(Pattern.quote(templateName.toLowerCase()), Pattern.CASE_INSENSITIVE).matcher(IAASTEMPLATE.toLowerCase()).find()
					templateName.equalsIgnoreCase(IAASTEMPLATE)
					){
				templateID.add(templateList.get(j).getTemplateid());
			}
			//FOR EACH SERVICE PASSED
			for (int z=0; z<service.size(); z++){

				String serviceName = templateName.substring(templateName.lastIndexOf("ate")+3);

				if(templateName.toLowerCase().equalsIgnoreCase(TEMPLATE_RADIX+service.get(z))
//						Pattern.compile(Pattern.quote(templateName.toLowerCase()), Pattern.CASE_INSENSITIVE).matcher(service.get(z).toLowerCase()).find()
//						templateName.equalsIgnoreCase(service.get(z))
						){
					templateID.add(templateList.get(j).getTemplateid());
				}
			}
		}
		if (templateID.size()-1 != service.size()){
			throw new NotFoundException("Not Service Existing into Monitoring Platform at: " + url);
		}
		return templateID;
	}

//	protected PaaSMetric getMetricByName(List<PaaSMetric> list, String name) throws Exception {
//		for(PaaSMetric metric : list)
//			if(metric.getMetricName().equalsIgnoreCase(name))
//				return metric;
//
//		throw new Exception("metric name does not exists");
//	} 
}