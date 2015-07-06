package it.prisma.domain.dsl.accounting.workgroups;

import it.prisma.domain.dsl.accounting.users.UserRepresentation;

import java.util.Collection;
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
@JsonPropertyOrder({ "workgroupId", "label", "description", "approved",
		"approvedByUserAccountId", "createdByUserAccountId",
		"applicantsNumber", "approvedMembersNumber", "totalMembersNumber", "referentUsers"})
public class WorkgroupRepresentation implements java.io.Serializable {

	@JsonProperty("workgroupId")
	private Long workgroupId;
	@JsonProperty("label")
	private String label;
	@JsonProperty("description")
	private String description;
	@JsonProperty("approved")
	private Boolean approved;
	@JsonProperty("approvedByUserAccountId")
	private Long approvedByUserAccountId;
	@JsonProperty("createdByUserAccountId")
	private Long createdByUserAccountId;
	@JsonProperty("applicantsNumber")
	private Long applicantsNumber;
	@JsonProperty("approvedMembersNumber")
	private Long approvedMembersNumber;
	@JsonProperty("totalMembersNumber")
	private Long totalMembersNumber;
	@JsonProperty("referentUsers")
	private Collection<UserRepresentation> referentUsers;
	
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
	 * @return The label
	 */
	@JsonProperty("label")
	public String getLabel() {
		return label;
	}

	/**
	 * 
	 * @param label
	 *            The label
	 */
	@JsonProperty("label")
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 
	 * @return The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
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

	/**
	 * 
	 * @return The createdByUserAccountId
	 */
	@JsonProperty("createdByUserAccountId")
	public Long getCreatedByUserAccountId() {
		return createdByUserAccountId;
	}

	/**
	 * 
	 * @param createdByUserAccountId
	 *            The createdByUserAccountId
	 */
	@JsonProperty("createdByUserAccountId")
	public void setCreatedByUserAccountId(Long createdByUserAccountId) {
		this.createdByUserAccountId = createdByUserAccountId;
	}

	/**
	 * 
	 * @return The applicantsNumber
	 */
	@JsonProperty("applicantsNumber")
	public Long getApplicantsNumber() {
		return applicantsNumber;
	}

	/**
	 * 
	 * @param applicantsNumber
	 *            The applicantsNumber
	 */
	@JsonProperty("applicantsNumber")
	public void setApplicantsNumber(Long applicantsNumber) {
		this.applicantsNumber = applicantsNumber;
	}

	/**
	 * 
	 * @return The approvedMembersNumber
	 */
	@JsonProperty("approvedMembersNumber")
	public Long getApprovedMembersNumber() {
		return approvedMembersNumber;
	}

	/**
	 * 
	 * @param approvedMembersNumber
	 *            The approvedMembersNumber
	 */
	@JsonProperty("approvedMembersNumber")
	public void setApprovedMembersNumber(Long approvedMembersNumber) {
		this.approvedMembersNumber = approvedMembersNumber;
	}

	/**
	 * 
	 * @return The totalMembersNumber
	 */
	@JsonProperty("totalMembersNumber")
	public Long getTotalMembersNumber() {
		return totalMembersNumber;
	}

	/**
	 * 
	 * @param totalMembersNumber
	 *            The totalMembersNumber
	 */
	@JsonProperty("totalMembersNumber")
	public void setTotalMembersNumber(Long totalMembersNumber) {
		this.totalMembersNumber = totalMembersNumber;
	}
	
	/**
	 * 
	 * @return The referent users list
	 */
	@JsonProperty("referentUsers")
	public Collection<UserRepresentation> getReferentUsers() {
		return referentUsers;
	}

	/**
	 * 
	 * @param referentUsers
	 *            The referent users list
	 */
	@JsonProperty("referentUsers")
	public void setReferentUsers(Collection<UserRepresentation> referentUsers) {
		this.referentUsers = referentUsers;
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
		return new HashCodeBuilder().append(workgroupId).append(label)
				.append(description).append(approved)
				.append(approvedByUserAccountId).append(createdByUserAccountId)
				.append(applicantsNumber).append(approvedMembersNumber)
				.append(totalMembersNumber).append(additionalProperties)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof WorkgroupRepresentation) == false) {
			return false;
		}
		WorkgroupRepresentation rhs = ((WorkgroupRepresentation) other);
		return new EqualsBuilder().append(workgroupId, rhs.workgroupId)
				.append(label, rhs.label).append(description, rhs.description)
				.append(approved, rhs.approved)
				.append(approvedByUserAccountId, rhs.approvedByUserAccountId)
				.append(createdByUserAccountId, rhs.createdByUserAccountId)
				.append(applicantsNumber, rhs.applicantsNumber)
				.append(approvedMembersNumber, rhs.approvedMembersNumber)
				.append(totalMembersNumber, rhs.totalMembersNumber)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}