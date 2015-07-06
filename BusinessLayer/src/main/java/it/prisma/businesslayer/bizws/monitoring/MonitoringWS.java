package it.prisma.businesslayer.bizws.monitoring;

import it.prisma.businesslayer.bizws.config.annotations.PrismaWrapper;
import it.prisma.domain.dsl.monitoring.businesslayer.iaas.hypervisor.HypervisorGroup;
import it.prisma.domain.dsl.monitoring.businesslayer.iaas.infrastructure.InfrastructurePicture;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.IaaSHealth;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/monitoring")	
@PrismaWrapper
public interface MonitoringWS {

	
    	/**
	 * Get the information about the status of "storage", "network", "compute", "available_nodes" 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/iaas-status")
	public IaaSHealth getIaaSHealth();
    
	/**
	 * Get the information about the status of "storage", "network", "compute", "available_nodes" 
	 * and "total_nodes" and the list of VM that compose the infrastucture
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/infrastructure-status")
	public InfrastructurePicture getInfrastructurePicture();
	
	/**
	 * Gets information about the ypervisors
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/hypervisor")
	public HypervisorGroup getHypervisorInfo(@DefaultValue("zabbix") @QueryParam("adapter") String adapterType,  @QueryParam("groupName") String groupName, @QueryParam("hostName") String hostName);
	
	
	//TODO cosa succede se passo un gruppo che non è iaas
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{group-name}/{host-name}")
	public MonitoringWrappedResponseIaas getAllItemsFromHostGroupAndHostName(@DefaultValue("zabbix") @QueryParam("adapter") String adapterType,  @PathParam("group-name") String groupName, @PathParam("host-name") String hostName);
	
}