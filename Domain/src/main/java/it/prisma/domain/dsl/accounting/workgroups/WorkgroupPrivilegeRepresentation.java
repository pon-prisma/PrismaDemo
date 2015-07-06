package it.prisma.domain.dsl.accounting.workgroups;

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
@JsonPropertyOrder({ "workgroupPrivilegeId", "workgroupPrivilegeName",
		"workgroupPrivilegeDescription" })
public class WorkgroupPrivilegeRepresentation implements java.io.Serializable {

	@JsonProperty("workgroupPrivilegeId")
	private Long workgroupPrivilegeId;
	@JsonProperty("workgroupPrivilegeName")
	private String workgroupPrivilegeName;
	@JsonProperty("workgroupPrivilegeDescription")
	private String workgroupPrivilegeDescription;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The workgroupPrivilegeId
	 */
	@JsonProperty("workgroupPrivilegeId")
	public Long getWorkgroupPrivilegeId() {
		return workgroupPrivilegeId;
	}

	/**
	 * 
	 * @param workgroupPrivilegeId
	 *            The workgroupPrivilegeId
	 */
	@JsonProperty("workgroupPrivilegeId")
	public void setWorkgroupPrivilegeId(Long workgroupPrivilegeId) {
		this.workgroupPrivilegeId = workgroupPrivilegeId;
	}

	/**
	 * 
	 * @return The workgroupPrivilegeName
	 */
	@JsonProperty("workgroupPrivilegeName")
	public String getWorkgroupPrivilegeName() {
		return workgroupPrivilegeName;
	}

	/**
	 * 
	 * @param workgroupPrivilegeName
	 *            The workgroupPrivilegeName
	 */
	@JsonProperty("workgroupPrivilegeName")
	public void setWorkgroupPrivilegeName(String workgroupPrivilegeName) {
		this.workgroupPrivilegeName = workgroupPrivilegeName;
	}

	/**
	 * 
	 * @return The workgroupPrivilegeDescription
	 */
	@JsonProperty("workgroupPrivilegeDescription")
	public String getWorkgroupPrivilegeDescription() {
		return workgroupPrivilegeDescription;
	}

	/**
	 * 
	 * @param workgroupPrivilegeDescription
	 *            The workgroupPrivilegeDescription
	 */
	@JsonProperty("workgroupPrivilegeDescription")
	public void setWorkgroupPrivilegeDescription(
			String workgroupPrivilegeDescription) {
		this.workgroupPrivilegeDescription = workgroupPrivilegeDescription;
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
		return new HashCodeBuilder().append(workgroupPrivilegeId)
				.append(workgroupPrivilegeName)
				.append(workgroupPrivilegeDescription)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof WorkgroupPrivilegeRepresentation) == false) {
			return false;
		}
		WorkgroupPrivilegeRepresentation rhs = ((WorkgroupPrivilegeRepresentation) other);
		return new EqualsBuilder()
				.append(workgroupPrivilegeId, rhs.workgroupPrivilegeId)
				.append(workgroupPrivilegeName, rhs.workgroupPrivilegeName)
				.append(workgroupPrivilegeDescription,
						rhs.workgroupPrivilegeDescription)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}