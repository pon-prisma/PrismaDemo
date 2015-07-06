package it.prisma.domain.dsl.paas.services.biaas.pentaho.request;

import it.prisma.domain.dsl.paas.services.generic.request.BaseCustomPaaSDetails;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "adminPassword" })
public class PentahoServerOnlyDeployDetails extends BaseCustomPaaSDetails {

	@JsonProperty("adminPassword")
	private String adminPassword;

	/**
	 * 
	 * @return The adminPassword
	 */
	@JsonProperty("adminPassword")
	public String getAdminPassword() {
		return adminPassword;
	}

	/**
	 * 
	 * @param adminPassword
	 *            The adminPassword
	 */
	@JsonProperty("adminPassword")
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(adminPassword).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof PentahoServerOnlyDeployDetails) == false) {
			return false;
		}
		PentahoServerOnlyDeployDetails rhs = ((PentahoServerOnlyDeployDetails) other);
		return new EqualsBuilder().append(adminPassword, rhs.adminPassword)
				.isEquals();
	}

}