package it.prisma.domain.dsl.accounting;

import java.util.Date;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class AuthTokenRepresentation implements java.io.Serializable {

	private static final long serialVersionUID = 9159632667905910470L;
	
	@JsonProperty("userAccountId")
	private Long userAccountId;
	@JsonProperty("tokenId")
	private String tokenId;
	@JsonProperty("createdAt")
	private Date createdAt;
	@JsonProperty("expiresAt")
	private Date expiresAt;
	@JsonProperty("session")
	private byte session;

	public Long getUserAccountId() {
		return userAccountId;
	}

	/**
	 * @return the session
	 */
	public byte getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(byte session) {
		this.session = session;
	}

	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

}
