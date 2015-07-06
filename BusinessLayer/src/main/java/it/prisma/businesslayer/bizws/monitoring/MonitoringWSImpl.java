package it.prisma.businesslayer.bizws.monitoring;

import it.prisma.businesslayer.bizlib.monitoring.MonitoringBean;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.config.EnvironmentConfig;
import it.prisma.domain.dsl.exceptions.monitoring.NotFoundMonitoringException;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.HostIaaS;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility.IaaSHostGroups;
import it.prisma.domain.dsl.monitoring.businesslayer.iaas.hypervisor.HypervisorGroup;
import it.prisma.domain.dsl.monitoring.businesslayer.iaas.infrastructure.InfrastructurePicture;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.IaaSHealth;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;

import javax.inject.Inject;

public class MonitoringWSImpl extends BaseWS implements MonitoringWS {

	@Inject
	private MonitoringBean monitoring;
	
	@Inject
	private EnvironmentConfig configuration;
	
	@Override
	public IaaSHealth getIaaSHealth() {
	    	    
	    try {
		return monitoring.getIaaSHealth();
		 
	    } catch (Exception e) {
		throw new PrismaWrapperException(e.getMessage(), e);
	    }
	}
	
	public InfrastructurePicture getInfrastructurePicture() {
		try {
			return monitoring.getInfrastructurePicture();
		} catch (Exception e) {
			throw new PrismaWrapperException(e.getMessage(), e);
		}
	}

	@Override
	public MonitoringWrappedResponseIaas getAllItemsFromHostGroupAndHostName(
			String adapterType, String groupName, String hostName) {
		try {
			return monitoring.getItemsByHost(adapterType, groupName, hostName);
		} catch (Exception e) {
			throw new PrismaWrapperException(e.getMessage(), e);
		}
	}

	@Override
	public HypervisorGroup getHypervisorInfo(String adapterType,
			String groupName, String hostName) {
		// TODO Auto-generated method stub
		return null;
	}



//	@Override
//	public HypervisorGroup getHypervisorInfo(String adapterType,
//			String groupName, String hostName) {
//		try {
//			return monitoring.getHypervisorInfo();
//		} catch (Exception e) {
//			throw new PrismaWrapperException(e.getMessage(), e);
//		}
//	}
	

}