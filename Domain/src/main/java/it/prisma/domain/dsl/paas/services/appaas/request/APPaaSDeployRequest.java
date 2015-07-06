package it.prisma.domain.dsl.paas.services.appaas.request;

import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType;
import it.prisma.domain.dsl.paas.services.appaas.response.APPaaSRepresentation;
import it.prisma.domain.dsl.paas.services.generic.request.Account;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.domain.dsl.paas.services.generic.request.ServiceDetails;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "userId", "tenantId", "applicationParams",
		"environmentParams" })
public class APPaaSDeployRequest implements GenericPaaSServiceDeployRequest<APPaaSRepresentation> {

	private static final long serialVersionUID = 4377419958022624682L;

	private Account account;
	private ServiceDetails serviceDetails;

	@JsonProperty("environmentDetails")
	private EnvironmentDetails environmentDetails;

	@Override
	public PaaSServiceType getPaaSServiceType() {
		return PaaSServiceType.APPaaS;
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

}
