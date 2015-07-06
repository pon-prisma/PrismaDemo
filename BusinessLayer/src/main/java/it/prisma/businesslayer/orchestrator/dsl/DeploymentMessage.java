package it.prisma.businesslayer.orchestrator.dsl;

import it.prisma.businesslayer.orchestrator.dsl.deployment.DeployerType;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.paas.deployments.PaaSDeployment;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;

import java.io.Serializable;

public class DeploymentMessage extends GenericPaaSManagementMessage implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GenericPaaSServiceDeployRequest deployRequest;
	private DeployerType deployerType;
	private QuotaLimits quotaLimits = QuotaLimits.defaults();
	// private InfrastructureData infrastructureData;

	// private AbstractPaaSService paasService;
	private PaaSDeployment paasDeployment;

	private UserAccount account;
	// private Workgroup workgroup;

	private DeployerDeploymentData deploymentData;

	public GenericPaaSServiceDeployRequest getDeployRequest() {
		return deployRequest;
	}

	public void setDeployRequest(GenericPaaSServiceDeployRequest deployRequest) {
		this.deployRequest = deployRequest;
	}

	public DeployerType getDeployerType() {
		return deployerType;
	}

	public void setDeployerType(DeployerType deployerType) {
		this.deployerType = deployerType;
	}

	// public InfrastructureData getInfrastructureData() {
	// return infrastructureData;
	// }
	//
	// public void setInfrastructureData(InfrastructureData infrastructureData)
	// {
	// this.infrastructureData = infrastructureData;
	// }

	// public AbstractPaaSService getPaaSService() {
	// return paasService;
	// }
	//
	// public void setPaaSService(AbstractPaaSService paasService) {
	// this.paasService = paasService;
	// }

	public PaaSDeployment getPaasDeployment() {
		return paasDeployment;
	}

	public void setPaasDeployment(PaaSDeployment paasDeployment) {
		this.paasDeployment = paasDeployment;
	}

	public UserAccount getUserAccount() {
		return account;
	}

	public void setUserAccount(UserAccount account) {
		this.account = account;
	}

	// public Workgroup getWorkgroup() {
	// return workgroup;
	// }
	//
	// public void setWorkgroup(Workgroup workgroup) {
	// this.workgroup = workgroup;
	// }

	public DeployerDeploymentData getDeploymentData() {
		return deploymentData;
	}

	public void setDeploymentData(DeployerDeploymentData deploymentData) {
		this.deploymentData = deploymentData;
	}

	public QuotaLimits getQuotaLimits() {
		return quotaLimits;
	}

	public void setQuotaLimits(QuotaLimits quotaLimits) {
		this.quotaLimits = quotaLimits;
	}

	@Override
	public String toString() {
		return "DeploymentMessage [deployRequest=" + deployRequest
				+ ", deployerType=" + deployerType + ", quotaLimits="
				+ quotaLimits + ", paasDeployment=" + paasDeployment
				+ ", account=" + account + ", deploymentData=" + deploymentData
				+ "]";
	}

}
