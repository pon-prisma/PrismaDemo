package it.prisma.businesslayer.orchestrator.dsl;

import it.prisma.businesslayer.orchestrator.dsl.deployment.DeployerType;
import it.prisma.dal.entities.iaas.tenant.IaaSTenant;

import java.io.Serializable;

public class InfrastructureData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PrismaZone zone;
	private DeployerType type;
	private IaaSTenant iaasTenant;
	private MonitoringEndpoint monitoringEndpoint;
	private MailEndpoint mailEndpoint;
	private DeployerEndpoint deployerEndpoint;

	public PrismaZone getZone() {
		return zone;
	}

	public void setZone(PrismaZone zone) {
		this.zone = zone;
	}

	public DeployerType getType() {
		return type;
	}

	public void setType(DeployerType type) {
		this.type = type;
	}

	public IaaSTenant getIaasTenant() {
		return iaasTenant;
	}

	public void setIaasTenant(IaaSTenant iaasTenant) {
		this.iaasTenant = iaasTenant;
	}

	public MonitoringEndpoint getMonitoringEndpoint() {
		return monitoringEndpoint;
	}

	public void setMonitoringEndpoint(MonitoringEndpoint monitoringEndpoint) {
		this.monitoringEndpoint = monitoringEndpoint;
	}

	public MailEndpoint getMailEndpoint() {
		return mailEndpoint;
	}

	public void setMailEndpoint(MailEndpoint mailEndpoint) {
		this.mailEndpoint = mailEndpoint;
	}

	public DeployerEndpoint getDeployerEndpoint() {
		return deployerEndpoint;
	}

	public void setDeployerEndpoint(DeployerEndpoint deployerEndpoint) {
		this.deployerEndpoint = deployerEndpoint;
	}

	@Override
	public String toString() {
		return "InfrastructureData [zone=" + zone + ", type=" + type
				+ ", iaasTenant=" + iaasTenant + ", monitoringEndpoint="
				+ monitoringEndpoint + ", mailEndpoint=" + mailEndpoint
				+ ", deployerEndpoint=" + deployerEndpoint + "]";
	}

}
