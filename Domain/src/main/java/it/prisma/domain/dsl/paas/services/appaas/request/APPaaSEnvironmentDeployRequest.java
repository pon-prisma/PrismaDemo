package it.prisma.domain.dsl.paas.services.appaas.request;

import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSEnvironmentRepresentation;
import it.prisma.domain.dsl.paas.services.generic.request.Account;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.domain.dsl.paas.services.generic.request.ServiceDetails;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Reply
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "account", "serviceDetails", "envDetails" })
public class APPaaSEnvironmentDeployRequest implements
		GenericPaaSServiceDeployRequest<APPaaSEnvironmentRepresentation> {

	private static final long serialVersionUID = 4377419958022624682L;

	private Account account;
	private ServiceDetails serviceDetails;

	@JsonProperty("environmentDetails")
	private EnvironmentDetails environmentDetails;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@Override
	public PaaSServiceType getPaaSServiceType() {
		return PaaSServiceType.APPaaSEnvironment;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@JsonProperty("serviceDetails")
	public ServiceDetails getServiceDetails() {
		return serviceDetails;
	}

	@JsonProperty("serviceDetails")
	public void setServiceDetails(ServiceDetails serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	public EnvironmentDetails getEnvironmentDetails() {
		return environmentDetails;
	}

	public void setEnvironmentDetails(EnvironmentDetails environmentDetails) {
		this.environmentDetails = environmentDetails;
	}

}
