package it.prisma.dal.dao.accounting;

import it.prisma.dal.entities.accounting.IdentityProvider;
import it.prisma.dal.entities.accounting.Role;
import it.prisma.dal.entities.accounting.UserAccount;
import it.prisma.dal.entities.accounting.UserAccountConfirmation;
import it.prisma.dal.entities.organization.Organization;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
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
public class UserAccountConfirmationTest {

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
	@Inject private UserAccountConfirmationDAO userAccountConfirmationDAO;
	@Inject private RoleDAO roleDAO;
	
	private UserAccount userAccount;
	private Organization organization;
	private IdentityProvider identityProvider;
	private Role role;
	
	// Setup test
	
	@Before
	public void init() {
		assert(organizationDAO.count() == 0);
		assert(identityProviderDAO.count() == 0);
		assert(roleDAO.count() == 0);
		assert(userAccountDAO.count() == 0);
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
		assert(userAccountDAO.count() == 1);
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
	}
	
	@After
	public void clear() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		assert(userAccountDAO.count() == 1);
		Set<Role> roles = new HashSet<Role>();
		roles.clear();
		userAccount.setRoles(roles);
		userAccountDAO.update(userAccount);
		userAccountDAO.delete(userAccount.getId());
		roleDAO.delete(role.getId());
		identityProviderDAO.delete(identityProvider.getId());
		organizationDAO.delete(organization.getId());
		assert(roleDAO.count() == 0);
		assert(organizationDAO.count() == 0);
		assert(identityProviderDAO.count() == 0);
		assert(userAccountDAO.count() == 0);
	}
	
	@Test
	public void createAndDeleteTest() {
		assert(organizationDAO.count() == 1);
		assert(identityProviderDAO.count() == 1);
		assert(roleDAO.count() == 1);
		assert(userAccountDAO.count() == 1);
		assert(userAccountConfirmationDAO.count() == 0);
		long counter = userAccountConfirmationDAO.count();
		Date currentDate = new Date();
		UserAccountConfirmation userAccountConfirmation = new UserAccountConfirmation(
				userAccount, this.generateToken(), true, currentDate, addDelay(
						currentDate, 48));
		UserAccountConfirmation createdUserAccountConfirmation = userAccountConfirmationDAO.create(userAccountConfirmation);
		counter++;
		Assert.assertEquals(counter, userAccountConfirmationDAO.count());
		userAccountConfirmationDAO.delete(createdUserAccountConfirmation.getId());
		counter--;
		Assert.assertEquals(counter, userAccountConfirmationDAO.count());
		assert(userAccountConfirmationDAO.count() == 0);
	}
	
	// Helper
	private String generateToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
	
	private Date addDelay(Date date, int hours) {
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.HOUR_OF_DAY, hours);
	    return cal.getTime();
	}
	
}
