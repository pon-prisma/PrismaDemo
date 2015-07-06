package it.prisma.dal.entities.accounting;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "AuthToken")
public class AuthToken implements java.io.Serializable {

	private static final long serialVersionUID = 855027119696331716L;
	
	private UserAccount userAccount;
	private String tokenId;
	private Date createdAt;
	private Date expiresAt;
	private byte session;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name = "tokenID", unique = true, nullable = false)
	public String getId() {
		return this.tokenId;
	}

	public void setId(String tokenId) {
		this.tokenId = tokenId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt", nullable = false, length = 19, updatable = false)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiresAt", nullable = false, length = 19)
	public Date getExpiresAt() {
		return this.expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	/**
	 * @return the session
	 */
	@Column(name = "session", nullable = false)
	public byte getSession() {
		return this.session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(byte session) {
		this.session = session;
	}
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userAccountId", nullable = false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@Override
	public String toString() {
		return "Token [userAccount=" + userAccount + ", tokenId=" + tokenId
				+ ", createdAt=" + createdAt + ", expiresAt=" + expiresAt + ", session=" + session +"]";
	}
	
}
