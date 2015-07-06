package it.prisma.domain.dsl.iaas.vmaas.request;

import it.prisma.domain.dsl.iaas.vmaas.VMRepresentation;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation.PaaSServiceType;
import it.prisma.domain.dsl.paas.services.generic.request.Account;
import it.prisma.domain.dsl.paas.services.generic.request.GenericPaaSServiceDeployRequest;
import it.prisma.domain.dsl.paas.services.generic.request.ServiceDetails;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "account", "serviceDetails", "vmDetails" })
public class VMDeployRequest implements GenericPaaSServiceDeployRequest<VMRepresentation> {

	
	private static final long serialVersionUID = 5543407260029304641L;
	
	private Account account;
	private ServiceDetails serviceDetails;
	
	@JsonProperty("vmDetails")
	private VmDetails vmDetails;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@Override
	public PaaSServiceType getPaaSServiceType() {
		return PaaSServiceType.VMaaS;
	}
	
	/**
	 * 
	 * @return The account
	 */
	@JsonProperty("account")
	public Account getAccount() {
		return account;
	}

	/**
	 * 
	 * @param account
	 *            The account
	 */
	@JsonProperty("account")
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * 
	 * @return The serviceDetails
	 */
	@JsonProperty("serviceDetails")
	public ServiceDetails getServiceDetails() {
		return serviceDetails;
	}

	/**
	 * 
	 * @param serviceDetails
	 *            The serviceDetails
	 */
	@JsonProperty("serviceDetails")
	public void setServiceDetails(ServiceDetails serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	/**
	 * 
	 * @return The vmDetails
	 */
	@JsonProperty("vmDetails")
	public VmDetails getVmDetails() {
		return vmDetails;
	}

	/**
	 * 
	 * @param vmDetails
	 *            The vmDetails
	 */
	@JsonProperty("vmDetails")
	public void setVmDetails(VmDetails vmDetails) {
		this.vmDetails = vmDetails;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(account).append(serviceDetails)
				.append(vmDetails).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof VMDeployRequest) == false) {
			return false;
		}
		VMDeployRequest rhs = ((VMDeployRequest) other);
		return new EqualsBuilder().append(account, rhs.account)
				.append(serviceDetails, rhs.serviceDetails)
				.append(vmDetails, rhs.vmDetails)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}