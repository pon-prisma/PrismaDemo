package it.monitoringpillar;

import it.monitoringpillar.config.DetermineAdapter;
import it.monitoringpillar.config.EnvironmentDeployConfig;
import it.monitoringpillar.config.PillarEnvironmentConfig;
import it.prisma.domain.dsl.monitoring.MonitoringFeatureUtility;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostGroupMonitoringCreateRequest;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringDeleteRequest;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringRequest;
import it.prisma.domain.dsl.monitoring.businesslayer.paas.response.MonitoringVMCredentialsResponse;
import it.prisma.domain.dsl.monitoring.pillar.protocol.MonitoringErrorCode;
import it.prisma.domain.dsl.monitoring.pillar.protocol.MonitoringResponse;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.MonitPillarEventResponse;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.MonitoringWrappedResponseIaas;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.CreatedHostInServer;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.prisma.domain.dsl.monitoring.pillar.wrapper.paas.UpdatedPaasGroup;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.HostGroupResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.JSONRPCResponse;
import it.prisma.domain.dsl.monitoring.pillar.zabbix.response.UpdatedItemsResponse;
import it.prisma.utils.misc.StackTrace;
import it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException;
import it.prisma.utils.web.ws.rest.apiencoding.MappingException;
import it.prisma.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.prisma.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.prisma.utils.web.ws.rest.restclient.exceptions.RestClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * 
 * @author m.grandolfo <br>
 * <br>
 *         Class containing RESTful API that wrap Monitoring products' API such
 *         as Zabbix's ones
 * 
 */

@Path("/adapters/{adapterType}")
public class MonitoringPillarWSImpl implements MonitoringPillarWS{

	private String adapterType;
	// private Object adapterResponse;
	private static Logger LOGMonit = Logger.getLogger(MonitoringPillarWSImpl.class);
	private MonitoringWrappedResponsePaas wrappedPaas;
	private MonitoringWrappedResponseIaas wrappedIaas;
	private MonitPillarEventResponse wrappedEvent;
	private WrappedIaasHealthByTrigger wrapperTrigger;

	private JSONRPCResponse<MonitoringVMCredentialsResponse> responseUpdate;
	private JSONRPCResponse<UpdatedItemsResponse> responseUpdatedItems;
	private MonitoringFeatureUtility monitFeat; 

	
	@Inject
	DetermineAdapter determineAdapt;

	@Inject
	private PillarEnvironmentConfig envConf;

	@Inject
	private EnvironmentDeployConfig envDeployConf;

	@PostConstruct
	public void init() throws IOException {

		this.wrappedPaas = new MonitoringWrappedResponsePaas();
		this.wrappedIaas = new MonitoringWrappedResponseIaas();
		this.wrappedEvent = new MonitPillarEventResponse();
		this.responseUpdate = new JSONRPCResponse<>();
		this.monitFeat = new MonitoringFeatureUtility();
		this.wrapperTrigger = new WrappedIaasHealthByTrigger();
	}


	/**
	 * 
	 * Method useful for: <br>
	 * <ul>
	 * <li>creating a host to be monitored </br>
	 * 
	 * @param bodyRequest
	 * <br>
	 *            JSON String with the body request <br>
	 *            { <br>
	 *            POST: "http://<url-pillar>/monitoring/prisma-services/", <br>
	 *            { <br>
	 *            "testbed-type":"infn", <br>
	 *            "action-type":"create_monitored_host", <br>
	 *            "parameters":{ <br>
	 *            "vmuuid": "6ad90238-9a82-4235-bb90-e0bd2aed627b", <br>
	 *            "vmip":"<public or private machine's ip>", <br>
	 *            "service-category":"<service-category>", <br>
	 *            "tag-service":"<service-id>", <br>
	 *            "service-type":["<atomic services>"] <br>
	 *            , <br>
	 *            "adapter-type":"zabbix"
	 * 
	 * <br>
	 * <br>
	 * @return the ID belonging to the cancelled host
	 *         </ul>
	 * <br>
	 * <br>
	 *         <ul>
	 *         <li>deleting a monitored host </br>
	 * @param bodyRequest
	 * <br>
	 *            JSON String with the body request <br>
	 *            { <br>
	 *            POST: "http://<url-pillar>/monitoring/prisma-services/", <br>
	 *            { <br>
	 *            "testbed-type":"infn", <br>
	 *            "action-type":"delete_monitored_host", <br>
	 *            "parameters":{ <br>
	 *            "vmuuid": "6ad90238-9a82-4235-bb90-e0bd2aed627b", <br>
	 *            , <br>
	 *            "adapter-type":"zabbix" <br>
	 * 
	 * <br>
	 * @return the ID belonging to the cancelled host
	 *         </ul>
	 * 
	 * <br>
	 * <br>
	 * 
	 */


	/********************************************************************************************
	 * 											IaaS
	 ********************************************************************************************/
	/**************************
	 * GET IaaS Info by Groups
	 **************************/
	/**
	 * Method that wraps Zabbix API in order to provide info about the gruop
	 * containing all the monitored hosts, the metrics associated to themselves,
	 * as well as trigger. The API consumer will be able to take actions based
	 * on the metrics and triggers' actual values. All this coming from a
	 * specific testbed, zabbix server (in this case zabbic iaas) and adapter.
	 */
	@Override
	public Response getIaaSInfo(
			String adapterType, List<String> iaasType) {

		try {
			wrappedIaas = determineAdapt.getAdapter(adapterType).getIaaSOverallMachines(
					envDeployConf.getMonitoringEnvironment(), iaasType, null);
		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, wrappedIaas).build()
				.build();	
	}

	/***********************************************
	 * GET IaaS Info by GROUP and HOSTNAMES
	 * *********************************************/
	@Override
	public Response getIaaSInfoMachine(String adapterType, List<String> iaasType, List<String> iaashost) {

		try {
				wrappedIaas = determineAdapt.getAdapter(adapterType) 
						.getIaaSOverallMachines(
								envDeployConf.getMonitoringEnvironment(), iaasType,
								iaashost);
		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, wrappedIaas).build()
				.build();
	}

	/******************************************************
	 * GET TRIGGERS STATUS (EVENT API Equivalent) BY GROUP
	 * 
	 * @param adapter
	 * @param host_id
	 * @param server
	 * @param requestTime
	 * @return
	 ************************************************/

	@Override
	public Response getTriggerHealth( String adapterType, String group){

		try {
			wrapperTrigger = determineAdapt.getAdapter(adapterType).getIaasHealth(
					envDeployConf.getMonitoringEnvironment(), group);

		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, wrapperTrigger).build()
				.build();
	}

	/***********************************************************************
	 * 											PaaS
	 ***********************************************************************/

	/*****************************************
	 * CREATE GROUP INTO PAAS PLATFORM
	 * @param adapterType
	 * @param hostGroupMonitoringCreateRequest
	 * @return
	 * @throws Exception
	 */

	@Override
	public Response getMonitoringCreationHostGroup( String adapterType,
			HostGroupMonitoringCreateRequest hostGroupMonitoringCreateRequest) throws Exception  {

		UpdatedPaasGroup resultgroupID = new UpdatedPaasGroup();

		String errorMessage;
		switch (adapterType) {
		case "zabbix":
			int z;
			try {
				@SuppressWarnings("unchecked")
				ArrayList<JSONRPCResponse<HostGroupResponse>> hosts = 
				(ArrayList<JSONRPCResponse<HostGroupResponse>>) determineAdapt.getAdapter(adapterType)
				.creationMonitoredHostGroup(
						envDeployConf.getMonitoringEnvironment(),
						hostGroupMonitoringCreateRequest.getHostGroupName());

				for (z = 0; z < hosts.size();) {
					try {
						hosts.get(z).getError().equals(null);
						errorMessage = hosts.get(z).getError().getMessage()
								+ hosts.get(z).getError().getData();
						return MonitoringResponse
								.status(Status.BAD_REQUEST,
										MonitoringErrorCode.MONITORING_PILLAR_ERROR,
										StackTrace
										.getStackTraceToString(new Exception(
												errorMessage))).build()
												.build();

					} catch (NullPointerException e) {

						resultgroupID.setGroupsIDInMetrics(hosts.get(z)
								.getResult().getGroupids());
						z++;
						resultgroupID.setGroupsIDinWatcher(hosts.get(z)
								.getResult().getGroupids());
						return MonitoringResponse
								.status(Status.OK, resultgroupID).build()
								.build();
					}
				}
			} catch (MappingException e) {
				e.printStackTrace();
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException e) {
				LOGMonit.error("MappingException");
				e.printStackTrace();
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (NoMappingModelFoundException e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (ServerErrorResponseException e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (RestClientException e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			}
		}
		String error = " { \"Error\"  :  \" Wrong Parameter\" }";
		return MonitoringResponse.status(Status.BAD_REQUEST, error).build()
				.build();
	}


	/*********************************
	 * CREATE HOST INTO PAAS PLATFORM
	 * @param adapterType
	 * @param hostMonitoringRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	public Response getMonitoringCreationHost( String adapterType, HostMonitoringRequest hostMonitoringRequest) 
			throws Exception {

		CreatedHostInServer resulthostsID = new CreatedHostInServer();

		String errorMessage;
		switch (adapterType) {
		case "zabbix":
			int z;
			try {
				@SuppressWarnings("unchecked")
				ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>> hosts = 
				(ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>>) determineAdapt.getAdapter(adapterType)
				.creationMonitoredHost(
						envDeployConf.getMonitoringEnvironment(),
						hostMonitoringRequest.getHostGroup(),
						hostMonitoringRequest.getUuid(),
						hostMonitoringRequest.getIp(),
						hostMonitoringRequest.getServiceCategory(),
						hostMonitoringRequest.getServiceId(),
						hostMonitoringRequest.getAtomicServices());

				for (z = 0; z < hosts.size();) {
					try {
						hosts.get(z).getError().equals(null);
						errorMessage = hosts.get(z).getError().getMessage()
								+ hosts.get(z).getError().getData();
						return MonitoringResponse
								.status(Status.BAD_REQUEST,
										MonitoringErrorCode.MONITORING_PILLAR_ERROR,
										StackTrace
										.getStackTraceToString(new Exception(
												errorMessage))).build()
												.build();

					} catch (NullPointerException e) {

						resulthostsID.setHostIDInMetrics(hosts.get(z)
								.getResult().getHostids());
						z++;
						resulthostsID.setHostIDinWatcher(hosts.get(z)
								.getResult().getHostids());
						return MonitoringResponse
								.status(Status.OK, resulthostsID).build()
								.build();
					}
				}
			} catch (MappingException e) {
				e.printStackTrace();
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException e) {
				LOGMonit.error("MappingException");
				e.printStackTrace();
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (NoMappingModelFoundException e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (ServerErrorResponseException e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (RestClientException e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (NotFoundException e) {
					e.printStackTrace();
					LOGMonit.error(e, e);
					return MonitoringResponse
							.status(Status.NOT_FOUND,
									MonitoringErrorCode.MONITORING_PILLAR_ERROR,
									StackTrace.getStackTraceToString(e)).build()
									.build();
			} catch (NamingException e) {
				return MonitoringResponse
						.status(Status.NOT_FOUND,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			}
		}
		String error = " { \"Error\"  :  \" Wrong Parameter\" }";
		return MonitoringResponse.status(Status.BAD_REQUEST, error).build()
				.build();
	}

	/*******************************************
	 * DELETE GROUP FROM PAAS PLATFORM
	 * @param adapterType
	 * @param hostGroupMonitoringDeleteRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	public Response getMonitoringDeleteHostGroup(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			HostGroupMonitoringCreateRequest hostGroupMonitoringDeleteRequest) throws Exception {

		UpdatedPaasGroup resultgroupID = new UpdatedPaasGroup();
		String errorMessage;

		int v;
		try {
			@SuppressWarnings("unchecked")
			ArrayList<JSONRPCResponse<HostGroupResponse>> groups = (ArrayList<JSONRPCResponse<HostGroupResponse>>) determineAdapt.getAdapter(adapterType)
			.deleteMonitoredHostGroup(
					envDeployConf.getMonitoringEnvironment(),
					hostGroupMonitoringDeleteRequest.getHostGroupName());

			for (v = 0; v < groups.size();) {
				resultgroupID.setGroupsIDInMetrics(groups.get(v).getResult()
						.getGroupids());
				v++;
				resultgroupID.setGroupsIDinWatcher(groups.get(v).getResult()
						.getGroupids());
				try {
					groups.get(v).getError().equals(null);
					errorMessage = groups.get(v).getError().getMessage()
							+ groups.get(v).getError().getData();
					return MonitoringResponse
							.status(Status.BAD_REQUEST,
									MonitoringErrorCode.MONITORING_PILLAR_ERROR,
									StackTrace
									.getStackTraceToString(new Exception(
											errorMessage))).build()
											.build();

				} catch (NullPointerException e) {
					return MonitoringResponse.status(Status.NO_CONTENT)
							.build().build();
				}
			}
		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		String error = " { \"Error\"  :  \" Wrong Parameter\" }";
		return MonitoringResponse.status(Status.BAD_REQUEST, error).build()
				.build();
	}

	/**********************************
	 * DELETE HOST FROM PAAS PLATFORM
	 * @param adapterType
	 * @param hostMonitoringDeleteRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	public Response getMonitoringDeleteHost( String adapterType, HostMonitoringDeleteRequest hostMonitoringDeleteRequest)
			throws Exception {

		CreatedHostInServer resulthostsID = new CreatedHostInServer();
		String errorMessage;

		int v;
		try {
			@SuppressWarnings("unchecked")
			ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>> hosts = 
			(ArrayList<JSONRPCResponse<MonitoringVMCredentialsResponse>>) determineAdapt.getAdapter(adapterType).deleteMonitoredHost(
					envDeployConf.getMonitoringEnvironment(),
					hostMonitoringDeleteRequest.getUuid(),
					hostMonitoringDeleteRequest.getServiceId());

			for (v = 0; v < hosts.size();) {
				resulthostsID.setHostIDInMetrics(hosts.get(v).getResult()
						.getHostids());
				v++;
				resulthostsID.setHostIDinWatcher(hosts.get(v).getResult()
						.getHostids());
				try {
					hosts.get(v).getError().equals(null);
					errorMessage = hosts.get(v).getError().getMessage()
							+ hosts.get(v).getError().getData();
					return MonitoringResponse
							.status(Status.BAD_REQUEST,
									MonitoringErrorCode.MONITORING_PILLAR_ERROR,
									StackTrace
									.getStackTraceToString(new Exception(
											errorMessage))).build()
											.build();

				} catch (NullPointerException e) {
					return MonitoringResponse.status(Status.NO_CONTENT)
							.build().build();
				}
			}
		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (it.prisma.utils.web.ws.rest.apiclients.prisma.APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		String error = " { \"Error\"  :  \" Wrong Parameter\" }";
		return MonitoringResponse.status(Status.BAD_REQUEST, error).build()
				.build();
	}

	/***************************************************
	 * GET A SPECIFIC GROUP OF HOSTS
	 ***************************************************/
	/**
	 * In Prisma PaaS context there are category services such as DBaaS, AppaaS,
	 * etc. created by a specific user. Here the API consumer might be
	 * interested in having a picture of the general situation into a category
	 * Service group
	 * 
	 * @param testbed_id
	 * @param adapter
	 * @param service_category_id
	 * @return MonitoringWrappedResponsePaaS Object that wraps Zabbix APIs in
	 *         order to give needed information about a host, service category,
	 *         atomic services, metrics, triggers
	 * @throws MappingException
	 * @throws APIErrorException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws RestClientException
	 * @throws Exception
	 */
	@Override
	public Response getSpecificServiceCategory( String adapterType, String group) {

		try {
			wrappedPaas = determineAdapt.getAdapter(adapterType).getOverallPaaSMetrics(
					envDeployConf.getMonitoringEnvironment(), null, group,
					null, null, null, null, null, null,
					envConf.getZabbixServer_Metrics(), null // TimeRequestBody
					);
		} catch (MappingException e) {
			e.printStackTrace();
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, wrappedPaas).build()
				.build();
	}

	/*******************************************************************************
	 * GET A SPECIFIC HOST (and its carachteristics ) BY ITS NAME
	 ******************************************************************************/
	/**
	 * Method useful for having a SPECIFIC HOST info (and its characteristics )
	 * belonging to a SPECIFIC Category service regardless of the fact that it
	 * could be belonging to a group of host composing a Category service
	 * identified by the ID.
	 * 
	 * @param testbed_id
	 * @param adapter
	 * @param host_id
	 * @param service_category_id
	 * @return MonitoringWrappedResponsePaaS Object that wraps Zabbix APIs in
	 *         order to give needed information about a host, service category,
	 *         atomic services, metrics, triggers
	 * @throws MappingException
	 * @throws APIErrorException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws RestClientException
	 * @throws Exception
	 */

	@Override
	public Response getSpecificHostbyHostUuid( String adapterType, String hostName) {

		try {
			wrappedPaas = determineAdapt.getAdapter(adapterType).getOverallPaaSMetricsV2_4(
					envDeployConf.getMonitoringEnvironment(), hostName, 
					null,//group,
					null,//category, 
					null, null, null, null, null,
					envConf.getZabbixServer_Metrics(), null // TimeRequestBody
					);
			LOGMonit.info(wrappedPaas);
		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, wrappedPaas).build()
				.build();
	}

	/***************************
	 * SPECIFIC SERVICE BY TAG 
	 ***************************/
	/**
	 * A deployed service might be composed of two or more machines, so that
	 * it's needed to call those hosts belonging to the category by searching
	 * them by the serviceID attached to themselves at the creation stage. It
	 * retrieves all hosts belonging to a specific ATOMIC Service (properly
	 * tagged since the creation of it) created by the user.
	 * 
	 * @param testbeds_id
	 * @param host_id
	 * @param service_category_id
	 * @param atomic_service_id
	 * @param metrics_id
	 * @param triggers_id
	 * @return MonitoringWrappedResponsePaaS Object that wraps Zabbix APIs in
	 *         order to give needed information about a host, service category,
	 *         atomic services, metrics, triggers
	 * @throws MappingException
	 * @throws APIErrorException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws RestClientException
	 * @throws Exception
	 */

	@Override
	public Response getHostsByServiceId( String adapterType, String serviceId){
		{

			try {
				// determineAdapterType(adapter);
				wrappedPaas = determineAdapt.getAdapter(adapterType).getOverallPaaSMetricsV2_4(
						envDeployConf.getMonitoringEnvironment(), null, null,
						null, serviceId, null, null, null, null,
						envConf.getZabbixServer_Metrics(), null // TimeRequestBody
						);
				return MonitoringResponse.status(Status.OK, wrappedPaas).build()
						// .header("Content-Type", "application/javascript")
						// .header("Access-Control-Allow-Origin", "*")
						// .header("Access-Control-Allow-Methods",
						// "POST, GET, PUT, UPDATE, OPTIONS")
						// .header("Access-Control-Allow-Headers",
						// "Content-Type, Accept, X-Requested-With")
						.build();

			} catch (MappingException e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (APIErrorException e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (NoMappingModelFoundException e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (ServerErrorResponseException e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (RestClientException e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (NamingException e) {
				return MonitoringResponse
						.status(Status.NOT_FOUND,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			} catch (Exception e) {
				e.printStackTrace();
				LOGMonit.error(e, e);
				return MonitoringResponse
						.status(Status.BAD_REQUEST,
								MonitoringErrorCode.MONITORING_PILLAR_ERROR,
								StackTrace.getStackTraceToString(e)).build()
								.build();
			}
		}
	}

	/****************************************************************
	 * DEPRECATED - Get metrics belonging to a SPECIFIC ATOMIC SERVICE, TAG, HOST
	 *****************************************************************/
	/**
	 * Method useful for having a specific atomic service info (identified by
	 * its name) belonging to a specific host, category group.
	 * 
	 * @param testbeds_id
	 * @param host_id
	 * @param service_category_id
	 * @param atomic_service_id
	 * @param metrics_id
	 * @param triggers_id
	 * @return MonitoringWrappedResponsePaaS Object that wraps Zabbix APIs in
	 *         order to give needed information about a host, service category,
	 *         atomic services, metrics, triggers
	 * @throws MappingException
	 * @throws APIErrorException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws RestClientException
	 * @throws Exception
	 */

	@Path("/host/{host_id}" + "/groups/{group}"
			+ "/service-id/{service_id}" + "/atomic_services")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public// MonitoringWrappedResponsePaaSV2
	Response getAtomicService(
			@DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
			@PathParam("host_id") String host_id,
			@PathParam("group") String group,
			@PathParam("service_id") String service_id,
			@QueryParam("atomic_services") List<String> atomic_services) {

		try {
			wrappedPaas = determineAdapt.getAdapter(adapterType).getOverallPaaSMetrics(
					envDeployConf.getMonitoringEnvironment(), host_id, group, 
					null, service_id, atomic_services, null,
					null, null, envConf.getZabbixServer_Metrics(), null // TimeRequestBody
					// metrics_id,
					// triggers_id
					// graph
					);
		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, wrappedPaas).build()
				.build();
	}

	/*********************************
	 * SPECIFIC METRIC REQUESTED
	 ********************************/
	/**
	 * Method useful for having a specific metric info (identified by its name)
	 * belonging to a specific host, category group, atomic service which all
	 * are to be specified in the request.
	 * 
	 * @param testbeds_id
	 * @param adapter
	 * @param host_id
	 * @param service_category_id
	 * @param tag_service
	 * @param atomic_service_id
	 * @param metrics_id
	 * @return MonitoringWrappedResponsePaaS Object that wraps Zabbix APIs in
	 *         order to give needed information about a host, service category,
	 *         atomic services, metrics, triggers
	 * @throws MappingException
	 * @throws APIErrorException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws RestClientException
	 * @throws Exception
	 */

	@Override
	public Response getMetric(
			String adapterType, String host_id, List<String> metrics_id) {

		try {
			wrappedPaas = determineAdapt.getAdapter(adapterType).getOverallPaaSMetrics(
					envDeployConf.getMonitoringEnvironment(), host_id, null,
					null, null, null,
					metrics_id, null // triggers_id
					, null, envConf.getZabbixServer_Metrics(), null // TimeRequestBody

					);
		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, wrappedPaas).build()
				.build();
	}

	/*********************
	 * History without filter Time
	 * 
	 * @param adapter
	 * @param host_id
	 * @param service_category_id
	 * @param tag_service
	 * @param history
	 * @param server
	 * @param atomic_services
	 * @param metrics_id
	 * @return REST response listing the values and history
	 */

	@Override
	public Response getHistory( String adapterType, String host_id, String server, List<String> metrics_id) {

		String history = "true";
		try {
			wrappedPaas = determineAdapt.getAdapter(adapterType).getOverallPaaSMetrics(
					envDeployConf.getMonitoringEnvironment(), host_id, null,
					null, 
					null,//service_id, 
					null,//atomic_services,
					metrics_id, null // triggers_id
					, history, server, null // TimeRequestBody
					);
		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ResourceNotFoundException rnfe) {
			rnfe.printStackTrace();
			LOGMonit.error(rnfe, rnfe);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(rnfe)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, wrappedPaas).build()
				.build();
	}

	/*********************************
	 * HISTORY WATCHER BY FILTER TIME
	 ********************************/
	/**
	 * Method useful for having a specific metric info (identified by its name)
	 * belonging to a specific host, category group, atomic service which all
	 * are to be specified in the request.
	 * 
	 * @param testbeds_id
	 * @param adapter
	 * @param host_id
	 * @param service_category_id
	 * @param tag_service
	 * @param atomic_service_id
	 * @param metrics_id
	 * @return MonitoringWrappedResponsePaaS Object that wraps Zabbix APIs in
	 *         order to give needed information about a host, service category,
	 *         atomic services, metrics, triggers
	 * @throws MappingException
	 * @throws APIErrorException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws RestClientException
	 * @throws Exception
	 */

	@Override
	public Response getMetricWithTime( String adapterType, String host_id, String group,
			String service_id, String history_filtered, String server, List<String> atomic_services,
			List<String> metrics_id, String requestTime) {


		try {
			wrappedPaas = determineAdapt.getAdapter(adapterType).getOverallPaaSMetrics(
					envDeployConf.getMonitoringEnvironment(), host_id, group, 
					null, service_id, atomic_services,
					metrics_id, null // triggers_id
					, history_filtered, server, requestTime);
		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, wrappedPaas).build()
				.build();
	}

	/*********************************
	 * WRAPPING EVENTS IN ALL SERVERS
	 *********************************/

	/*********************************
	 * HOST's EVENTS NO FILTER TIME
	 ********************************/
	/**
	 * Method useful for having Events happening into the monitoring platform.
	 * They can be filtered by period of time as much as the history
	 * 
	 * @param testbeds_id
	 * @param adapter
	 * @param host_id
	 * @param service_category_id
	 * @param tag_service
	 * @param atomic_service_id
	 * @param metrics_id
	 * @return MonitoringWrappedResponsePaaS Object that wraps Zabbix APIs in
	 *         order to give needed information about a host, service category,
	 *         atomic services, metrics, triggers
	 * @throws MappingException
	 * @throws APIErrorException
	 * @throws NoMappingModelFoundException
	 * @throws ServerErrorResponseException
	 * @throws RestClientException
	 * @throws Exception
	 */

	@Override
	public Response getServerEvents( String adapterType,
			String host_id,
			String history_filtered,
			String server, String requestTime) {


		try {
			wrappedEvent = determineAdapt.getAdapter(adapterType).getOverallServerEvents(
					envDeployConf.getMonitoringEnvironment(), host_id, null,
					null, server, requestTime);

		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, wrappedEvent).build()
				.build();
	}

	/*********************************
	 * HOST's EVENTS WITH FILTER TIME
	 ********************************/
	/**
	 * Method useful for having Events happening into the monitoring platform.
	 * They can be filtered by period of time as much as the history
	 * 
	 * @param adapter
	 * @param host_id
	 * @param history_filtered
	 * @param server
	 * @param requestTime
	 * @return the wrapped Zabbix APIs describing
	 */

	@Override
	public Response getServerFilteredEvents( String adapterType,String host_id,
			String history_filtered, String server, String requestTime) {


		try {
			wrappedEvent = determineAdapt.getAdapter(adapterType).getOverallServerEvents(
					envDeployConf.getMonitoringEnvironment(), host_id, null,
					null, server, requestTime);

		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, wrappedEvent).build()
				.build();
	}

	/*****************
	 * DISABLE HOST
	 *****************/
	/**
	 * Method useful for having Events happening into the monitoring platform.
	 * They can be filtered by period of time as much as the history
	 * 
	 * @param adapter
	 * @param host_id
	 * @param history_filtered
	 * @param server
	 * @param requestTime
	 * @return the wrapped Zabbix APIs describing
	 */

	@Override
	public Response getDisablement(String adapterType, String hostName, String server, String update) {

		try {
			responseUpdate = determineAdapt.getAdapter(adapterType).getDisablingHost(
					envDeployConf.getMonitoringEnvironment(), hostName, null,
					null, server, update);

		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, responseUpdate).build()
				.build();
	}

	/*****************
	 * DISABLE METRIC
	 *****************/
	@Override
	public Response getDisablementMetric(String adapterType, String hostName, String metric_id, String server, String update) {


		try {
			responseUpdate = determineAdapt.getAdapter(adapterType).getDisablingHost(
					envDeployConf.getMonitoringEnvironment(), hostName,
					metric_id, null, server, update);

		} catch (MappingException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (APIErrorException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NoMappingModelFoundException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (ServerErrorResponseException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (RestClientException e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (NamingException e) {
			return MonitoringResponse
					.status(Status.NOT_FOUND,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		} catch (Exception e) {
			e.printStackTrace();
			LOGMonit.error(e, e);
			return MonitoringResponse
					.status(Status.BAD_REQUEST,
							MonitoringErrorCode.MONITORING_PILLAR_ERROR,
							StackTrace.getStackTraceToString(e)).build()
							.build();
		}
		return MonitoringResponse.status(Status.OK, responseUpdate).build()
				.build();
	}
}