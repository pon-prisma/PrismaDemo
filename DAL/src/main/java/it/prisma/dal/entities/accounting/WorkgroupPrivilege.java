package it.prisma.dal.entities.accounting;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "WorkgroupPrivilege", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class WorkgroupPrivilege implements java.io.Serializable {

	private static final long serialVersionUID = 8357379972506504809L;
	private Long workgroupPrivilegeId;
	private String name;
	private String description;
	private Set<UserAccountHasWorkgroup> userAccountHasWorkgroups = new HashSet<UserAccountHasWorkgroup>(
			0);

	public WorkgroupPrivilege() {
	}

	public WorkgroupPrivilege(String name, String description,
			Set<UserAccountHasWorkgroup> userAccountHasWorkgroups) {
		this.name = name;
		this.description = description;
		this.userAccountHasWorkgroups = userAccountHasWorkgroups;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "workgroupPrivilegeID", unique = true, nullable = false)
	public Long getId() {
		return this.workgroupPrivilegeId;
	}

	public void setId(Long workgroupPrivilegeId) {
		this.workgroupPrivilegeId = workgroupPrivilegeId;
	}

	@Column(name = "name", unique = true, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 65535)
	@Type(type="text")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workgroupPrivilege")
	public Set<UserAccountHasWorkgroup> getUserAccountHasWorkgroups() {
		return this.userAccountHasWorkgroups;
	}

	public void setUserAccountHasWorkgroups(
			Set<UserAccountHasWorkgroup> userAccountHasWorkgroups) {
		this.userAccountHasWorkgroups = userAccountHasWorkgroups;
	}

}
