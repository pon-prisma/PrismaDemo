package it.prisma.businesslayer.bizlib.orchestration;

import it.prisma.businesslayer.orchestrator.dsl.InfrastructureData;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.iaas.services.vm.VMaaS;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entities.paas.services.biaas.BIaaS;
import it.prisma.dal.entities.paas.services.dbaas.DBaaS;
import it.prisma.dal.entities.paas.services.mqaas.MQaaS;
import it.prisma.dal.entitywrappers.paas.services.appaas.AppaaS;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSEnvironmentDeployRequest;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;

import javax.validation.Valid;

public interface DeploymentOrchestrator {

	public AppaaS deployAPPaaS(@Valid APPaaSDeployRequest request)
			throws Exception;

	public AppaaSEnvironment deployAPPaaSEnvironment(
			@Valid APPaaSEnvironmentDeployRequest request) throws Exception;

	public MQaaS deployMQaaS(@Valid MQaaSDeployRequest request)
			throws Exception;

	public DBaaS deployDBaaS(@Valid DBaaSDeployRequest request)
			throws Exception;

	public BIaaS deployBIaaS(@Valid BIaaSDeployRequest request)
			throws Exception;
			
	public VMaaS deployVMaaS(@Valid VMDeployRequest request)
			throws Exception;
	
	public InfrastructureData buildInfrastructureData(PaaSServiceType paaSServiceType,
			Workgroup workgroup);
	
	public void undeployAPPaaSEnvironment(Long userAccountId, Long workgroupId, Long appId, Long envId)
			throws Exception;

	public void undeployVMaaS(Long userAccountId, Long workgroupId, Long resourceId) throws Exception;

	public void undeployDBaaS(Long userAccountId, Long workgroupId, Long resourceId) throws Exception;

	public void undeployMQaaS(Long userAccountId, Long workgroupId, Long resourceId) throws Exception;

	public void undeployBIaaS(Long userAccountId, Long workgroupId, Long resourceId) throws Exception;



}
