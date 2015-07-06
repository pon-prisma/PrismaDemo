package it.prisma.businesslayer.orchestrator.dsl;

import it.prisma.businesslayer.orchestrator.dsl.deployment.DeployerType;

import java.io.Serializable;

public abstract class DeployerDeploymentData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected long deploymentCheckTimeout = 30 * 1000;
	protected long deploymentFailTimeout = 60 * 60 * 1000;
	protected long monitoringCheckTimeout = 30 * 1000;
	protected long monitoringFailTimeout = 30 * 60 * 1000;

	public long getDeploymentCheckTimeout() {
		return deploymentCheckTimeout;
	}

	public void setDeploymentCheckTimeout(long deploymentCheckTimeout) {
		this.deploymentCheckTimeout = deploymentCheckTimeout;
	}

	public long getDeploymentFailTimeout() {
		return deploymentFailTimeout;
	}

	public void setDeploymentFailTimeout(long deploymentFailTimeout) {
		this.deploymentFailTimeout = deploymentFailTimeout;
	}

	public long getMonitoringCheckTimeout() {
		return monitoringCheckTimeout;
	}

	public void setMonitoringCheckTimeout(long monitoringCheckTimeout) {
		this.monitoringCheckTimeout = monitoringCheckTimeout;
	}

	public long getMonitoringFailTimeout() {
		return monitoringFailTimeout;
	}

	public void setMonitoringFailTimeout(long monitoringFailTimeout) {
		this.monitoringFailTimeout = monitoringFailTimeout;
	}


	public abstract DeployerType getDeployerType();

}
