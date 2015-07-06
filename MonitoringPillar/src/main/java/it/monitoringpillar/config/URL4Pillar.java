package it.monitoringpillar.config;

import it.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ServerType;

import java.io.Serializable;

import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Singleton;

@Startup
@Singleton
public class URL4Pillar implements Serializable {

	private static final long serialVersionUID = 1L;
	private String chosenURL;

	private String prismaIaasServer;
	private String prismaMetricsServer;
	private String prismaWatcherServer;
	private String URLProduct;

	@Inject
	private EnvironmentDeployConfig envDeplConf;


	public String getURLfromPillar(String testbedType, String monitoringType) throws Exception{

		if (testbedType.equals(envDeplConf.getMonitoringEnvironment()) && monitoringType.equals(ServerType.SERVERIAAS.getServerType()))
		{
			chosenURL = envDeplConf.getMonitoringIaaSURL();
		}
		else if (testbedType.equals(envDeplConf.getMonitoringEnvironment()) && monitoringType.equals(ServerType.SERVERMETRICS.getServerType()))
		{
			chosenURL = envDeplConf.getMonitoringMetricsURL();
		}
		else if (testbedType.equals(envDeplConf.getMonitoringEnvironment()) && monitoringType.equals(ServerType.SERVERWATCHER.getServerType()))
		{
			chosenURL = envDeplConf.getMonitoringWatcherURL();
		}
		return chosenURL;
	}
}