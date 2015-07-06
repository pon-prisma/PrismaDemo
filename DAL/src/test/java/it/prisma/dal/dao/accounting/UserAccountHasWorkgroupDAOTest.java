package it.prisma.dal.dao.accounting;

import it.prisma.dal.entities.accounting.IdentityProvider;
import it.prisma.dal.entities.accounting.Role;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.UserAccountHasWorkgroup;
import it.prisma.dal.entities.accounting.UserAccountHasWorkgroupId;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.accounting.WorkgroupPrivilege;
import it.prisma.dal.entities.organization.Organization;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserAccountHasWorkgroupDAOTest {

	@Deployment
	public static Archive<?> createDeployment() {
		MavenDependencyResolver resolver = DependencyResolvers.use(
				MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addAsResource("test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource("xa-orchestrator-ds.xml")
				.addPackages(true, "it.prisma.dal")
				.addAsLibraries(
						resolver.artifacts("com.mysema.querydsl:querydsl-core",
								"com.mysema.querydsl:querydsl-jpa",
								"com.mysema.querydsl:querydsl-apt")
								.resolveAsFiles())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	// DAO to test
	@Inject	private OrganizationDAO organizationDAO;
	@Inject	private IdentityProviderDAO identityProviderDAO;
	@Inject	private UserAccountDAO userAccountDAO;
	@Inject	private RoleDAO roleDAO;
	@Inject	private WorkgroupPrivilegeDAO workgroupPrivilegeDAO;
	@Inject	private WorkgroupDAO workgroupDAO;
	@Inject private UserAccountHasWorkgroupDAO userAccountHasWorkgroupDAO;

	private UserAccount userAccount;
	private Organization organization;
	private IdentityProvider identityProvider;
	private Role role;
	private WorkgroupPrivilege workgroupPrivilege;
	private Workgroup workgroup;

	// Setup test

	@Before
	public void init() {
		assert (organizationDAO.count() == 0);
		assert (identityProviderDAO.count() == 0);
		assert (roleDAO.count() == 0);
		assert (userAccountDAO.count() == 0);
		assert (workgroupPrivilegeDAO.count() == 0);
		Organization anOrganization = new Organization();
		anOrganization.setName("Moe's Tavern");
		anOrganization
				.setDescription("Moe's Tavern, once known as Moe's Bar is the "
						+ "local tavern in Springfield. The bar is named after its owner, Moe Szyslak.");
		anOrganization
				.setLogoUri("http://simpsonswiki.com/wiki/File:Moe%27s_Tavern.png");
		anOrganization
				.setWebsiteUri("http://simpsonswiki.com/wiki/Moe's_Tavern");
		anOrganization.setWebsiteLabel("The Simpsons Wiki: Moe's Tavern");
		organization = organizationDAO.create(anOrganization);
		IdentityProvider anIdentityProvider = new IdentityProvider();
		anIdentityProvider.setEntityId("http://sample.prisma.it/sampleIdp");
		anIdentityProvider.setOrganization(organization);
		identityProvider = identityProviderDAO.create(anIdentityProvider);
		Role aRole = new Role();
		aRole.setName("PRISMA_USER");
		aRole.setDescription("PRISMA registered user.");
		role = roleDAO.create(aRole);
		UserAccount anUserAccount = new UserAccount();
		anUserAccount.setIdentityProvider(identityProvider);
		anUserAccount.setNameIdOnIdentityProvider("jdoe");
		anUserAccount.setEmail("jdoe@prisma.miur.gov");
		anUserAccount.setSuspended(false);
		anUserAccount.setEnabled(false);
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleDAO.findByName(role.getName()));
		anUserAccount.setRoles(roles);
		userAccount = userAccountDAO.create(anUserAccount);
		WorkgroupPrivilege aWorkgroupPrivilege = new WorkgroupPrivilege();
		aWorkgroupPrivilege.setName("WG_ADMIN");
		aWorkgroupPrivilege.setDescription("Workgroup Admin");
		workgroupPrivilege = workgroupPrivilegeDAO.create(aWorkgroupPrivilege);
		Workgroup aWorkgroup = new Workgroup();
		aWorkgroup.setLabel("The Be Sharps");
		aWorkgroup
				.setDescription("The Be Sharps were once a band consisting of Homer Simpson, "
						+ "Apu Nahasapeemapetilon, Seymour Skinner, and Chief Wiggum (later replaced "
						+ "by Barney Gumble).");
		aWorkgroup.setOrganization(organization);
		aWorkgroup.setUserAccountByCreatedByUserAccountId(userAccount);
		workgroup = workgroupDAO.create(aWorkgroup);
		assert (userAccountDAO.count() == 1);
		assert (organizationDAO.count() == 1);
		assert (identityProviderDAO.count() == 1);
		assert (roleDAO.count() == 1);
		assert (workgroupPrivilegeDAO.count() == 1);
		assert (workgroupDAO.count() == 1);
	}

	@After
	public void clear() {
		assert (workgroupDAO.count() == 1);
		assert (organizationDAO.count() == 1);
		assert (identityProviderDAO.count() == 1);
		assert (roleDAO.count() == 1);
		assert (userAccountDAO.count() == 1);
		assert (workgroupPrivilegeDAO.count() == 1);
		workgroupDAO.delete(workgroup.getId());
		workgroupPrivilegeDAO.delete(workgroupPrivilege.getId());
		Set<Role> roles = new HashSet<Role>();
		roles.clear();
		userAccount.setRoles(roles);
		userAccountDAO.update(userAccount);
		userAccountDAO.delete(userAccount.getId());
		roleDAO.delete(role.getId());
		identityProviderDAO.delete(identityProvider.getId());
		organizationDAO.delete(organization.getId());
		assert (roleDAO.count() == 0);
		assert (organizationDAO.count() == 0);
		assert (identityProviderDAO.count() == 0);
		assert (userAccountDAO.count() == 0);
		assert (workgroupPrivilegeDAO.count() == 0);
		assert (workgroupDAO.count() == 0);
	}

	// Create, update and delete
	@Test
	public void createUpdateAndDeleteTest() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		assert(userAccountDAO.count() == 1);
		assert(workgroupDAO.count() == 1);
		assert(userAccountHasWorkgroupDAO.count() == 0);
		long counter = userAccountHasWorkgroupDAO.count();
		UserAccountHasWorkgroup membership = new UserAccountHasWorkgroup();
		UserAccountHasWorkgroupId membershipId = new UserAccountHasWorkgroupId();
		membershipId.setWorkgroupId(workgroup.getId());
		membershipId.setUserAccountId(userAccount.getId());
		membership.setId(membershipId);
		membership.setWorkgroup(workgroup);
		membership.setUserAccountByUserAccountId(userAccount);
		membership.setWorkgroupPrivilege(workgroupPrivilege);
		membership.setApproved(true);
		membership.setUserAccountByApprovedByUserAccountId(userAccount);
		counter++;
		UserAccountHasWorkgroup createdMembership = userAccountHasWorkgroupDAO
				.create(membership);
		Assert.assertEquals(counter, userAccountHasWorkgroupDAO.count());
		Assert.assertEquals(counter, workgroupDAO.findById(workgroup.getId())
				.getUserAccountHasWorkgroups().size());
		Assert.assertEquals(counter,
				userAccountDAO.findById(userAccount.getId()).getWorkgroups()
						.size());
		userAccountHasWorkgroupDAO.delete(createdMembership.getId());
		counter--;
		Assert.assertEquals(counter, userAccountHasWorkgroupDAO.count());
		Assert.assertEquals(counter, workgroupDAO.findById(workgroup.getId())
				.getUserAccountHasWorkgroups().size());
		Assert.assertEquals(counter,
				userAccountDAO.findById(userAccount.getId()).getWorkgroups()
						.size());
		assert(userAccountHasWorkgroupDAO.count() == 0);
	}
	
	@Test
	public void approveAndUnapproveMembershipTest() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		assert(userAccountDAO.count() == 1);
		assert(workgroupDAO.count() == 1);
		assert(userAccountHasWorkgroupDAO.count() == 0);
		UserAccountHasWorkgroup membership = new UserAccountHasWorkgroup();
		UserAccountHasWorkgroupId membershipId = new UserAccountHasWorkgroupId();
		membershipId.setWorkgroupId(workgroup.getId());
		membershipId.setUserAccountId(userAccount.getId());
		membership.setId(membershipId);
		membership.setWorkgroup(workgroup);
		membership.setUserAccountByUserAccountId(userAccount);
		membership.setWorkgroupPrivilege(workgroupPrivilege);
		membership.setApproved(false);
		membership.setUserAccountByApprovedByUserAccountId(userAccount);
		UserAccountHasWorkgroup createdMembership = userAccountHasWorkgroupDAO
				.create(membership);
		Assert.assertFalse(createdMembership.isApproved());
		userAccountHasWorkgroupDAO.approveMembership(createdMembership.getId());
		UserAccountHasWorkgroup approvedMembership = userAccountHasWorkgroupDAO
				.findById(createdMembership.getId());
		Assert.assertTrue(approvedMembership.isApproved());
		userAccountHasWorkgroupDAO.unapproveMembership(approvedMembership.getId());
		UserAccountHasWorkgroup unapproveddMembership = userAccountHasWorkgroupDAO
				.findById(createdMembership.getId());
		Assert.assertFalse(unapproveddMembership.isApproved());
		userAccountHasWorkgroupDAO.delete(createdMembership.getId());
		assert(userAccountHasWorkgroupDAO.count() == 0);
	}
	
}
