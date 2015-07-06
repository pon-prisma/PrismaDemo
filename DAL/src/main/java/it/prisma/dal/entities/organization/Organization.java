package it.prisma.dal.entities.organization;

import static javax.persistence.GenerationType.IDENTITY;
import it.prisma.dal.entities.accounting.IdentityProvider;
import it.prisma.dal.entities.accounting.Workgroup;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "Organization", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Organization implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7319531384530352902L;
	private Long organizationId;
	private String name;
	private String description;
	private String logoUri;
	private String websiteUri;
	private String websiteLabel;
	private Date createdAt;
	private Date modifiedAt;
	private Set<Workgroup> workgroups = new HashSet<Workgroup>(0);
	private Set<OrganizationReferent> organizationReferents = new HashSet<OrganizationReferent>(
			0);
	private Set<IdentityProvider> identityProviders = new HashSet<IdentityProvider>(
			0);

	public Organization() {
	}

	public Organization(String name, String description, Date createdAt,
			Date modifiedAt) {
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public Organization(String name, String description, String logoUri,
			String websiteUri, String websiteLabel, Date createdAt,
			Date modifiedAt, Set<Workgroup> workgroups,
			Set<OrganizationReferent> organizationReferents,
			Set<IdentityProvider> identityProviders) {
		this.name = name;
		this.description = description;
		this.logoUri = logoUri;
		this.websiteUri = websiteUri;
		this.websiteLabel = websiteLabel;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.workgroups = workgroups;
		this.organizationReferents = organizationReferents;
		this.identityProviders = identityProviders;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "organizationID", unique = true, nullable = false)
	public Long getId() {
		return this.organizationId;
	}

	public void setId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name = "name", unique = true, nullable = false, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", nullable = false, length = 65535)
	@Type(type="text")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "logoURI", length = 2083)
	public String getLogoUri() {
		return this.logoUri;
	}

	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}

	@Column(name = "websiteURI", length = 2083)
	public String getWebsiteUri() {
		return this.websiteUri;
	}

	public void setWebsiteUri(String websiteUri) {
		this.websiteUri = websiteUri;
	}

	@Column(name = "websiteLabel", length = 100)
	public String getWebsiteLabel() {
		return this.websiteLabel;
	}

	public void setWebsiteLabel(String websiteLabel) {
		this.websiteLabel = websiteLabel;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
	public Set<Workgroup> getWorkgroups() {
		return this.workgroups;
	}

	public void setWorkgroups(Set<Workgroup> workgroups) {
		this.workgroups = workgroups;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
	public Set<OrganizationReferent> getOrganizationReferents() {
		return this.organizationReferents;
	}

	public void setOrganizationReferents(
			Set<OrganizationReferent> organizationReferents) {
		this.organizationReferents = organizationReferents;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
	public Set<IdentityProvider> getIdentityProviders() {
		return this.identityProviders;
	}

	public void setIdentityProviders(Set<IdentityProvider> identityProviders) {
		this.identityProviders = identityProviders;
	}

}
