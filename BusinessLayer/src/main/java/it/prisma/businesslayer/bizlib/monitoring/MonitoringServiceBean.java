package it.prisma.businesslayer.bizlib.monitoring;

import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.dal.dao.monitoring.AtomicServiceDAO;
import it.prisma.dal.dao.monitoring.MetricDAO;
import it.prisma.dal.entities.monitoring.AtomicService;
import it.prisma.dal.entities.monitoring.Metric;
import it.prisma.dal.entities.monitoring.Threashold;
import it.prisma.domain.dsl.exceptions.monitoring.MonitoringException;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringDeleteRequest;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringRequest;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.CreatedHostInServer;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.PaaSMetric;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.PaasMachine;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.PaasThreshold;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.PaasThreshold.PaasThresholdStatus;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.Service;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiclients.prisma.PrismaMonitoringAPIClient;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class MonitoringServiceBean {

    private static final Logger LOG = LogManager.getLogger(MonitoringServiceBean.class);

    private PrismaMonitoringAPIClient monitAPIClient;

    @Inject
    private AtomicServiceDAO atomicServiceDAO;
    
    @Inject
    private MetricDAO metricDAO;

    @Inject
    private EnvironmentConfig envConf;

    @PostConstruct
    private void init() {
	LOG.debug("Init MonitoringServiceBean");
	this.monitAPIClient = new PrismaMonitoringAPIClient(
		envConf.getSvcEndpointProperty(EnvironmentConfig.SVCEP_MONITORING_PILLAR_URL));
    }

    /**
     * Add host to monitoring
     * 
     * @param adapterType
     * @param vmuuid
     * @param vmip
     * @param group
     * @param serviceCategory
     * @param serviceID
     * @param services
     * @return
     * @throws MonitoringException
     */
    public CreatedHostInServer addServiceToMonitoring(String adapterType, String vmuuid, String vmip, String group,
	    String serviceCategory, String serviceID, List<String> services) throws MonitoringException, Exception {

	try {

	    LOG.debug("Adding service to monitoring with UUID: " + vmuuid + " IP: " + vmip);

	    HostMonitoringRequest hostMonitoringRequest = new HostMonitoringRequest();

	    hostMonitoringRequest.setIp(vmip);
	    hostMonitoringRequest.setUuid(vmuuid);
	    hostMonitoringRequest.setServiceCategory(serviceCategory);
	    hostMonitoringRequest.setHostGroup(group);
	    hostMonitoringRequest.setServiceId(serviceID);
	    hostMonitoringRequest.setAtomicServices(services);

	    return monitAPIClient.addHostToMonitoring(adapterType, hostMonitoringRequest);

	    // TODO migliorare gestione eccezioni
	} catch (APIErrorException | MappingException | IOException | RestClientException
		| NoMappingModelFoundException | ServerErrorResponseException e) {
	    throw new MonitoringException("Fail to add service: " + vmuuid + " IP: " + vmip + " to monitoring. "
		    + e.getMessage(), e);
	} catch (Exception e) {
	    throw new Exception("Exception while adding service to monitoring: " + e.getMessage(), e);
	}
    }

    /**
     * Delete Host from monitoring
     * 
     * @param adapterType
     * @param vmuuid
     * 
     * @throws MonitoringException, Exception if an error occured
     */
    public void removeServiceToMonitoring(String adapterType, String vmuuid) throws MonitoringException,
	    Exception {

	try {
	    LOG.debug("Deleting service to monitoring: " + vmuuid);

	    HostMonitoringDeleteRequest hostMonitoringDeleteRequest = new HostMonitoringDeleteRequest();
	    hostMonitoringDeleteRequest.setUuid(vmuuid);

	    monitAPIClient.deleteHostFromMonitoring(adapterType, hostMonitoringDeleteRequest);
	    // TODO migliorare gestione eccezioni
	} catch (APIErrorException e){
	    if(e.getResponseMessage().getHttpStatusCode() == Status.NOT_FOUND.getStatusCode()){
		LOG.debug("VM: " + vmuuid + " NOT found on monitoring; considered as removed");
	    } else {
		throw e; 
	    }
    	} catch (MappingException | IOException | RestClientException
		| NoMappingModelFoundException | ServerErrorResponseException e) {
	    throw new MonitoringException("Fail to remove VM: " + vmuuid + " from monitoring", e);
	} catch (Exception e) {
	    throw new Exception("Exception removing service from monitoring", e);
	}
    }

    /**
     * Checks if the PaaSService is running.
     * 
     * @param adapterType for example "zabbix"
     * @param serviceGroup the PRISMA Workgroup ID
     * @param serviceId the PaaSService ID
     * 
     * @return true if the service is running
     * 
     * @throws Exception
     */
    public boolean isServiceRunning(String adapterType, String serviceGroup, String serviceId)
	    throws Exception {

	MonitoringWrappedResponsePaas monitoringWrappedResponsePaas = monitAPIClient.getMonitoringServiceByTag(
		adapterType, serviceId);

	// Recupero la lista delle VM che compongono il servizio PRISMA
	List<PaasMachine> machines = monitoringWrappedResponsePaas.getGroups().get(0).getPaasMachines();
	// Itero su tutte le macchine che compongono il servizio
	for (PaasMachine machine : machines) {
	    // Ogni macchina può avere più servizio
	    for (Service service : machine.getServices()) {
		// Verifico che tutti i servizi atomici (MySQL, Apache... )
		// sulla VM siano in running
		if (!isAtomicServiceRunning(service)) {
		    LOG.debug("AtomicService " + service.getServiceName() + " is NOT running");
		    return false;
		} else {
		    LOG.debug("AtomicService " + service.getServiceName() + " is running");
		}
	    }
	}
	LOG.debug("PaaSService " + serviceId + " is running");
	return true;
    }


    /**
     * Returns <tt>true</tt> if the {@link Service} is running. <br />
     * For the specified {@link Service}, this method performs a lookup on the
     * {@link MonitoringService} Enum. Afterwards it searches among the
     * {@link PaaSMetric} that compose the service in order to establish if the
     * specified service is running.
     * 
     * 
     * @param service
     *            service to enstablish if is running
     * @throws IllegalArgumentException
     *             if the service is not insede the {@link MonitoringService}
     *             Enum
     * @return <tt>true</tt> if the service is running
     * @throws MonitoringException 
     * 
     */
    private boolean isAtomicServiceRunning(Service service) throws IllegalArgumentException, MonitoringException {
	
	//Create a MAP from monitoring DSL
	Map<String, PaaSMetric> monitoringMetricMap = new HashMap<String, PaaSMetric>();
	for(PaaSMetric paasMetric : service.getPaasMetrics()) {
	    if(!monitoringMetricMap.containsKey(paasMetric.getMetricName())){
		monitoringMetricMap.put(paasMetric.getMetricName(), paasMetric);
	    } else {
		LOG.error("Duplicate metric in monitoring DSL: " +  paasMetric.getMetricName());
	    }
	}
	
	//Find if there is the service in the DB
	//Può essere omesso
	AtomicService atomicService = atomicServiceDAO.findByName(service.getServiceName());
	if(atomicService == null){
	    throw new MonitoringException("CONFIGURATION ERROR: cannot find service " + service.getServiceName() + " into DB");
	}
	
	Collection<Metric> metrics = metricDAO.findByAtomicServiceAndPurposeLevel(atomicService.getId(), Metric.PURPOSE_STATUS);
	/**
	 * For all the metrics with "PURPOSE_STATUS = 2" linked to the AtomicService check if metric is OK
	 */
	for(Metric m : metrics) {
	    if(monitoringMetricMap.containsKey(m.getName())){
		PaaSMetric paasMetric = monitoringMetricMap.get(m.getName());
		if( paasMetric.getMetricValue() < 0f ) {
		    LOG.debug("Metric " + m.getName() + " is not running. Current value: " + paasMetric.getMetricValue());
		    return false;
		} else {
		    
		    LOG.debug("Current value of metric [" + m.getName() + "] is : " + paasMetric.getMetricValue());

		    
		    //Create a MAP with all threshold from monitoring DSL
		    Map<String, PaasThreshold> monitoringPaasThresholdMap = new HashMap<String, PaasThreshold>();
		    for(PaasThreshold paasThreshold : paasMetric.getPaasThresholds()) {
			if(!monitoringPaasThresholdMap.containsKey(paasThreshold.getThresholdName())){
			    monitoringPaasThresholdMap.put(paasThreshold.getThresholdName(), paasThreshold);
			} else {
			    LOG.error("Duplicate threshold in monitoring DSL: " +  paasThreshold.getThresholdName());
			}
		    }
		   for(Threashold t : m.getThreasholds()) {
		       if(monitoringPaasThresholdMap.containsKey(t.getName())){
			   if(monitoringPaasThresholdMap.get(t.getName()).getThresholdStatus().equals(PaasThresholdStatus.PROBLEM)){
			       LOG.debug("Problem at PaasThreshold " + t.getName() + " of metric: " + m.getName()
					+ " status = " + monitoringPaasThresholdMap.get(t.getName()).getThresholdStatus().toString());
			       return false;
			   } else {
			       LOG.debug("PaasThreshold " + t.getName() + " of metric: " + m.getName() + " status = " + monitoringPaasThresholdMap.get(t.getName()).getThresholdStatus().toString());
			   }
		       } else {
			   LOG.error("CONFIGURATION ERROR: cannot find Threashold " + t.getName() + " for metric " + m.getName() + " into DB");
			   throw new MonitoringException("Cannot find Threashold " + t.getName() + " for metric " + m.getName() + " into DB");
		       }
		   }		
		}
		
	    } else {
		throw new MonitoringException("CONFIGURATION ERROR - Cannot find metric " + m.getName() + " in monitoring response.");
	    }
	}
	return true;

    }

    protected PaaSMetric getMetricByName(List<PaaSMetric> list, String name) throws NamingException {
	for (PaaSMetric metric : list)
	    if (metric.getMetricName().equalsIgnoreCase(name))
		return metric;

	return null;
    }

}