package it.prisma.dal.dao.accounting;

import it.prisma.dal.entities.organization.Organization;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class OrganizationDAOTest {

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

	// Create and delete

	@Test
	public void createAndDeleteTest() {
		long counter = organizationDAO.count();
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
		Long createdOrganizationId = organizationDAO.create(anOrganization).getId();
		counter++;
		Assert.assertEquals(counter, organizationDAO.count());
		organizationDAO.delete(createdOrganizationId);
		counter--;
		Assert.assertEquals(counter, organizationDAO.count());
	}

	@Test(expected = EJBException.class)
	public void attemptToCreateWithIllegalArguments() {
		// Too long data for this field ...
		Organization anotherOrganization = new Organization();
		anotherOrganization
				.setName("The Drunken Clam can be easily recognized by its animated "
						+ "neon sign, on the roof at the front of the building. It depicts a clam, "
						+ "swigging from a bottle, and patently inebriated, judging by the xx eyes, "
						+ "and the bubbles rising.");
		anotherOrganization.setDescription("The Drunken Clam");
		anotherOrganization
				.setLogoUri("http://img1.wikia.nocookie.net/__cb20091126033853/familyguy/images/a/"
						+ "ac/Drunken_Clam.jpg");
		anotherOrganization
				.setWebsiteUri("http://familyguy.wikia.com/wiki/The_Drunken_Clam");
		anotherOrganization
				.setWebsiteLabel("Family Guy Wiki: The Drunken Clam");
		organizationDAO.create(anotherOrganization);
	}

	// Read

	@Test
	public void findByIdAndExistsTest() {
		Organization anotherOrganization = new Organization();
		anotherOrganization.setName("Moe's Tavern");
		anotherOrganization
				.setDescription("Moe's Tavern, once known as Moe's Bar is the "
						+ "local tavern in Springfield. The bar is named after its owner, Moe Szyslak.");
		anotherOrganization
				.setLogoUri("http://simpsonswiki.com/wiki/File:Moe%27s_Tavern.png");
		anotherOrganization
				.setWebsiteUri("http://simpsonswiki.com/wiki/Moe's_Tavern");
		anotherOrganization.setWebsiteLabel("The Simpsons Wiki: Moe's Tavern");
		Long createdOrganizationId = organizationDAO.create(anotherOrganization).getId();
		Assert.assertTrue(organizationDAO.exists(createdOrganizationId));
		Organization organization = organizationDAO.findById(createdOrganizationId);
		Assert.assertNotNull(organization);
		Assert.assertEquals("Moe's Tavern", organization.getName());
		organizationDAO.delete(createdOrganizationId);
		boolean found = true;
		try {
			Assert.assertFalse(organizationDAO.exists(createdOrganizationId));
			organization = organizationDAO.findById(createdOrganizationId);
		} catch(Exception e) {
			found = false;
		}
		Assert.assertFalse(found);
	}
	
	@Test
	public void testFindAll() {
		Assert.assertEquals(organizationDAO.count(), 0);
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
		organizationDAO.create(anOrganization).getId();
		Organization anotherOrganization = new Organization();
		anotherOrganization.setName("The Drunken Clam");
		anotherOrganization
				.setDescription("The Drunken Clam can be easily recognized by its animated "
						+ "neon sign, on the roof at the front of the building. It depicts a clam, "
						+ "swigging from a bottle, and patently inebriated, judging by the xx eyes, "
						+ "and the bubbles rising.");
		anotherOrganization
				.setLogoUri("http://img1.wikia.nocookie.net/__cb20091126033853/familyguy/images/a/"
						+ "ac/Drunken_Clam.jpg");
		anotherOrganization
				.setWebsiteUri("http://familyguy.wikia.com/wiki/The_Drunken_Clam");
		anotherOrganization
				.setWebsiteLabel("Family Guy Wiki: The Drunken Clam");
		organizationDAO.create(anotherOrganization).getId();
		Assert.assertEquals(2, organizationDAO.findAll().size());
		for (Organization o : organizationDAO.findAll())
			organizationDAO.delete(o.getId());
		boolean found = true;
		Collection<Organization> organizations = null;
		try {
			organizations = organizationDAO.findAll();
		} catch(Exception e) {
			found = false; 
		}
		Assert.assertNull(organizations);
		Assert.assertFalse(found);
	}

	// Update

	@Test
	public void updateTest() {
		Organization newOrganization = new Organization();
		newOrganization.setName("The Drunken Clam");
		newOrganization
		.setDescription("The Drunken Clam can be easily recognized by its animated "
				+ "neon sign, on the roof at the front of the building. It depicts a clam, "
				+ "swigging from a bottle, and patently inebriated, judging by the xx eyes, "
				+ "and the bubbles rising.");
		newOrganization
				.setLogoUri("http://img1.wikia.nocookie.net/__cb20091126033853/familyguy/images/a/"
						+ "ac/Drunken_Clam.jpg");
		newOrganization
				.setWebsiteUri("http://familyguy.wikia.com/wiki/The_Drunken_Clam");
		newOrganization
				.setWebsiteLabel("Family Guy Wiki: The Drunken Clam");
		Organization toUpdateOrganization = organizationDAO
				.findById(organizationDAO.create(newOrganization).getId());
		String newName = "Homer's Tavern";
		String newDescription = "Ok, Homer Simpsons has just been brought the old Moe's Tavern!";
		toUpdateOrganization.setName(newName);
		toUpdateOrganization.setDescription(newDescription);
		Organization updatedOrganization = organizationDAO
				.findById(organizationDAO.update(toUpdateOrganization).getId());
		Assert.assertEquals(newName, updatedOrganization.getName());
		Assert.assertEquals(newDescription,
				updatedOrganization.getDescription());
		organizationDAO.delete(updatedOrganization.getId());	
	}

}
