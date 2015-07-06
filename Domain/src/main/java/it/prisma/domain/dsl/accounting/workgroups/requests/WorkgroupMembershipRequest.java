package it.prisma.domain.dsl.accounting.workgroups.requests;

import java.util.HashMap;
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
@JsonPropertyOrder({ "workgroupId", "applicantUserAccountId",
		"approvedByUserAccountId", "workgroupPrivilegeName" })
public class WorkgroupMembershipRequest {

	@JsonProperty("workgroupId")
	private Long workgroupId;
	@JsonProperty("applicantUserAccountId")
	private Long applicantUserAccountId;
	@JsonProperty("approvedByUserAccountId")
	private Long approvedByUserAccountId;
	@JsonProperty("workgroupPrivilegeName")
	private String workgroupPrivilegeName;
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
	 * @return The applicantUserAccountId
	 */
	@JsonProperty("applicantUserAccountId")
	public Long getApplicantUserAccountId() {
		return applicantUserAccountId;
	}

	/**
	 * 
	 * @param applicantUserAccountId
	 *            The applicantUserAccountId
	 */
	@JsonProperty("applicantUserAccountId")
	public void setApplicantUserAccountId(Long applicantUserAccountId) {
		this.applicantUserAccountId = applicantUserAccountId;
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
				.append(applicantUserAccountId).append(approvedByUserAccountId)
				.append(workgroupPrivilegeName).append(additionalProperties)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof WorkgroupMembershipRequest) == false) {
			return false;
		}
		WorkgroupMembershipRequest rhs = ((WorkgroupMembershipRequest) other);
		return new EqualsBuilder().append(workgroupId, rhs.workgroupId)
				.append(applicantUserAccountId, rhs.applicantUserAccountId)
				.append(approvedByUserAccountId, rhs.approvedByUserAccountId)
				.append(workgroupPrivilegeName, rhs.workgroupPrivilegeName)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}