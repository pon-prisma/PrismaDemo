package it.monitoringpillar;


import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostGroupMonitoringCreateRequest;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringDeleteRequest;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringRequest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/adapters/{adapterType}")
public interface MonitoringPillarWS {
	
									/**********
									 *  IAAS
									 *********/

	/**************************
	 * GET IaaS Info by Groups
	 **************************/
	@Path("/iaas")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getIaaSInfo(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@QueryParam("iaasType") List<String> iaasType);
	
	/***********************************************
	 * GET IaaS Info by GROUP and HOSTNAMES
	 * *********************************************/
	/**
	 * Get the information about the status of "storage", "network", "compute", "available_nodes" 
	 */
	@Path("/machine/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response getIaaSInfoMachine(@DefaultValue("zabbix") @PathParam("adapterType") String adapterType, 
			@QueryParam("iaasType") List<String> iaasType,
			@QueryParam("iaashost")  List<String> iaashost);

	
	/******************************************************
	 * GET TRIGGERS STATUS (EVENT API Equivalent) BY GROUP
	 ******************************************************/
	@Path("/groups/{group}/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getTriggerHealth(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@PathParam("group") String group);
	
	
															/***********
															 * 	PAAS
															 ***********/

	/***********************************
	 * CREATE GROUP INTO PAAS PLATFORM
	 ************************************/
	@PUT
	@Path("/hostgroups")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMonitoringCreationHostGroup(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			HostGroupMonitoringCreateRequest hostGroupMonitoringCreateRequest) throws Exception;
	
	
	/*********************************
	 * CREATE HOST INTO PAAS PLATFORM
	 *********************************/
	@PUT
	@Path("/hosts")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMonitoringCreationHost(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			HostMonitoringRequest hostMonitoringRequest) throws Exception;
	
	
	/**************************************
	 * DELETE GROUP FROM PAAS PLATFORM
	 *************************************/
	@DELETE
	@Path("/hostgroups")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMonitoringDeleteHostGroup(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			HostGroupMonitoringCreateRequest hostGroupMonitoringDeleteRequest) throws Exception ;
	
	/**********************************
	 * DELETE HOST FROM PAAS PLATFORM
	 *********************************/
	@DELETE
	@Path("/hosts")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMonitoringDeleteHost(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			HostMonitoringDeleteRequest hostMonitoringDeleteRequest)
					throws Exception ;
	
	
	/***************************************************
	 * GET A SPECIFIC GROUP OF HOSTS
	 ***************************************************/
	/**
	 * In Prisma PaaS context there are category services such as DBaaS, AppaaS,
	 * etc. created by a specific user. Here the API consumer might be
	 * interested in having a picture of the general situation into a category
	 * Service group
	 */
	@Path("/groupsInfo/{group}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getSpecificServiceCategory(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@PathParam("group") String group);
	
	/*******************************************************************************
	 * GET A SPECIFIC HOST (and its carachteristics ) BY ITS NAME
	 ******************************************************************************/
	/**
	 * Method useful for having a SPECIFIC HOST info (and its characteristics )
	 * belonging to a SPECIFIC Category service regardless of the fact that it
	 * could be belonging to a group of host composing a Category service
	 * identified by the ID.
	 */
	@Path( "/hosts/{hostName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getSpecificHostbyHostUuid(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@PathParam("hostName") String hostName);
	
	
	/***************************
	 * SPECIFIC SERVICE BY TAG 
	 ***************************/
	/**
	 * A deployed service might be composed of two or more machines, so that
	 * it's needed to call those hosts belonging to the category by searching
	 * them by the serviceID attached to themselves at the creation stage. It
	 * retrieves all hosts belonging to a specific ATOMIC Service (properly
	 * tagged since the creation of it) created by the user.
	 */
	@GET
	@Path("/hosts")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getHostsByServiceId(@DefaultValue("zabbix") 
	@PathParam("adapterType") String adapterType, @QueryParam("service-id") String serviceId);
	
	/*********************************
	 * SPECIFIC METRIC REQUESTED
	 ********************************/
	/**
	 * Method useful for having a specific metric info (identified by its name)
	 * belonging to a specific host, category group, atomic service which all
	 * are to be specified in the request.
	 */
	@Path("/hosts/{host_id}" + "/metrics")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMetric(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@PathParam("host_id") String host_id, @QueryParam("metrics_id") List<String> metrics_id);
	
	/*****************************
	 * History without filter Time
	 ****************************/

	@Path("/hosts/{host_id}" 
			+ "/history"
			+ "/server/{server}"  
			+ "/metrics")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getHistory(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@PathParam("host_id") String host_id,
			@PathParam("server") String server,
			@QueryParam("metrics_id") List<String> metrics_id
			);
	
	/*********************************
	 * HISTORY WATCHER BY FILTER TIME
	 ********************************/
	/**
	 * Method useful for having a specific metric info (identified by its name)
	 * belonging to a specific host, category group, atomic service which all
	 * are to be specified in the request.
	 */
	@Path("/paas/host/{host_id}" + "/groups/{group}"
			+ "/service-id/{service_id}"
			+ "/history_filtered/{history_filtered}" + "/server/{server}"
			+ "/atomic_services" + "/metrics")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMetricWithTime(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@PathParam("host_id") String host_id,
			@PathParam("group") String group,
			@PathParam("service_id") String service_id,
			@PathParam("history_filtered") String history_filtered,
			@PathParam("server") String server,
			@QueryParam("atomic_services") List<String> atomic_services,
			@QueryParam("metrics_id") List<String> metrics_id,
			String requestTime);
	
	/*********************************
	 * WRAPPING EVENTS IN ALL SERVERS
	 *********************************/

	/*********************************
	 * HOST's EVENTS NO FILTER TIME
	 ********************************/
	/**
	 * Method useful for having Events happening into the monitoring platform.
	 * They can be filtered by period of time as much as the history
	 */

	@Path("/hosts/{host_id}"
			+ "/history_filtered/{history_filtered}" + "/server/{server}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getServerEvents(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@PathParam("host_id") String host_id,
			@PathParam("history_filtered") String history_filtered,
			@PathParam("server") String server, String requestTime); 
	
	/*********************************
	 * HOST's EVENTS WITH FILTER TIME
	 ********************************/
	/**
	 * Method useful for having Events happening into the monitoring platform.
	 * They can be filtered by period of time as much as the history
	 */

	@Path("/paas/host/{host_id}" + "/history_filtered/{history_filtered}"
			+ "/server/{server}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getServerFilteredEvents(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@PathParam("host_id") String host_id,
			@PathParam("history_filtered") String history_filtered,
			@PathParam("server") String server, String requestTime);
	
	/*****************
	 * DISABLE HOST
	 *****************/
	/**
	 * Method useful for having Events happening into the monitoring platform.
	 * They can be filtered by period of time as much as the history
	 */
	@Path("hosts/{hostName}" + "/server/{server}" + "/update/{update}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDisablement(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@PathParam("hostName") String hostName,
			@PathParam("server") String server,
			@PathParam("update") String update); 
	
	/*****************
	 * DISABLE METRIC
	 *****************/
	@Path("/hosts/{hostName}" + "/metrics/{metric_id}"
			+ "/server/{server}" + "/update/{update}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDisablementMetric(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@PathParam("hostName") String hostName,
			@PathParam("metric_id") String metric_id,
			// @PathParam("tag_service") String tag_service,
			@PathParam("server") String server,
			@PathParam("update") String update); 
	
}


