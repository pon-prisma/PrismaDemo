package it.prisma.domain.dsl.iaas.openstack.network.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "status", "subnets", "name", "provider:physical_network",
		"admin_state_up", "tenant_id", "provider:network_type",
		"router:external", "shared", "id", "provider:segmentation_id" })
public class Network {

	@JsonProperty("status")
	private String status;
	@JsonProperty("subnets")
	private List<String> subnets = new ArrayList<String>();
	@JsonProperty("name")
	private String name;
	@JsonProperty("provider:physical_network")
	private Object providerPhysicalNetwork;
	@JsonProperty("admin_state_up")
	private Boolean adminStateUp;
	@JsonProperty("tenant_id")
	private String tenantId;
	@JsonProperty("provider:network_type")
	private String providerNetworkType;
	@JsonProperty("router:external")
	private Boolean routerExternal;
	@JsonProperty("shared")
	private Boolean shared;
	@JsonProperty("id")
	private String id;
	@JsonProperty("provider:segmentation_id")
	private Object providerSegmentationId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 *            The status
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return The subnets
	 */
	@JsonProperty("subnets")
	public List<String> getSubnets() {
		return subnets;
	}

	/**
	 * 
	 * @param subnets
	 *            The subnets
	 */
	@JsonProperty("subnets")
	public void setSubnets(List<String> subnets) {
		this.subnets = subnets;
	}

	/**
	 * 
	 * @return The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The providerPhysicalNetwork
	 */
	@JsonProperty("provider:physical_network")
	public Object getProviderPhysicalNetwork() {
		return providerPhysicalNetwork;
	}

	/**
	 * 
	 * @param providerPhysicalNetwork
	 *            The provider:physical_network
	 */
	@JsonProperty("provider:physical_network")
	public void setProviderPhysicalNetwork(Object providerPhysicalNetwork) {
		this.providerPhysicalNetwork = providerPhysicalNetwork;
	}

	/**
	 * 
	 * @return The adminStateUp
	 */
	@JsonProperty("admin_state_up")
	public Boolean getAdminStateUp() {
		return adminStateUp;
	}

	/**
	 * 
	 * @param adminStateUp
	 *            The admin_state_up
	 */
	@JsonProperty("admin_state_up")
	public void setAdminStateUp(Boolean adminStateUp) {
		this.adminStateUp = adminStateUp;
	}

	/**
	 * 
	 * @return The tenantId
	 */
	@JsonProperty("tenant_id")
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * 
	 * @param tenantId
	 *            The tenant_id
	 */
	@JsonProperty("tenant_id")
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * 
	 * @return The providerNetworkType
	 */
	@JsonProperty("provider:network_type")
	public String getProviderNetworkType() {
		return providerNetworkType;
	}

	/**
	 * 
	 * @param providerNetworkType
	 *            The provider:network_type
	 */
	@JsonProperty("provider:network_type")
	public void setProviderNetworkType(String providerNetworkType) {
		this.providerNetworkType = providerNetworkType;
	}

	/**
	 * 
	 * @return The routerExternal
	 */
	@JsonProperty("router:external")
	public Boolean getRouterExternal() {
		return routerExternal;
	}

	/**
	 * 
	 * @param routerExternal
	 *            The router:external
	 */
	@JsonProperty("router:external")
	public void setRouterExternal(Boolean routerExternal) {
		this.routerExternal = routerExternal;
	}

	/**
	 * 
	 * @return The shared
	 */
	@JsonProperty("shared")
	public Boolean getShared() {
		return shared;
	}

	/**
	 * 
	 * @param shared
	 *            The shared
	 */
	@JsonProperty("shared")
	public void setShared(Boolean shared) {
		this.shared = shared;
	}

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The providerSegmentationId
	 */
	@JsonProperty("provider:segmentation_id")
	public Object getProviderSegmentationId() {
		return providerSegmentationId;
	}

	/**
	 * 
	 * @param providerSegmentationId
	 *            The provider:segmentation_id
	 */
	@JsonProperty("provider:segmentation_id")
	public void setProviderSegmentationId(Object providerSegmentationId) {
		this.providerSegmentationId = providerSegmentationId;
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
		return new HashCodeBuilder().append(status).append(subnets)
				.append(name).append(providerPhysicalNetwork)
				.append(adminStateUp).append(tenantId)
				.append(providerNetworkType).append(routerExternal)
				.append(shared).append(id).append(providerSegmentationId)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Network) == false) {
			return false;
		}
		Network rhs = ((Network) other);
		return new EqualsBuilder().append(status, rhs.status)
				.append(subnets, rhs.subnets).append(name, rhs.name)
				.append(providerPhysicalNetwork, rhs.providerPhysicalNetwork)
				.append(adminStateUp, rhs.adminStateUp)
				.append(tenantId, rhs.tenantId)
				.append(providerNetworkType, rhs.providerNetworkType)
				.append(routerExternal, rhs.routerExternal)
				.append(shared, rhs.shared).append(id, rhs.id)
				.append(providerSegmentationId, rhs.providerSegmentationId)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}