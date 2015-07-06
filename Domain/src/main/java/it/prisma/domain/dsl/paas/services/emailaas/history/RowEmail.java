package it.prisma.domain.dsl.paas.services.emailaas.history;

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
@JsonPropertyOrder({
	"id",
	"domain_id",
	"user_prisma_id",
	"user",
	"password",
	"email"
})
public class RowEmail {

	@JsonProperty("id")
	private Long id;
	@JsonProperty("domain_id")
	private Long domainId;
	@JsonProperty("user_prisma_id")
	private Long userPrismaId;
	@JsonProperty("user")
	private String user;
	@JsonProperty("password")
	private String password;
	@JsonProperty("email")
	private String email;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 * The domainId
	 */
	@JsonProperty("domain_id")
	public Long getDomainId() {
		return domainId;
	}

	/**
	 * 
	 * @param domainId
	 * The domain_id
	 */
	@JsonProperty("domain_id")
	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	/**
	 * 
	 * @return
	 * The userPrismaId
	 */
	@JsonProperty("user_prisma_id")
	public Long getUserPrismaId() {
		return userPrismaId;
	}

	/**
	 * 
	 * @param userPrismaId
	 * The user_prisma_id
	 */
	@JsonProperty("user_prisma_id")
	public void setUserPrismaId(Long userPrismaId) {
		this.userPrismaId = userPrismaId;
	}

	/**
	 * 
	 * @return
	 * The user
	 */
	@JsonProperty("user")
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 * The user
	 */
	@JsonProperty("user")
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * 
	 * @return
	 * The password
	 */
	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password
	 * The password
	 */
	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return
	 * The email
	 */
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 * The email
	 */
	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
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
		return new HashCodeBuilder().append(id).append(domainId).append(userPrismaId).append(user).append(password).append(email).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof RowEmail) == false) {
			return false;
		}
		RowEmail rhs = ((RowEmail) other);
		return new EqualsBuilder().append(id, rhs.id).append(domainId, rhs.domainId).append(userPrismaId, rhs.userPrismaId).append(user, rhs.user).append(password, rhs.password).append(email, rhs.email).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}

