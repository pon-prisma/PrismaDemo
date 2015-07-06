package it.prisma.dal.entities.accounting;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
//@IdClass(UserAccountHasWorkgroupId.class)
@Table(name = "UserAccount_has_Workgroup")
public class UserAccountHasWorkgroup implements java.io.Serializable {

	private static final long serialVersionUID = 6537213525312531347L;
	
	private UserAccountHasWorkgroupId id;
//	@Id private long userAccountId;
//	@Id private long workgroupId;


	private UserAccount userAccountByUserAccountId;
	private WorkgroupPrivilege workgroupPrivilege;
	private UserAccount userAccountByApprovedByUserAccountId;
	private Workgroup workgroup;
	private boolean approved;
	
	
	public UserAccountHasWorkgroup() {
	}

	public UserAccountHasWorkgroup(UserAccountHasWorkgroupId id,
			UserAccount userAccountByUserAccountId,
			UserAccount userAccountByApprovedByUserAccountId,
			Workgroup workgroup, boolean approved) {
		this.id = id;
		this.userAccountByUserAccountId = userAccountByUserAccountId;
		this.userAccountByApprovedByUserAccountId = userAccountByApprovedByUserAccountId;
		this.workgroup = workgroup;
		this.approved = approved;
	}

	public UserAccountHasWorkgroup(UserAccountHasWorkgroupId id,
			UserAccount userAccountByUserAccountId,
			WorkgroupPrivilege workgroupPrivilege,
			UserAccount userAccountByApprovedByUserAccountId,
			Workgroup workgroup, boolean approved) {
		this.id = id;
		this.userAccountByUserAccountId = userAccountByUserAccountId;
		this.workgroupPrivilege = workgroupPrivilege;
		this.userAccountByApprovedByUserAccountId = userAccountByApprovedByUserAccountId;
		this.workgroup = workgroup;
		this.approved = approved;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "userAccountId", column = @Column(name = "userAccountID", nullable = false)),
			@AttributeOverride(name = "workgroupId", column = @Column(name = "workgroupID", nullable = false)) })
	public UserAccountHasWorkgroupId getId() {
		return this.id;
	}

	public void setId(UserAccountHasWorkgroupId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userAccountID", nullable = false, insertable = false, updatable = false)
	public UserAccount getUserAccountByUserAccountId() {
		return this.userAccountByUserAccountId;
	}

	public void setUserAccountByUserAccountId(
			UserAccount userAccountByUserAccountId) {
		this.userAccountByUserAccountId = userAccountByUserAccountId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workgroupPrivilegeID")
	public WorkgroupPrivilege getWorkgroupPrivilege() {
		return this.workgroupPrivilege;
	}

	public void setWorkgroupPrivilege(WorkgroupPrivilege workgroupPrivilege) {
		this.workgroupPrivilege = workgroupPrivilege;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approvedBy_userAccountID", nullable = false)
	public UserAccount getUserAccountByApprovedByUserAccountId() {
		return this.userAccountByApprovedByUserAccountId;
	}

	public void setUserAccountByApprovedByUserAccountId(
			UserAccount userAccountByApprovedByUserAccountId) {
		this.userAccountByApprovedByUserAccountId = userAccountByApprovedByUserAccountId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workgroupID", nullable = false, insertable = false, updatable = false)
	public Workgroup getWorkgroup() {
		return this.workgroup;
	}

	public void setWorkgroup(Workgroup workgroup) {
		this.workgroup = workgroup;
	}

	@Column(name = "approved", columnDefinition = "bit", nullable = false)
	public boolean isApproved() {
		return this.approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

}
