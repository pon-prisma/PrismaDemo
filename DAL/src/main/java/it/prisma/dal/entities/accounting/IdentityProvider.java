package it.prisma.dal.entities.accounting;

import static javax.persistence.GenerationType.IDENTITY;
import it.prisma.dal.entities.organization.Organization;

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

@Entity
@Table(name = "IdentityProvider")
public class IdentityProvider implements java.io.Serializable {

	private static final long serialVersionUID = 7161430200488130501L;
	private Long identityProviderId;
	private Organization organization;
	private String entityId;
	private Date createdAt;
	private Date modifiedAt;
	private Set<UserAccount> userAccounts = new HashSet<UserAccount>(0);

	public IdentityProvider() {
	}

	public IdentityProvider(Organization organization, String entityId,
			Date createdAt, Date modifiedAt) {
		this.organization = organization;
		this.entityId = entityId;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public IdentityProvider(Organization organization, String entityId,
			Date createdAt, Date modifiedAt, Set<UserAccount> userAccounts) {
		this.organization = organization;
		this.entityId = entityId;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.userAccounts = userAccounts;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "identityProviderID", unique = true, nullable = false)
	public Long getId() {
		return this.identityProviderId;
	}

	public void setId(Long identityProviderId) {
		this.identityProviderId = identityProviderId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organizationID", nullable = false)
	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Column(name = "entityID", nullable = false, length = 2048)
	public String getEntityId() {
		return this.entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt", nullable = false, length = 19, insertable = false, updatable = false)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedAt", nullable = false, length = 19, insertable = false, updatable = false)
	public Date getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "identityProvider")
	public Set<UserAccount> getUserAccounts() {
		return this.userAccounts;
	}

	public void setUserAccounts(Set<UserAccount> userAccounts) {
		this.userAccounts = userAccounts;
	}

}
