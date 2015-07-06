package it.prisma.dal.dao.accounting;

import it.prisma.dal.entities.organization.Organization;
import it.prisma.dal.entities.organization.OrganizationReferent;

import java.util.List;

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
public class OrganizationReferentDAOTest {

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
	@Inject private OrganizationReferentDAO organizationReferentDAO;
	
	private Organization organization;
	
	// Setup test
	
	@Before
	public void init() {
		assert(organizationDAO.count() == 0);
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
		assert(organizationDAO.count() == 1);
	}
	
	@After
	public void clear() {
		assert(organizationDAO.count() == 1);
		organizationDAO.delete(organization.getId());
		assert(organizationDAO.count() == 0);
	}
	
	// Create
	
	@Test
	public void createAndDeleteTest() {
		assert(organizationDAO.exists(organization.getId()));
		long counter = organizationReferentDAO.count();
		OrganizationReferent organizationReferent = new OrganizationReferent();
		organizationReferent.setFirstName("Mike");
		organizationReferent.setLastName("Ehrmantraut");
		organizationReferent.setEmail("mehrmantraut@lospolloshermanos.bb");
		organizationReferent.setPhone("+1 000 000 0000");
		organizationReferent.setOrganization(organization);
		OrganizationReferent createdOrganizationReferent = organizationReferentDAO.create(organizationReferent);
		Assert.assertEquals(createdOrganizationReferent.getOrganization(), organization);
		counter++;
		Assert.assertEquals(counter, organizationReferentDAO.count());
		organizationReferentDAO.delete(createdOrganizationReferent.getId());
		counter--;
		Assert.assertEquals(counter, organizationReferentDAO.count());
		Assert.assertEquals(0, organizationReferentDAO.count());
	}
	
	// Read
	
	@Test
	public void findByIdAndExistsTest() {
		assert(organizationDAO.exists(organization.getId()));
		OrganizationReferent organizationReferent = new OrganizationReferent();
		organizationReferent.setFirstName("Mike");
		organizationReferent.setLastName("Ehrmantraut");
		organizationReferent.setEmail("mehrmantraut@lospolloshermanos.bb");
		organizationReferent.setPhone("+1 000 000 0000");
		organizationReferent.setOrganization(organization);
		Long organizationReferentId = organizationReferentDAO.create(organizationReferent).getId();
		OrganizationReferent foundOrganizationReferent 
			= organizationReferentDAO.findById(organizationReferentId);
		Assert.assertEquals(foundOrganizationReferent.getFirstName(), "Mike");
		organizationReferentDAO.delete(organizationReferentId);
		Assert.assertFalse(organizationReferentDAO.exists(organizationReferentId));
		boolean found = true;
		try {
			organizationReferentDAO.findById(organizationReferentId);
		} catch(Exception e) {
			found = false;
		}
		Assert.assertFalse(found);
		Assert.assertEquals(0, organizationReferentDAO.count());
	}
	
	@Test
	public void findByLastNameTest() {
		assert(organizationDAO.exists(organization.getId()));
		OrganizationReferent organizationReferent = new OrganizationReferent();
		organizationReferent.setFirstName("Mike");
		organizationReferent.setLastName("Ehrmantraut");
		organizationReferent.setEmail("mehrmantraut@lospolloshermanos.bb");
		organizationReferent.setPhone("+1 000 000 0000");
		organizationReferent.setOrganization(organization);
		Long organizationReferentId = organizationReferentDAO.create(organizationReferent).getId();
		List<OrganizationReferent> organizationReferents 
			= (List<OrganizationReferent>) organizationReferentDAO
				.findByLastName("Ehrmantraut");
		Assert.assertEquals(1, organizationReferents.size());
		organizationReferentDAO.delete(organizationReferentId);
		Assert.assertFalse(organizationReferentDAO.exists(organizationReferentId));
		boolean found = true;
		try {
			organizationReferentDAO.findByLastName("Ehrmantraut");
		} catch(Exception e) {
			found = false;
		}
		Assert.assertFalse(found);
		Assert.assertEquals(0, organizationReferentDAO.count());
	}
	
	@Test
	public void findAllTest() {
		assert(organizationDAO.exists(organization.getId()));
		OrganizationReferent organizationReferent = new OrganizationReferent();
		organizationReferent.setFirstName("Mike");
		organizationReferent.setLastName("Ehrmantraut");
		organizationReferent.setEmail("mehrmantraut@lospolloshermanos.bb");
		organizationReferent.setPhone("+1 000 000 0000");
		organizationReferent.setOrganization(organization);
		organizationReferentDAO.create(organizationReferent);
		Assert.assertEquals(1, organizationReferentDAO.findAll().size());
		OrganizationReferent anotherOrganizationReferent = new OrganizationReferent();
		anotherOrganizationReferent.setFirstName("Gus");
		anotherOrganizationReferent.setLastName("Fring");
		anotherOrganizationReferent.setEmail("gfring@lospolloshermanos.bb");
		anotherOrganizationReferent.setPhone("+1 000 000 0000");
		anotherOrganizationReferent.setOrganization(organization);
		organizationReferentDAO.create(anotherOrganizationReferent);
		Assert.assertEquals(2, organizationReferentDAO.findAll().size());
		for (OrganizationReferent o : organizationReferentDAO.findAll())
			organizationReferentDAO.delete(o.getId());
		boolean found = true;
		try {
			organizationReferentDAO.findAll();
		} catch(Exception e) {
			found = false;
		}
		Assert.assertFalse(found);
		Assert.assertEquals(0, organizationReferentDAO.count());
	}
	
}
