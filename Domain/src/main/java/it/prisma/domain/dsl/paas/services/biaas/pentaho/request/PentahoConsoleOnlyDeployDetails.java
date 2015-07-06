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
@JsonPropertyOrder({ "biServerIP", "designerUsername", "designerPassword", "userPublicKey" })
public class PentahoConsoleOnlyDeployDetails extends BaseCustomPaaSDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("biServerIP")
	private String biServerIP;
	@JsonProperty("designerUsername")
	private String designerUsername;
	@JsonProperty("designerPassword")
	private String designerPassword;
	@JsonProperty("userPublicKey")
	private String userPublicKey;

	@JsonProperty("userPublicKey")
	public String getUserPublicKey() {
		return userPublicKey;
	}

	@JsonProperty("userPublicKey")
	public void setUserPublicKey(String userPublicKey) {
		this.userPublicKey = userPublicKey;
	}

	/**
	 * 
	 * @return The biServerIP
	 */
	@JsonProperty("biServerIP")
	public String getBiServerIP() {
		return biServerIP;
	}

	/**
	 * 
	 * @param biServerIP
	 *            The biServerIP
	 */
	@JsonProperty("biServerIP")
	public void setBiServerIP(String biServerIP) {
		this.biServerIP = biServerIP;
	}

	/**
	 * 
	 * @return The designerUsername
	 */
	@JsonProperty("designerUsername")
	public String getDesignerUsername() {
		return designerUsername;
	}

	/**
	 * 
	 * @param designerUsername
	 *            The designerUsername
	 */
	@JsonProperty("designerUsername")
	public void setDesignerUsername(String designerUsername) {
		this.designerUsername = designerUsername;
	}

	/**
	 * 
	 * @return The designerPassword
	 */
	@JsonProperty("designerPassword")
	public String getDesignerPassword() {
		return designerPassword;
	}

	/**
	 * 
	 * @param designerPassword
	 *            The designerPassword
	 */
	@JsonProperty("designerPassword")
	public void setDesignerPassword(String designerPassword) {
		this.designerPassword = designerPassword;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(biServerIP)
				.append(designerUsername).append(designerPassword).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof PentahoConsoleOnlyDeployDetails) == false) {
			return false;
		}
		PentahoConsoleOnlyDeployDetails rhs = ((PentahoConsoleOnlyDeployDetails) other);
		return new EqualsBuilder().append(biServerIP, rhs.biServerIP)
				.append(designerUsername, rhs.designerUsername)
				.append(designerPassword, rhs.designerPassword).isEquals();
	}

}