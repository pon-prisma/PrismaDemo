package it.prisma.dal.entities.accounting;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserAccountHasWorkgroupId implements java.io.Serializable {

	private static final long serialVersionUID = 6368469866573301127L;
	private long userAccountId;
	private long workgroupId;

	public UserAccountHasWorkgroupId() {
	}

	public UserAccountHasWorkgroupId(long userAccountId, long workgroupId) {
		this.userAccountId = userAccountId;
		this.workgroupId = workgroupId;
	}

	@Column(name = "userAccountID", nullable = false)
	public long getUserAccountId() {
		return this.userAccountId;
	}

	public void setUserAccountId(long userAccountId) {
		this.userAccountId = userAccountId;
	}

	@Column(name = "workgroupID", nullable = false)
	public long getWorkgroupId() {
		return this.workgroupId;
	}

	public void setWorkgroupId(long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserAccountHasWorkgroupId))
			return false;
		UserAccountHasWorkgroupId castOther = (UserAccountHasWorkgroupId) other;

		return (this.getUserAccountId() == castOther.getUserAccountId())
				&& (this.getWorkgroupId() == castOther.getWorkgroupId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getUserAccountId();
		result = 37 * result + (int) this.getWorkgroupId();
		return result;
	}

}
