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
@JsonPropertyOrder({ "count", "users" })
public class UsersRepresentation implements java.io.Serializable {

	@JsonProperty("count")
	private Long count;
	@JsonProperty("users")
	private List<UserRepresentation> users = new ArrayList<UserRepresentation>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The count
	 */
	@JsonProperty("count")
	public Long getCount() {
		return count;
	}

	/**
	 * 
	 * @param count
	 *            The count
	 */
	@JsonProperty("count")
	public void setCount(Long count) {
		this.count = count;
	}

	/**
	 * 
	 * @return The users
	 */
	@JsonProperty("users")
	public List<UserRepresentation> getUsers() {
		return users;
	}

	/**
	 * 
	 * @param users
	 *            The users
	 */
	@JsonProperty("users")
	public void setUsers(
			List<UserRepresentation> users) {
		this.users = users;
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
		return new HashCodeBuilder().append(count).append(users)
				.append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof UsersRepresentation) == false) {
			return false;
		}
		UsersRepresentation rhs = ((UsersRepresentation) other);
		return new EqualsBuilder().append(count, rhs.count)
				.append(users, rhs.users)
				.append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}
