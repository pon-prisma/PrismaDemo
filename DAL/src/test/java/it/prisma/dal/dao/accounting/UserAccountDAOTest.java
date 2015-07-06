package it.prisma.dal.dao.accounting;

import it.prisma.dal.entities.accounting.IdentityProvider;
import it.prisma.dal.entities.accounting.Role;
import it.prisma.dal.entities.accounting.UserAccount;
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
public class UserAccountDAOTest {
	
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
	@Inject private OrganizationDAO organizationDAO;
	@Inject private IdentityProviderDAO identityProviderDAO;
	@Inject private UserAccountDAO userAccountDAO;
	@Inject private RoleDAO roleDAO;
	
	private Organization organization;
	private IdentityProvider identityProvider;
	private Role role;
	
	// Setup test
	
	@Before
	public void init() {
		assert(organizationDAO.count() == 0);
		assert(identityProviderDAO.count() == 0);
		assert(roleDAO.count() == 0);
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
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
	}
	
	@After
	public void clear() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		identityProviderDAO.delete(identityProvider.getId());
		organizationDAO.delete(organization.getId());
		roleDAO.delete(role.getId());
		assert(roleDAO.count() == 0);
		assert(organizationDAO.count() == 0);
		assert(identityProviderDAO.count() == 0);
	}
	
	// Create, update and delete
	
	@Test
	public void createUpdateAndDeleteTest() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		long counter = userAccountDAO.count();
		UserAccount anUserAccount = new UserAccount();
		anUserAccount.setIdentityProvider(identityProvider);
		anUserAccount.setNameIdOnIdentityProvider("jdoe");
		anUserAccount.setEmail("jdoe@prisma.miur.gov");
		anUserAccount.setSuspended(false);
		anUserAccount.setEnabled(false);
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleDAO.findByName(role.getName()));
		anUserAccount.setRoles(roles);
		UserAccount createdUserAccount = userAccountDAO.create(anUserAccount);
		counter++;
		Assert.assertEquals(counter, userAccountDAO.count());
		roles.clear();
		createdUserAccount.setRoles(roles);
		userAccountDAO.update(createdUserAccount);
		userAccountDAO.delete(createdUserAccount.getId());
		counter--;
		Assert.assertEquals(counter, userAccountDAO.count());
		assert(userAccountDAO.count() == 0);
	}
	
	@Test
	public void suspendAndUnsuspendTest() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		long counter = userAccountDAO.count();
		UserAccount anUserAccount = new UserAccount();
		anUserAccount.setIdentityProvider(identityProvider);
		anUserAccount.setNameIdOnIdentityProvider("jdoe");
		anUserAccount.setEmail("jdoe@prisma.miur.gov");
		anUserAccount.setSuspended(false);
		anUserAccount.setEnabled(false);
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleDAO.findByName(role.getName()));
		anUserAccount.setRoles(roles);
		UserAccount createdUserAccount = userAccountDAO.create(anUserAccount);
		counter++;
		Assert.assertEquals(counter, userAccountDAO.count());
		userAccountDAO.suspend(createdUserAccount.getId());
		Assert.assertTrue(userAccountDAO.findById(createdUserAccount.getId()).isSuspended());
		userAccountDAO.unsuspend(createdUserAccount.getId());
		Assert.assertFalse(userAccountDAO.findById(createdUserAccount.getId()).isSuspended());
		userAccountDAO.delete(createdUserAccount.getId());
		counter--;
		Assert.assertEquals(counter, userAccountDAO.count());
		assert(userAccountDAO.count() == 0);
	}
	
	@Test
	public void enableAndDisableTest() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		long counter = userAccountDAO.count();
		UserAccount anUserAccount = new UserAccount();
		anUserAccount.setIdentityProvider(identityProvider);
		anUserAccount.setNameIdOnIdentityProvider("jdoe");
		anUserAccount.setEmail("jdoe@prisma.miur.gov");
		anUserAccount.setSuspended(false);
		anUserAccount.setEnabled(false);
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleDAO.findByName(role.getName()));
		anUserAccount.setRoles(roles);
		UserAccount createdUserAccount = userAccountDAO.create(anUserAccount);
		counter++;
		Assert.assertEquals(counter, userAccountDAO.count());
		userAccountDAO.enable(createdUserAccount.getId());
		Assert.assertTrue(userAccountDAO.findById(createdUserAccount.getId()).isEnabled());
		userAccountDAO.disable(createdUserAccount.getId());
		Assert.assertFalse(userAccountDAO.findById(createdUserAccount.getId()).isEnabled());
		userAccountDAO.delete(createdUserAccount.getId());
		counter--;
		Assert.assertEquals(counter, userAccountDAO.count());
		assert(userAccountDAO.count() == 0);
	}
	
	@Test
	public void updateMailTest() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		long counter = userAccountDAO.count();
		UserAccount anUserAccount = new UserAccount();
		anUserAccount.setIdentityProvider(identityProvider);
		anUserAccount.setNameIdOnIdentityProvider("jdoe");
		anUserAccount.setEmail("jdoe@prisma.miur.gov");
		anUserAccount.setSuspended(false);
		anUserAccount.setEnabled(false);
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleDAO.findByName(role.getName()));
		anUserAccount.setRoles(roles);
		UserAccount createdUserAccount = userAccountDAO.create(anUserAccount);
		counter++;
		Assert.assertEquals(counter, userAccountDAO.count());
		userAccountDAO.updateEmail(createdUserAccount.getId(), "test@test.miur.gov");
		Assert.assertEquals("test@test.miur.gov", userAccountDAO.findById(createdUserAccount.getId()).getEmail());
		userAccountDAO.delete(createdUserAccount.getId());
		counter--;
		Assert.assertEquals(counter, userAccountDAO.count());
		assert(userAccountDAO.count() == 0);
	}
	
	@Test
	public void addAndRemoveRoleTest() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		long counter = userAccountDAO.count();
		UserAccount anUserAccount = new UserAccount();
		anUserAccount.setIdentityProvider(identityProvider);
		anUserAccount.setNameIdOnIdentityProvider("jdoe");
		anUserAccount.setEmail("jdoe@prisma.miur.gov");
		anUserAccount.setSuspended(false);
		anUserAccount.setEnabled(false);
		UserAccount createdUserAccount = userAccountDAO.create(anUserAccount);
		counter++;
		Assert.assertEquals(counter, userAccountDAO.count());
		Assert.assertTrue(createdUserAccount.getRoles().isEmpty());
		userAccountDAO.addRole(createdUserAccount.getId(), role);
		UserAccount updatedUserAccount = userAccountDAO.findById(createdUserAccount.getId());
		Assert.assertTrue(userAccountDAO.hasRole(updatedUserAccount.getRoles(), role));
		userAccountDAO.removeRole(updatedUserAccount.getId(), role);
		updatedUserAccount = userAccountDAO.findById(createdUserAccount.getId());
		Assert.assertFalse(userAccountDAO.hasRole(updatedUserAccount.getRoles(), role));
		userAccountDAO.delete(createdUserAccount.getId());
		counter--;
		Assert.assertEquals(counter, userAccountDAO.count());
		assert(userAccountDAO.count() == 0);
	}
	
	@Test
	public void removeAllRolesTest() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		long counter = userAccountDAO.count();
		UserAccount anUserAccount = new UserAccount();
		anUserAccount.setIdentityProvider(identityProvider);
		anUserAccount.setNameIdOnIdentityProvider("jdoe");
		anUserAccount.setEmail("jdoe@prisma.miur.gov");
		anUserAccount.setSuspended(false);
		anUserAccount.setEnabled(false);
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleDAO.findByName(role.getName()));
		anUserAccount.setRoles(roles);
		UserAccount createdUserAccount = userAccountDAO.create(anUserAccount);
		counter++;
		Assert.assertEquals(counter, userAccountDAO.count());
		userAccountDAO.removeAllRoles(createdUserAccount.getId());
		Assert.assertTrue(userAccountDAO.findById(createdUserAccount.getId())
				.getRoles().isEmpty());
		userAccountDAO.delete(createdUserAccount.getId());
		counter--;
		Assert.assertEquals(counter, userAccountDAO.count());
		assert(userAccountDAO.count() == 0);
	}
	
	@Test
	public void findByCredentialsOnIdentityProviderTest() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		long counter = userAccountDAO.count();
		UserAccount anUserAccount = new UserAccount();
		anUserAccount.setIdentityProvider(identityProvider);
		anUserAccount.setNameIdOnIdentityProvider("jdoe");
		anUserAccount.setEmail("jdoe@prisma.miur.gov");
		anUserAccount.setSuspended(false);
		anUserAccount.setEnabled(false);
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleDAO.findByName(role.getName()));
		anUserAccount.setRoles(roles);
		UserAccount createdUserAccount = userAccountDAO.create(anUserAccount);
		counter++;
		Assert.assertEquals(counter, userAccountDAO.count());
		Assert.assertEquals(createdUserAccount.getId(), userAccountDAO
				.findByCredentialsOnIdentityProvider(identityProvider.getId(), "jdoe").getId());
		userAccountDAO.delete(createdUserAccount.getId());
		counter--;
		Assert.assertEquals(counter, userAccountDAO.count());
		assert(userAccountDAO.count() == 0);
	}
	
	@Test
	public void existsForGivenCredentialsOnIdentityProviderTest() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		long counter = userAccountDAO.count();
		UserAccount anUserAccount = new UserAccount();
		anUserAccount.setIdentityProvider(identityProvider);
		anUserAccount.setNameIdOnIdentityProvider("jdoe");
		anUserAccount.setEmail("jdoe@prisma.miur.gov");
		anUserAccount.setSuspended(false);
		anUserAccount.setEnabled(false);
		Set<Role> roles = new HashSet<Role>();
		roles.add(roleDAO.findByName(role.getName()));
		anUserAccount.setRoles(roles);
		UserAccount createdUserAccount = userAccountDAO.create(anUserAccount);
		counter++;
		Assert.assertEquals(counter, userAccountDAO.count());
		Assert.assertTrue(userAccountDAO
				.existsForGivenCredentialsOnIdentityProvider(identityProvider.getId(), "jdoe"));
		userAccountDAO.delete(createdUserAccount.getId());
		counter--;
		Assert.assertEquals(counter, userAccountDAO.count());
		assert(userAccountDAO.count() == 0);
	}
	
}