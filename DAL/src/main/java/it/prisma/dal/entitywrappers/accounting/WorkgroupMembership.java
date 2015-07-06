package it.prisma.dal.entitywrappers.accounting;

import it.prisma.dal.entities.accounting.UserAccountHasWorkgroup;
import it.prisma.dal.entities.accounting.WorkgroupPrivilege;

import java.io.Serializable;

public class WorkgroupMembership implements Serializable {
	
	private static final long serialVersionUID = -8371256892034692752L;
	
	private Long workgroupId;
	private Long memberUserAccountId;
	private WorkgroupPrivilege workgroupPrivilege;
	private boolean approved;
	private Long approvedByUserAccountId;
	
	public WorkgroupMembership(UserAccountHasWorkgroup membershipData) {
		this.setWorkgroupId(membershipData.getWorkgroup().getId());
		this.setMemberUserAccountId(membershipData.getUserAccountByUserAccountId().getId());
		this.setWorkgroupPrivilege(membershipData.getWorkgroupPrivilege());
		this.setApproved(membershipData.isApproved());
		if (membershipData.getUserAccountByApprovedByUserAccountId() != null)
			this.setApprovedByUserAccountId(membershipData.getUserAccountByApprovedByUserAccountId().getId());
	}

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public Long getMemberUserAccountId() {
		return memberUserAccountId;
	}

	public void setMemberUserAccountId(Long memberUserAccountId) {
		this.memberUserAccountId = memberUserAccountId;
	}

	public WorkgroupPrivilege getWorkgroupPrivilege() {
		return workgroupPrivilege;
	}

	public void setWorkgroupPrivilege(WorkgroupPrivilege workgroupPrivilege) {
		this.workgroupPrivilege = workgroupPrivilege;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	public Long getApprovedByUserAccountId() {
		return approvedByUserAccountId;
	}

	public void setApprovedByUserAccountId(Long approvedByUserAccountId) {
		this.approvedByUserAccountId = approvedByUserAccountId;
	}

}
