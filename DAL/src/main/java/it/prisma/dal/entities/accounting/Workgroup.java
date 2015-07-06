package it.prisma.dal.entities.accounting;

import static javax.persistence.GenerationType.IDENTITY;
import it.prisma.dal.entities.iaas.tenant.IaaSTenant;
import it.prisma.dal.entities.organization.Organization;
import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.appaas.ApplicationRepositoryEntry;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "Workgroup")
public class Workgroup implements java.io.Serializable {

	private static final long serialVersionUID = 7754713442158524456L;
	private Long workgroupId;
	private Organization organization;
	private UserAccount userAccountByCreatedByUserAccountId;
	private UserAccount userAccountByApprovedByUserAccountId;
	private String label;
	private String description;
	private Date createdAt;
	private Date modifiedAt;
	private boolean approved;
	private Set<PaaSService> paaSservices = new HashSet<PaaSService>(0);
	private Set<UserAccountHasWorkgroup> userAccountHasWorkgroups = new HashSet<UserAccountHasWorkgroup>(
			0);
	private Set<ApplicationRepositoryEntry> applicationRepositoryEntries = new HashSet<ApplicationRepositoryEntry>(
			0);

	private Set<UserAccount> referentUsers;
	private IaaSTenant iaasTenant;
	
	
	public Workgroup() {
	}

	public Workgroup(Organization organization,
			UserAccount userAccountByCreatedByUserAccountId, Date createdAt,
			Date modifiedAt, boolean approved) {
		this.organization = organization;
		this.userAccountByCreatedByUserAccountId = userAccountByCreatedByUserAccountId;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.approved = approved;
	}

	public Workgroup(Organization organization,
			UserAccount userAccountByCreatedByUserAccountId,
			UserAccount userAccountByApprovedByUserAccountId, String label,
			String description, Date createdAt, Date modifiedAt,
			boolean approved, Set<PaaSService> paaSservices,
			Set<UserAccountHasWorkgroup> userAccountHasWorkgroups,
			Set<ApplicationRepositoryEntry> applicationRepositoryEntries) {
		this.organization = organization;
		this.userAccountByCreatedByUserAccountId = userAccountByCreatedByUserAccountId;
		this.userAccountByApprovedByUserAccountId = userAccountByApprovedByUserAccountId;
		this.label = label;
		this.description = description;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.approved = approved;
		this.paaSservices = paaSservices;
		this.userAccountHasWorkgroups = userAccountHasWorkgroups;
		this.applicationRepositoryEntries = applicationRepositoryEntries;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "workgroupID", unique = true, nullable = false)
	public Long getId() {
		return this.workgroupId;
	}

	public void setId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organizationID", nullable = false)
	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy_userAccountID", nullable = false)
	public UserAccount getUserAccountByCreatedByUserAccountId() {
		return this.userAccountByCreatedByUserAccountId;
	}

	public void setUserAccountByCreatedByUserAccountId(
			UserAccount userAccountByCreatedByUserAccountId) {
		this.userAccountByCreatedByUserAccountId = userAccountByCreatedByUserAccountId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approvedBy_userAccountID")
	public UserAccount getUserAccountByApprovedByUserAccountId() {
		return this.userAccountByApprovedByUserAccountId;
	}

	public void setUserAccountByApprovedByUserAccountId(
			UserAccount userAccountByApprovedByUserAccountId) {
		this.userAccountByApprovedByUserAccountId = userAccountByApprovedByUserAccountId;
	}

	@Column(name = "label", length = 45)
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Column(name = "description", length = 65535)
	@Type(type="text")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Column(name = "approved", columnDefinition = "bit", nullable = false)
	public boolean isApproved() {
		return this.approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workgroup")
	@JsonBackReference
	public Set<PaaSService> getPaaSServices() {
		return this.paaSservices;
	}

	public void setPaaSServices(Set<PaaSService> paaSservices) {
		this.paaSservices = paaSservices;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "workgroup")
	@JsonBackReference
	public Set<UserAccountHasWorkgroup> getUserAccountHasWorkgroups() {
		return this.userAccountHasWorkgroups;
	}

	public void setUserAccountHasWorkgroups(
			Set<UserAccountHasWorkgroup> userAccountHasWorkgroups) {
		this.userAccountHasWorkgroups = userAccountHasWorkgroups;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workgroup")
	@JsonBackReference
	public Set<ApplicationRepositoryEntry> getApplicationRepositoryEntries() {
		return this.applicationRepositoryEntries;
	}

	public void setApplicationRepositoryEntries(
			Set<ApplicationRepositoryEntry> applicationRepositoryEntries) {
		this.applicationRepositoryEntries = applicationRepositoryEntries;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "workgroupsForWhichIsReferent")
	public Set<UserAccount> getReferentUsers() {
		return this.referentUsers;
	}
 
	public void setReferentUsers(Set<UserAccount> referentUsers) {
		this.referentUsers = referentUsers;
	}
 
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "iaasTenantId")
	public IaaSTenant getIaasTenant() {
		return this.iaasTenant;
	}

	public void setIaasTenant(IaaSTenant iaasTenant) {
		this.iaasTenant = iaasTenant;
	}

}
