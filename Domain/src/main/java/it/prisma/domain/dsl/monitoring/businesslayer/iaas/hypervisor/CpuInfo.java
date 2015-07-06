package it.prisma.domain.dsl.monitoring.businesslayer.iaas.hypervisor;

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
@JsonPropertyOrder({
	"vendor",
	"model",
	"arch",
	"features",
	"topology"
})
public class CpuInfo {

	@JsonProperty("vendor")
	private String vendor;
	@JsonProperty("model")
	private String model;
	@JsonProperty("arch")
	private String arch;
	@JsonProperty("features")
	private List<String> features = new ArrayList<String>();
	@JsonProperty("topology")
	private Topology topology;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The vendor
	 */
	@JsonProperty("vendor")
	public String getVendor() {
		return vendor;
	}

	/**
	 * 
	 * @param vendor
	 * The vendor
	 */
	@JsonProperty("vendor")
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * 
	 * @return
	 * The model
	 */
	@JsonProperty("model")
	public String getModel() {
		return model;
	}

	/**
	 * 
	 * @param model
	 * The model
	 */
	@JsonProperty("model")
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * 
	 * @return
	 * The arch
	 */
	@JsonProperty("arch")
	public String getArch() {
		return arch;
	}

	/**
	 * 
	 * @param arch
	 * The arch
	 */
	@JsonProperty("arch")
	public void setArch(String arch) {
		this.arch = arch;
	}

	/**
	 * 
	 * @return
	 * The features
	 */
	@JsonProperty("features")
	public List<String> getFeatures() {
		return features;
	}

	/**
	 * 
	 * @param features
	 * The features
	 */
	@JsonProperty("features")
	public void setFeatures(List<String> features) {
		this.features = features;
	}

	/**
	 * 
	 * @return
	 * The topology
	 */
	@JsonProperty("topology")
	public Topology getTopology() {
		return topology;
	}

	/**
	 * 
	 * @param topology
	 * The topology
	 */
	@JsonProperty("topology")
	public void setTopology(Topology topology) {
		this.topology = topology;
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
		return new HashCodeBuilder().append(vendor).append(model).append(arch).append(features).append(topology).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof CpuInfo) == false) {
			return false;
		}
		CpuInfo rhs = ((CpuInfo) other);
		return new EqualsBuilder().append(vendor, rhs.vendor).append(model, rhs.model).append(arch, rhs.arch).append(features, rhs.features).append(topology, rhs.topology).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}