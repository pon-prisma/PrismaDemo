package it.prisma.domain.dsl.accounting.users.requests;

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
@JsonPropertyOrder({ "identityProviderEntityId", "nameIdOnIdentityProvider",
		"email", "firstName", "middleName", "lastName", "personalPhone",
		"workPhone", "nationality", "taxcode", "employer", "emailRef" })
public class SignUpUserRequest {

	@JsonProperty("identityProviderEntityId")
	private String identityProviderEntityId;
	@JsonProperty("nameIdOnIdentityProvider")
	private String nameIdOnIdentityProvider;
	@JsonProperty("email")
	private String email;
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("middleName")
	private String middleName;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("personalPhone")
	private String personalPhone;
	@JsonProperty("workPhone")
	private String workPhone;
	@JsonProperty("nationality")
	private String nationality;
	@JsonProperty("taxcode")
	private String taxcode;
	@JsonProperty("employer")
	private String employer;
	@JsonProperty("emailRef")
	private String emailRef;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("identityProviderEntityId")
	public String getIdentityProviderEntityId() {
		return identityProviderEntityId;
	}

	@JsonProperty("identityProviderEntityId")
	public void setIdentityProviderEntityId(String identityProviderEntityId) {
		this.identityProviderEntityId = identityProviderEntityId;
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
	public void setTaxCode(String taxcode) {
		this.taxcode = taxcode;
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