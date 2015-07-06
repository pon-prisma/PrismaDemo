package it.prisma.dal.entities.orchestrator.lrt;

// Generated 17-set-2014 21.25.53 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;
import it.prisma.dal.entities.accounting.UserAccount;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Lrt generated by hbm2java
 */
@Entity
@Table(name = "LRT")
public class LRT implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5511909788716204282L;

	public enum LRTStatus {
		RUNNING, WAITING, COMPLETE
	}

	private Long lrtid;
	private UserAccount userAccount;
	private LRTEvent result;
	private Date completedAt;
	private Date createdAt;
	private Long instanceId;
	private Long lastPendingWorkItemId;
	private String name;
	private LRTStatus status;
	private Set<LRTEvent> lrtevents = new HashSet<LRTEvent>(0);

	public LRT() {
	}

	public LRT(long instanceId, String name, UserAccount userAccount) {
		this(instanceId, name, userAccount, LRTStatus.RUNNING, new Date()
				.getTime());
	}

	public LRT(long instanceId, String name, UserAccount userAccount,
			LRTStatus status, Long createdTime) {
		this.instanceId = instanceId;
		this.name = name;
		this.userAccount = userAccount;
		this.status = status;
	}

	public LRT(UserAccount userAccount, LRTEvent result, Date completedAt,
			Date createdAt, Long instanceId, Long lastPendingWorkItemId,
			String name, LRTStatus status, Set<LRTEvent> lrtevents) {
		this.userAccount = userAccount;
		this.result = result;
		this.completedAt = completedAt;
		this.createdAt = createdAt;
		this.instanceId = instanceId;
		this.lastPendingWorkItemId = lastPendingWorkItemId;
		this.name = name;
		this.status = status;
		this.lrtevents = lrtevents;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "LRTID", unique = true, nullable = false)
	public Long getId() {
		return this.lrtid;
	}

	public void setId(Long lrtid) {
		this.lrtid = lrtid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userAccountID")
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "result_EventID")
	public LRTEvent getResult() {
		return this.result;
	}

	public void setResult(LRTEvent result) {
		this.result = result;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "completedAt", length = 19)
	public Date getCompletedAt() {
		return this.completedAt;
	}

	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt", nullable = false, length = 19, insertable=false, updatable=false)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "instanceId")
	public Long getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	@Column(name = "lastPendingWorkItemId")
	public Long getLastPendingWorkItemId() {
		return this.lastPendingWorkItemId;
	}

	public void setLastPendingWorkItemId(Long lastPendingWorkItemId) {
		this.lastPendingWorkItemId = lastPendingWorkItemId;
	}

	@Column(name = "name", nullable = false, length=512)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "status")
	public LRTStatus getStatus() {
		return this.status;
	}

	public void setStatus(LRTStatus status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lrt")
	public Set<LRTEvent> getLrtevents() {
		return this.lrtevents;
	}

	public void setLrtevents(Set<LRTEvent> lrtevents) {
		this.lrtevents = lrtevents;
	}

}