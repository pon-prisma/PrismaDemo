package it.prisma.dal.entities.logging;

import static javax.persistence.GenerationType.IDENTITY;
import it.prisma.dal.entities.accounting.UserAccount;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * UserAccess generated by hbm2java
 */
@Entity
@Table(name = "UserAccess")
public class UserAccess implements java.io.Serializable {

	private static final long serialVersionUID = 4882693676475482496L;
	private Long userAccessId;
	private UserAccount userAccount;
	private Date time;
	private String ip;

	public UserAccess() {
	}

	public UserAccess(UserAccount userAccount, Date time) {
		this.userAccount = userAccount;
		this.time = time;
	}

	public UserAccess(UserAccount userAccount, Date time, String ip) {
		this.userAccount = userAccount;
		this.time = time;
		this.ip = ip;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "userAccessID", unique = true, nullable = false)
	public Long getUserAccessId() {
		return this.userAccessId;
	}

	public void setUserAccessId(Long userAccessId) {
		this.userAccessId = userAccessId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userAccountID", nullable = false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time", nullable = false, length = 19)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "ip", length = 64)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
