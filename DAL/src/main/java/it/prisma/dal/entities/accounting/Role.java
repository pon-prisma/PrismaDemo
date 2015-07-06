package it.prisma.dal.entities.accounting;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "Role", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Role implements java.io.Serializable {

	private static final long serialVersionUID = -3338156020759544425L;
	private Long roleId;
	private String name;
	private String description;
	private Set<UserAccount> userAccounts = new HashSet<UserAccount>(0);

	public Role() {
	}

	public Role(String name, String description, Set<UserAccount> userAccounts) {
		this.name = name;
		this.description = description;
		this.userAccounts = userAccounts;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "roleID", unique = true, nullable = false)
	public Long getId() {
		return this.roleId;
	}

	public void setId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "name", unique = true, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	@Column(name = "description", length = 65535)
	@Type(type="text")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	public Set<UserAccount> getUserAccounts() {
		return this.userAccounts;
	}

	public void setUserAccounts(Set<UserAccount> userAccounts) {
		this.userAccounts = userAccounts;
	}
	
	@Override
	public boolean equals(Object o) {
		Role role = (Role) o;
		if (this.getId().equals(role.getId()))
			return true;
		else
			return false;
	}

}
