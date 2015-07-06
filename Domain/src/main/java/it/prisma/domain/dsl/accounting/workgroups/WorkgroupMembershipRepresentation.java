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
@JsonPropertyOrder({ "workgroupId", "memberUserAccountId",
		"workgroupPrivilege", "approved", "approvedByUserAccountId" })
public class WorkgroupMembershipRepresentation implements java.io.Serializable {

	@JsonProperty("workgroupId")
	private Long workgroupId;
	@JsonProperty("memberUserAccountId")
	private Long memberUserAccountId;
	@JsonProperty("workgroupPrivilege")
	private WorkgroupPrivilegeRepresentation workgroupPrivilege;
	@JsonProperty("approved")
	private Boolean approved;
	@JsonProperty("approvedByUserAccountId")
	private Long approvedByUserAccountId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The workgroupId
	 */
	@JsonProperty("workgroupId")
	public Long getWorkgroupId() {
		return workgroupId;
	}

	/**
	 * 
	 * @param workgroupId
	 *            The workgroupId
	 */
	@JsonProperty("workgroupId")
	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	/**
	 * 
	 * @return The memberUserAccountId
	 */
	@JsonProperty("memberUserAccountId")
	public Long getMemberUserAccountId() {
		return memberUserAccountId;
	}

	/**
	 * 
	 * @param memberUserAccountId
	 *            The memberUserAccountId
	 */
	@JsonProperty("memberUserAccountId")
	public void setMemberUserAccountId(Long memberUserAccountId) {
		this.memberUserAccountId = memberUserAccountId;
	}

	/**
	 * 
	 * @return The workgroupPrivilege
	 */
	@JsonProperty("workgroupPrivilege")
	public WorkgroupPrivilegeRepresentation getWorkgroupPrivilege() {
		return workgroupPrivilege;
	}

	/**
	 * 
	 * @param workgroupPrivilege
	 *            The workgroupPrivilege
	 */
	@JsonProperty("workgroupPrivilege")
	public void setWorkgroupPrivilege(
			WorkgroupPrivilegeRepresentation workgroupPrivilege) {
		this.workgroupPrivilege = workgroupPrivilege;
	}

	/**
	 * 
	 * @return The approved
	 */
	@JsonProperty("approved")
	public Boolean getApproved() {
		return approved;
	}

	/**
	 * 
	 * @param approved
	 *            The approved
	 */
	@JsonProperty("approved")
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	/**
	 * 
	 * @return The approvedByUserAccountId
	 */
	@JsonProperty("approvedByUserAccountId")
	public Long getApprovedByUserAccountId() {
		return approvedByUserAccountId;
	}

	/**
	 * 
	 * @param approvedByUserAccountId
	 *            The approvedByUserAccountId
	 */
	@JsonProperty("approvedByUserAccountId")
	public void setApprovedByUserAccountId(Long approvedByUserAccountId) {
		this.approvedByUserAccountId = approvedByUserAccountId;
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
		return new HashCodeBuilder().append(workgroupId)
				.append(memberUserAccountId).append(workgroupPrivilege)
				.append(approved).append(approvedByUserAccountId)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof WorkgroupMembershipRepresentation) == false) {
			return false;
		}
		WorkgroupMembershipRepresentation rhs = ((WorkgroupMembershipRepresentation) other);
		return new EqualsBuilder().append(workgroupId, rhs.workgroupId)
				.append(memberUserAccountId, rhs.memberUserAccountId)
				.append(workgroupPrivilege, rhs.workgroupPrivilege)
				.append(approved, rhs.approved)
				.append(approvedByUserAccountId, rhs.approvedByUserAccountId)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}
