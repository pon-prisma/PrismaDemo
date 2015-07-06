package it.prisma.domain.dsl.paas.services.emailaas;

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
	"domain_id",
	"user",
	"password",
	"email"
})
public class EmailInfoRepresentation {

	@JsonProperty("domain_id")
	private Long domain_id;
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
	 * The domain_id
	 */
	@JsonProperty("domain_id")
	public Long getDomain_id() {
		return domain_id;
	}

	/**
	 * 
	 * @param domain_id
	 * The domain_id
	 */
	@JsonProperty("domain_id")
	public void setDomain_id(Long domain_id) {
		this.domain_id = domain_id;
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
		return new HashCodeBuilder().append(domain_id).append(user).append(password).append(email).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof EmailInfoRepresentation) == false) {
			return false;
		}
		EmailInfoRepresentation rhs = ((EmailInfoRepresentation) other);
		return new EqualsBuilder().append(domain_id, rhs.domain_id).append(user, rhs.user).append(password, rhs.password).append(email, rhs.email).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}