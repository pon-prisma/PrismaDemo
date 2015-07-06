package it.prisma.businesslayer.orchestrator.dsl;

import it.prisma.businesslayer.orchestrator.dsl.deployment.DeployerType;
import it.prisma.domain.dsl.paas.services.GenericPaaSServiceRepresentation;

import java.io.Serializable;

public class UndeploymentMessage extends GenericPaaSManagementMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GenericPaaSServiceRepresentation paaSServiceRepresentation;
	private DeployerType deployerType;
//	private InfrastructureData infrastructureData;

	//private AbstractPaaSService paasService;
	//private PaaSDeployment paasDeployment;

//	private UserAccount account;
//	private Workgroup workgroup;

	private DeployerDeploymentData deploymentData;

	public GenericPaaSServiceRepresentation getPaaSServiceRepresentation() {
		return paaSServiceRepresentation;
	}

	public void setPaaSServiceRepresentation(GenericPaaSServiceRepresentation paaSServiceRepresentation) {
		this.paaSServiceRepresentation = paaSServiceRepresentation;
	}

	public DeployerType getDeployerType() {
		return deployerType;
	}

	public void setDeployerType(DeployerType deployerType) {
		this.deployerType = deployerType;
	}

//	public InfrastructureData getInfrastructureData() {
//		return infrastructureData;
//	}
//
//	public void setInfrastructureData(InfrastructureData infrastructureData) {
//		this.infrastructureData = infrastructureData;
//	}

//	public AbstractPaaSService getPaaSService() {
//		return paasService;
//	}
//
//	public void setPaaSService(AbstractPaaSService paasService) {
//		this.paasService = paasService;
//	}

//	public PaaSDeployment getPaasDeployment() {
//		return paasDeployment;
//	}
//
//	public void setPaasDeployment(PaaSDeployment paasDeployment) {
//		this.paasDeployment = paasDeployment;
//	}

//	public UserAccount getUserAccount() {
//		return account;
//	}
//
//	public void setUserAccount(UserAccount account) {
//		this.account = account;
//	}
//
//	public Workgroup getWorkgroup() {
//		return workgroup;
//	}
//
//	public void setWorkgroup(Workgroup workgroup) {
//		this.workgroup = workgroup;
//	}

	public DeployerDeploymentData getDeploymentData() {
		return deploymentData;
	}

	public void setDeploymentData(DeployerDeploymentData deploymentData) {
		this.deploymentData = deploymentData;
	}

	@Override
	public String toString() {
		return "UndeploymentMessage [paaSServiceRepresentation="
				+ paaSServiceRepresentation + ", deployerType=" + deployerType
				+ ", deploymentData=" + deploymentData + "]";
	}

}
