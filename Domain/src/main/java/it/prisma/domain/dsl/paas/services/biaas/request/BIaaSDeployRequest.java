package it.prisma.domain.dsl.paas.services.biaas.request;

import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation;
import it.prisma.domain.dsl.paas.services.generic.request.Account;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.domain.dsl.paas.services.generic.request.ServiceDetails;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "account", "environment", "biaas" })
public class BIaaSDeployRequest implements GenericPaaSServiceDeployRequest<BIaaSRepresentation> {

	private static final long serialVersionUID = -6699532613602153950L;

	private Account account;
	private ServiceDetails serviceDetails;
	@JsonProperty("biaasDetails")
	private BIaaSDetails biaasDetails;

	@Override
	public PaaSServiceType getPaaSServiceType() {
		return PaaSServiceType.BIaaS;
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

	@JsonProperty("biaasDetails")
	public BIaaSDetails getBIaasDetails() {
		return biaasDetails;
	}

	@JsonProperty("biaasDetails")
	public void setBIaasDetails(BIaaSDetails biaasDetails) {
		this.biaasDetails = biaasDetails;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

}