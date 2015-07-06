package it.prisma.domain.dsl.paas.services.mqaas;

import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType;
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
@JsonPropertyOrder({ "account", "environment", "queue" })
public class MQaaSDeployRequest implements GenericPaaSServiceDeployRequest<MQaaSRepresentation> {

	private static final long serialVersionUID = -6699532613602153950L;

	private Account account;
	private ServiceDetails serviceDetails;
	@JsonProperty("mqaasDetails")
	private QueueDetails mqaasDetails;

	@Override
	public PaaSServiceType getPaaSServiceType() {
		return PaaSServiceType.MQaaS;
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

	@JsonProperty("mqaasDetails")
	public QueueDetails getMQaasDetails() {
		return mqaasDetails;
	}

	@JsonProperty("mqaasDetails")
	public void setMQaasDetails(QueueDetails queueDetails) {
		this.mqaasDetails = queueDetails;
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