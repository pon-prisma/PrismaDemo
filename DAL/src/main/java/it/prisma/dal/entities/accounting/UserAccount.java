package it.prisma.dal.entities.accounting;

import static javax.persistence.GenerationType.IDENTITY;
import it.prisma.dal.entities.logging.UserAccess;
import it.prisma.dal.entities.orchestrator.lrt.LRT;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "UserAccount", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"identityProviderID", "nameIdOnIdentityProvider" }) })
public class UserAccount implements java.io.Serializable {

	private static final long serialVersionUID = 3690546904486837126L;
	private Long userAccountId;
	private IdentityProvider identityProvider;
	private String nameIdOnIdentityProvider;
	private UserProfile userProfile;
	private String email;
	private boolean suspended;
	private Date createdAt;
	private Date modifiedAt;
	private Date suspendedAt;
	private boolean enabled;
	private Date enabledAt;
	private Set<UserAccountHasWorkgroup> userAccountHasWorkgroupsForApprovedByUserAccountId = new HashSet<UserAccountHasWorkgroup>(
			0);
	private Set<Workgroup> workgroupsForApprovedByUserAccountId = new HashSet<Workgroup>(
			0);
	private Set<UserAccess> userAccesses = new HashSet<UserAccess>(0);
	private Set<UserAccountConfirmation> userAccountConfirmations = new HashSet<UserAccountConfirmation>(
			0);
	private Set<PaaSService> paaSservices = new HashSet<PaaSService>(0);
	private Set<LRT> lrts = new HashSet<LRT>(0);
	private Set<Workgroup> workgroupsForCreatedByUserAccountId = new HashSet<Workgroup>(
			0);
	private Set<ApplicationRepositoryEntry> applicationRepositoryEntries = new HashSet<ApplicationRepositoryEntry>(
			0);
	private Set<Role> roles = new HashSet<Role>(0);
	private Set<UserAccountHasWorkgroup> workgroups = new HashSet<UserAccountHasWorkgroup>(
			0);
	private Set<Workgroup> workgroupsForWhichIsReferent = new HashSet<Workgroup>(
			0);

	public UserAccount() {
	}

	public UserAccount(IdentityProvider identityProvider,
			String nameIdOnIdentityProvider, boolean suspended, Date createdAt,
			Date modifiedAt, boolean enabled) {
		this.identityProvider = identityProvider;
		this.nameIdOnIdentityProvider = nameIdOnIdentityProvider;
		this.suspended = suspended;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.enabled = enabled;
	}

	public UserAccount(IdentityProvider identityProvider,
			String nameIdOnIdentityProvider, String email, boolean suspended,
			boolean enabled) {
		this.identityProvider = identityProvider;
		this.nameIdOnIdentityProvider = nameIdOnIdentityProvider;
		this.email = email;
		this.suspended = suspended;
		this.enabled = enabled;
	}

	public UserAccount(
			IdentityProvider identityProvider,
			String nameIdOnIdentityProvider,
			String email,
			boolean suspended,
			Date createdAt,
			Date modifiedAt,
			Date suspendedAt,
			boolean enabled,
			Date enabledAt,
			Set<UserAccountHasWorkgroup> userAccountHasWorkgroupsForApprovedByUserAccountId,
			Set<Workgroup> workgroupsForApprovedByUserAccountId,
			Set<UserAccess> userAccesses,
			Set<UserAccountConfirmation> userAccountConfirmations,
			Set<PaaSService> paaSservices, Set<LRT> lrts,
			Set<Workgroup> workgroupsForCreatedByUserAccountId,
			Set<ApplicationRepositoryEntry> applicationRepositoryEntries,
			Set<Role> roles, UserProfile userProfile,
			Set<UserAccountHasWorkgroup> workgroups,
			Set<Workgroup> workgroupsForWhichIsReferent) {
		this.identityProvider = identityProvider;
		this.nameIdOnIdentityProvider = nameIdOnIdentityProvider;
		this.email = email;
		this.suspended = suspended;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.suspendedAt = suspendedAt;
		this.enabled = enabled;
		this.enabledAt = enabledAt;
		this.userAccountHasWorkgroupsForApprovedByUserAccountId = userAccountHasWorkgroupsForApprovedByUserAccountId;
		this.workgroupsForApprovedByUserAccountId = workgroupsForApprovedByUserAccountId;
		this.userAccesses = userAccesses;
		this.userAccountConfirmations = userAccountConfirmations;
		this.paaSservices = paaSservices;
		this.lrts = lrts;
		this.workgroupsForCreatedByUserAccountId = workgroupsForCreatedByUserAccountId;
		this.applicationRepositoryEntries = applicationRepositoryEntries;
		this.roles = roles;
		this.userProfile = userProfile;
		this.workgroups = workgroups;
		this.workgroupsForWhichIsReferent = workgroupsForWhichIsReferent;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "userAccountID", unique = true, nullable = false)
	public Long getId() {
		return this.userAccountId;
	}

	public void setId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "identityProviderID", nullable = false)
	public IdentityProvider getIdentityProvider() {
		return this.identityProvider;
	}

	public void setIdentityProvider(IdentityProvider identityProvider) {
		this.identityProvider = identityProvider;
	}

	@Column(name = "nameIdOnIdentityProvider", nullable = false, length = 45)
	public String getNameIdOnIdentityProvider() {
		return this.nameIdOnIdentityProvider;
	}

	public void setNameIdOnIdentityProvider(String nameIdOnIdentityProvider) {
		this.nameIdOnIdentityProvider = nameIdOnIdentityProvider;
	}

	@Column(name = "email", length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "suspended", columnDefinition = "bit", nullable = false)
	public boolean isSuspended() {
		return this.suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "suspendedAt", length = 19)
	public Date getSuspendedAt() {
		return this.suspendedAt;
	}

	public void setSuspendedAt(Date suspendedAt) {
		this.suspendedAt = suspendedAt;
	}

	@Column(name = "enabled", columnDefinition = "bit", nullable = false)
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "enabledAt", length = 19)
	public Date getEnabledAt() {
		return this.enabledAt;
	}

	public void setEnabledAt(Date enabledAt) {
		this.enabledAt = enabledAt;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccountByApprovedByUserAccountId")
	public Set<UserAccountHasWorkgroup> getUserAccountHasWorkgroupsForApprovedByUserAccountId() {
		return this.userAccountHasWorkgroupsForApprovedByUserAccountId;
	}

	public void setUserAccountHasWorkgroupsForApprovedByUserAccountId(
			Set<UserAccountHasWorkgroup> userAccountHasWorkgroupsForApprovedByUserAccountId) {
		this.userAccountHasWorkgroupsForApprovedByUserAccountId = userAccountHasWorkgroupsForApprovedByUserAccountId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccountByApprovedByUserAccountId")
	public Set<Workgroup> getWorkgroupsForApprovedByUserAccountId() {
		return this.workgroupsForApprovedByUserAccountId;
	}

	public void setWorkgroupsForApprovedByUserAccountId(
			Set<Workgroup> workgroupsForApprovedByUserAccountId) {
		this.workgroupsForApprovedByUserAccountId = workgroupsForApprovedByUserAccountId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccount")
	public Set<UserAccess> getUserAccesses() {
		return this.userAccesses;
	}

	public void setUserAccesses(Set<UserAccess> userAccesses) {
		this.userAccesses = userAccesses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccount")
	public Set<UserAccountConfirmation> getUserAccountConfirmations() {
		return this.userAccountConfirmations;
	}

	public void setUserAccountConfirmations(
			Set<UserAccountConfirmation> userAccountConfirmations) {
		this.userAccountConfirmations = userAccountConfirmations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccount")
	public Set<PaaSService> getPaaSServices() {
		return this.paaSservices;
	}

	public void setPaaSServices(Set<PaaSService> paaSservices) {
		this.paaSservices = paaSservices;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccount")
	public Set<LRT> getLrts() {
		return this.lrts;
	}

	public void setLrts(Set<LRT> lrts) {
		this.lrts = lrts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccountByCreatedByUserAccountId")
	public Set<Workgroup> getWorkgroupsForCreatedByUserAccountId() {
		return this.workgroupsForCreatedByUserAccountId;
	}

	public void setWorkgroupsForCreatedByUserAccountId(
			Set<Workgroup> workgroupsForCreatedByUserAccountId) {
		this.workgroupsForCreatedByUserAccountId = workgroupsForCreatedByUserAccountId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userAccount")
	public Set<ApplicationRepositoryEntry> getApplicationRepositoryEntries() {
		return this.applicationRepositoryEntries;
	}

	public void setApplicationRepositoryEntries(
			Set<ApplicationRepositoryEntry> applicationRepositoryEntries) {
		this.applicationRepositoryEntries = applicationRepositoryEntries;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "UserAccount_has_Role", joinColumns = { @JoinColumn(name = "userAccountID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "roleID", nullable = false, updatable = false) })
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "UserAccount_isReferentOf_Workgroup", joinColumns = { @JoinColumn(name = "userAccountID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "workgroupId", nullable = false, updatable = false) })
	public Set<Workgroup> getWorkgroupsForWhichIsReferent() {
		return this.workgroupsForWhichIsReferent;
	}

	public void setWorkgroupsForWhichIsReferent(
			Set<Workgroup> workgroupsForWhichIsReferent) {
		this.workgroupsForWhichIsReferent = workgroupsForWhichIsReferent;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userProfileId")
	public UserProfile getUserProfile() {
		return this.userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "userAccountByUserAccountId")
	public Set<UserAccountHasWorkgroup> getWorkgroups() {
		return this.workgroups;
	}

	public void setWorkgroups(Set<UserAccountHasWorkgroup> workgroups) {
		this.workgroups = workgroups;
	}

	@Override
	public String toString() {
		return "UserAccount [userAccountId=" + userAccountId
				+ ", nameIdOnIdentityProvider=" + nameIdOnIdentityProvider
				+ ", ...]";
	}
}
