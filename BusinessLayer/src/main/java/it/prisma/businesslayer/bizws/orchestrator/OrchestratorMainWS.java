package it.prisma.businesslayer.bizws.orchestrator;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.request.APPaaSEnvironmentDeployRequest;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.dbaas.DBaaSRepresentation;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSRepresentation;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface OrchestratorMainWS {

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deployments/appaas")
	@PrismaWrapper
	public abstract APPaaSRepresentation provisioningAPPaaS(
			APPaaSDeployRequest request);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deployments/appaasenv")
	@PrismaWrapper
	public abstract APPaaSEnvironmentRepresentation provisioningAPPaaSEnvironment(
			APPaaSEnvironmentDeployRequest request);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deployments/mqaas")
	@PrismaWrapper
	public abstract MQaaSRepresentation provisioningMQaaS(
			MQaaSDeployRequest request);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deployments/dbaas")
	@PrismaWrapper
	public abstract DBaaSRepresentation provisioningDBaaS(
			DBaaSDeployRequest request);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deployments/biaas")
	@PrismaWrapper
	public abstract BIaaSRepresentation provisioningBIaaS(
			BIaaSDeployRequest request);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deployments/vmaas")
	@PrismaWrapper
	public abstract VMRepresentation provisioningVMaaS(
			VMDeployRequest request);

}