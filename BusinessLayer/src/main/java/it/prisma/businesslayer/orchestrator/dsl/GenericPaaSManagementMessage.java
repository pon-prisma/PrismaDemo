package it.prisma.businesslayer.orchestrator.dsl;

import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.paas.services.AbstractPaaSService;

import java.io.Serializable;

public abstract class GenericPaaSManagementMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private GenericPaaSServiceDeployRequest deployRequest;
	// private DeployerType deployerType;
	private InfrastructureData infrastructureData;

	protected AbstractPaaSService paasService;
	// private PaaSDeployment paasDeployment;
	//
	// private UserAccount account;
	protected Workgroup workgroup;

	//
	// private DeployerDeploymentData deploymentData;

	// public GenericPaaSServiceDeployRequest getDeployRequest() {
	// return deployRequest;
	// }
	//
	// public void setDeployRequest(GenericPaaSServiceDeployRequest
	// deployRequest) {
	// this.deployRequest = deployRequest;
	// }
	//
	// public DeployerType getDeployerType() {
	// return deployerType;
	// }
	//
	// public void setDeployerType(DeployerType deployerType) {
	// this.deployerType = deployerType;
	// }
	//
	public InfrastructureData getInfrastructureData() {
		return infrastructureData;
	}

	public void setInfrastructureData(InfrastructureData infrastructureData) {
		this.infrastructureData = infrastructureData;
	}

	public AbstractPaaSService getPaaSService() {
		return paasService;
	}

	public void setPaaSService(AbstractPaaSService paasService) {
		this.paasService = paasService;
	}

	// public PaaSDeployment getPaasDeployment() {
	// return paasDeployment;
	// }
	//
	// public void setPaasDeployment(PaaSDeployment paasDeployment) {
	// this.paasDeployment = paasDeployment;
	// }
	//
	// public UserAccount getUserAccount() {
	// return account;
	// }
	//
	// public void setUserAccount(UserAccount account) {
	// this.account = account;
	// }
	//
	public Workgroup getWorkgroup() {
		return workgroup;
	}

	public void setWorkgroup(Workgroup workgroup) {
		this.workgroup = workgroup;
	}

	//
	// public DeployerDeploymentData getDeploymentData() {
	// return deploymentData;
	// }
	//
	// public void setDeploymentData(DeployerDeploymentData deploymentData) {
	// this.deploymentData = deploymentData;
	// }

	@Override
	public String toString() {
		return "GenericPaaSManagementMessage [infrastructureData="
				+ infrastructureData + ", paasService=" + paasService
				+ ", workgroup=" + workgroup + "]";
	}
}
