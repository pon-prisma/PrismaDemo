package it.prisma.domain.dsl.accounting.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@JsonPropertyOrder({ "userAccountId", "userProfileId", "identityProviderId",
		"nameIdOnIdentityProvider", "email", "suspended", "suspendedAt",
		"enabled", "enabledAt", "roles", "firstName", "middleName", "lastName",
		"employer", "emailRef", "personalPhone", "workPhone", "nationality",
		"taxcode", "avatar", "createdAt", "modifiedAt" })
public class UserRepresentation implements java.io.Serializable {
	
	@JsonProperty("userAccountId")
	private Long userAccountId;
	@JsonProperty("userProfileId")
	private Long userProfileId;
	@JsonProperty("identityProviderId")
	private Long identityProviderId;
	@JsonProperty("nameIdOnIdentityProvider")
	private String nameIdOnIdentityProvider;
	@JsonProperty("email")
	private String email;
	@JsonProperty("suspended")
	private Boolean suspended;
	@JsonProperty("suspendedAt")
	private String suspendedAt;
	@JsonProperty("enabled")
	private Boolean enabled;
	@JsonProperty("enabledAt")
	private String enabledAt;
	@JsonProperty("roles")
	private List<RoleRepresentation> roles = new ArrayList<RoleRepresentation>();
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("middleName")
	private String middleName;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("employer")
	private String employer;
	@JsonProperty("emailRef")
	private String emailRef;
	@JsonProperty("personalPhone")
	private String personalPhone;
	@JsonProperty("workPhone")
	private String workPhone;
	@JsonProperty("nationality")
	private String nationality;
	@JsonProperty("taxcode")
	private String taxcode;
	@JsonProperty("avatar")
	private String avatar;
	@JsonProperty("createdAt")
	private String createdAt;
	@JsonProperty("modifiedAt")
	private String modifiedAt;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("userAccountId")
	public Long getUserAccountId() {
		return userAccountId;
	}

	@JsonProperty("userAccountId")
	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	@JsonProperty("userProfileId")
	public Long getUserProfileId() {
		return userProfileId;
	}

	@JsonProperty("userProfileId")
	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}
	
	@JsonProperty("identityProviderId")
	public Long getIdentityProviderId() {
		return identityProviderId;
	}

	@JsonProperty("identityProviderId")
	public void setIdentityProviderId(Long identityProviderId) {
		this.identityProviderId = identityProviderId;
	}

	@JsonProperty("nameIdOnIdentityProvider")
	public String getNameIdOnIdentityProvider() {
		return nameIdOnIdentityProvider;
	}

	@JsonProperty("nameIdOnIdentityProvider")
	public void setNameIdOnIdentityProvider(String nameIdOnIdentityProvider) {
		this.nameIdOnIdentityProvider = nameIdOnIdentityProvider;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("suspended")
	public Boolean getSuspended() {
		return suspended;
	}

	@JsonProperty("suspended")
	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

	@JsonProperty("suspendedAt")
	public String getSuspendedAt() {
		return suspendedAt;
	}

	@JsonProperty("suspendedAt")
	public void setSuspendedAt(String suspendedAt) {
		this.suspendedAt = suspendedAt;
	}

	@JsonProperty("enabled")
	public Boolean getEnabled() {
		return enabled;
	}

	@JsonProperty("enabled")
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@JsonProperty("enabledAt")
	public String getEnabledAt() {
		return enabledAt;
	}

	@JsonProperty("enabledAt")
	public void setEnabledAt(String enabledAt) {
		this.enabledAt = enabledAt;
	}

	@JsonProperty("roles")
	public List<RoleRepresentation> getRoles() {
		return roles;
	}

	@JsonProperty("roles")
	public void setRoles(List<RoleRepresentation> roles) {
		this.roles = roles;
	}

	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("middleName")
	public String getMiddleName() {
		return middleName;
	}

	@JsonProperty("middleName")
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("employer")
	public String getEmployer() {
		return employer;
	}

	@JsonProperty("employer")
	public void setEmployer(String employer) {
		this.employer = employer;
	}

	@JsonProperty("emailRef")
	public String getEmailRef() {
		return emailRef;
	}

	@JsonProperty("emailRef")
	public void setEmailRef(String emailRef) {
		this.emailRef = emailRef;
	}

	@JsonProperty("personalPhone")
	public String getPersonalPhone() {
		return personalPhone;
	}

	@JsonProperty("personalPhone")
	public void setPersonalPhone(String personalPhone) {
		this.personalPhone = personalPhone;
	}

	@JsonProperty("workPhone")
	public String getWorkPhone() {
		return workPhone;
	}

	@JsonProperty("workPhone")
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	@JsonProperty("nationality")
	public String getNationality() {
		return nationality;
	}

	@JsonProperty("nationality")
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@JsonProperty("taxcode")
	public String getTaxcode() {
		return taxcode;
	}

	@JsonProperty("taxcode")
	public void setTaxcode(String taxcode) {
		this.taxcode = taxcode;
	}
	
	@JsonProperty("avatar")
	public String getAvatar() {
		return avatar;
	}

	@JsonProperty("avatar")
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@JsonProperty("createdAt")
	public String getCreatedAt() {
		return createdAt;
	}

	@JsonProperty("createdAt")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("modifiedAt")
	public String getModifiedAt() {
		return modifiedAt;
	}

	@JsonProperty("modifiedAt")
	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}