package it.prisma.domain.dsl.paas.services.generic.request;

import it.prisma.domain.dsl.paas.services.GenericPaaSServiceRepresentation;
import it.prisma.domain.dsl.paas.services.PaaSServiceRepresentation;

import java.io.Serializable;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "account", "serviceDetails" })
public interface GenericPaaSServiceDeployRequest<SERVICE_REPRESENTATION_TYPE extends GenericPaaSServiceRepresentation>
		extends Serializable {

	@JsonIgnore
	public PaaSServiceRepresentation.PaaSServiceType getPaaSServiceType();

	@JsonProperty("account")
	public Account getAccount();

	@JsonProperty("account")
	public void setAccount(Account account);

	@JsonProperty("serviceDetails")
	public ServiceDetails getServiceDetails();

	@JsonProperty("serviceDetails")
	public void setServiceDetails(ServiceDetails serviceDetail);

	/*
	 * account userId workgroupId
	 * 
	 * serviceDetails name description domainName notificationEmail
	 * loadBalancingInstances publicVisibility
	 * 
	 * secureConnection //OPTIONAL certificatePath
	 * 
	 * environment iaaSFlavor qos availabilityZone
	 * 
	 * dbaasDetails productType //DBMS Type productVersion diskSize
	 * diskEncryptionEnabled
	 * 
	 * backup //OPTIONAL interval
	 * 
	 * verticalScaling //OPTIONAL maxFlavor maxDiskSize
	 * 
	 * rootPassword
	 * 
	 * addUser //OPTIONAL username password
	 */

}
