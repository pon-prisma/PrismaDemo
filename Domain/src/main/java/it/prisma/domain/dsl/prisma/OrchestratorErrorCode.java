package it.prisma.domain.dsl.prisma;

/**
 * BizLib error codes.
 * 
 * @author l.biava
 * 
 */
public enum OrchestratorErrorCode implements ErrorCode {
	
	//IaaS Platform (Openstack) Error Codes
	ORC_IAAS_GENERIC_ERROR(100, "Generic IaaS Error"),
	
	//@formatter:off
	// MISC
	ORC_GENERIC_ERROR(950, "A generic error occurred"),
	ORC_LRT_NOT_FOUND(800, "Specified LRT not found", 404),
	ORC_ITEM_NOT_FOUND(904,	"Item not found", 404),
	ORC_ITEM_ALREADY_EXISTS(905, "Item already exists", 409),
	ORC_PLATFORM_STATUS_NOT_READY(906, "Platform status not ready"),
	ORC_PLATFORM_QUOTA_EXCEEDED(907, "Platform quota exceeded"),
	
	ORC_BAD_REQUEST(907, "Bad request",400),
	
	// WF DEPLOY
	ORC_WF_DEPLOY_GENERIC_ERROR(999, "Problem during deployment"),
	ORC_WF_DEPLOY_MONITORING_ERROR(	998, "Problem connecting to monitoring platform"),	
	ORC_WF_DEPLOY_TIMEOUT(	997, "Timeout occurred during deployment operation"),
	ORC_WF_DEPLOY_DNS_ERROR(	996, "Problem registering Domain Name"),
	// WF DEPLOY TO PAAS
	ORC_WF_DEPLOY_PAAS_DEPLOYING_ERROR(989, "Problem deploying to PaaS"),
	ORC_WF_DEPLOY_PAAS_DEPLOYING_CUSTOM_RECIPE_ERROR(987, "Problem customizing Cloudify recipe"),
	ORC_WF_DEPLOY_PAAS_RECIPE_NOT_FOUND(979, "Problem deploying to PaaS: Recipe not found"),
	ORC_WF_DEPLOY_PAAS_CFY_APP_NAME_ALREAD_EXISTS_ERROR(988,"Problem deploying to PaaS: Cloudify application name already exists"),
	// WF APPaaS - DEPLOY
	ORC_WF_DEPLOY_APPAAS_GENERIC_ERROR(700, "APPaaS deploy process error"),
	ORC_WF_DEPLOY_APPAAS_APP_NAME_ALREADY_EXISTS(701, "APPaaS name already exists", 409),
	ORC_WF_DEPLOY_APPAAS_ENV_NAME_ALREADY_EXISTS(702, "APPaaSEnvironment name already exists", 409),
	ORC_WF_DEPLOY_APPAAS_ENV_CREATION_ERROR(703, "APPaaSEnvironment cannot be created"),

	// WF UNDEPLOY
	ORC_WF_UNDEPLOY_GENERIC_ERROR(940, "Problem during undeployment"),
	ORC_WF_UNDEPLOY_MONITORING_ERROR(941, "Problem detaching from monitoring platform"),	
	ORC_WF_UNDEPLOY_TIMEOUT(942, "Timeout occurred during undeployment operation"),
	ORC_WF_UNDEPLOY_DNS_ERROR(943, "Problem removing Domain Name"),
	
	// WF UNDEPLOY PAAS
	ORC_WF_DEPLOY_PAAS_UNDEPLOYING_ERROR(944, "Problem undeploying PaaS"),
	
	ORC_ILLEGAL_SERVICE_STATE(200001, "Illegal service state"),

	// WF IAAS
	ORC_WF_IAAS_GENERIC_ERROR(1000, "IaaS generic error"),
	ORC_WF_IAAS_INVALID_DATA(1001, "IaaS input invalid data",400),
	ORC_WF_IAAS_RESOURCE_ALREADY_EXIXTS(1001, "IaaS resource already exists", 409),

	// Useful when on operation (start stop restart delete) fails
	ORC_SERVICE_ILLEGAL_STATE(1101, "Illegal service state"),
	ORC_SERVICE_OPERATION_ERROR(1102, "Error performing the required operation"),
	
	
	// PLATFORM CONFIGURATION
	ORC_CONFIGURATION_KEY_NOT_FOUND(600, "Configuration Key not found", 404),

	// DBaaS
	ORC_PAAS_SVC_ILLEGAL_STATE(1101, "Illegal service state"),
	
	// ACCOUNTING
	ORC_AUTHENTICATION_EXCEPTION(2401, "Authentication exception",401),
	ORC_INVALID_TOKEN(2402, "Invalid token",401),
	ORC_TOKEN_EXPIRED(2403, "Token expired",401),
	ORC_AUTHENTICATION_REQUIRED(2404, "Authentication required", 401),
	ORC_NOT_AUTHORIZED(2405, "Not authorized", 403),
	ORC_USER_ACCOUNT_NOT_FOUND(2406, "User account not found",404),
	
	// SMSaaS
	SMS_GENERIC_EXCEPTION(1200, "SMS error"),
	
	// Monitoring API
	MONITORING_EXCEPTION(5000, "Generic Exception");

	//@formatter:on

	private final int code;
	private final String description;
	private final int httpStatusCode;

	private OrchestratorErrorCode(int code, String description) {
		this.code = code;
		this.description = description;
		this.httpStatusCode=500;
	}
	
	private OrchestratorErrorCode(int code, String description, int httpStatusCode) {

		this.code = code;
		this.description = description;
		this.httpStatusCode=httpStatusCode;
	}

	public String getDescription() {
		return description;
	}

	public int getCode() {
		return code;
	}
	
	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public String getName() {
		return name();
	}

	@Override
	public String toString() {
		return code + ": " + description;
	}

	public OrchestratorErrorCode lookupFromCode(int errorCode) {
		for (OrchestratorErrorCode e : values()) {
			if (e.code == errorCode) {
				return e;
			}
		}
		return null;
	}

	public OrchestratorErrorCode lookupFromName(String errorName) {
		for (OrchestratorErrorCode e : values()) {
			if (errorName.equals(e.name())) {
				return e;
			}
		}
		return null;
	}
}