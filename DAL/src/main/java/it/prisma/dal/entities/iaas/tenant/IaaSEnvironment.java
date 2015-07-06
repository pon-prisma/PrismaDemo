package it.prisma.dal.entities.iaas.tenant;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "IaaSEnvironment")
public class IaaSEnvironment implements Serializable {

	private static final long serialVersionUID = -2432530610962548197L;

	private Long iaasEnvironmentId;
	private String name;
	private String version;
	private String description;
	private String identityURL;
	private String identityVersion;
	private String domain;
	private String adminUsername;
	private String adminPassword;
	private Set<IaaSTenant> iaasTenants = new HashSet<IaaSTenant>(0);

	public IaaSEnvironment() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "iaasEnvironmentId", unique = true, nullable = false)
	public Long getId() {
		return iaasEnvironmentId;
	}

	public void setId(Long iaasEnvironmentId) {
		this.iaasEnvironmentId = iaasEnvironmentId;
	}

	@Column(name = "name", nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "version", nullable=false)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "description", nullable=false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "identityURL", nullable=false)
	public String getIdentityURL() {
		return identityURL;
	}

	public void setIdentityURL(String identityURL) {
		this.identityURL = identityURL;
	}

	@Column(name = "identityVersion", nullable=false)
	public String getIdentityVersion() {
		return identityVersion;
	}

	public void setIdentityVersion(String identityVersion) {
		this.identityVersion = identityVersion;
	}

	@Column(name = "domain", nullable=true)
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "adminUsername", nullable=false)
	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	@Column(name = "adminPassword", nullable=false)
	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "iaasEnvironment")
	public Set<IaaSTenant> getiaasTenants() {
		return this.iaasTenants;

	}

	public void setiaasTenants(Set<IaaSTenant> iaasTenants) {
		this.iaasTenants = iaasTenants;
	}

}
