package it.prisma.dal.dao.accounting;

import it.prisma.dal.entities.accounting.IdentityProvider;
import it.prisma.dal.entities.organization.Organization;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

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
public class IdentityProviderDAOTest {
	
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
						resolver.artifacts(
								"com.mysema.querydsl:querydsl-core",
								"com.mysema.querydsl:querydsl-jpa",
								"com.mysema.querydsl:querydsl-apt")
								.resolveAsFiles())
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	// DAO to test
	@Inject private OrganizationDAO organizationDAO;
	@Inject private IdentityProviderDAO identityProviderDAO;
	
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
		long counter = identityProviderDAO.count();
		IdentityProvider identityProvider = new IdentityProvider();
		identityProvider.setEntityId("http://sample.prisma.it/sampleIdp");
		identityProvider.setOrganization(organization);
		Long identityProviderId = identityProviderDAO.create(identityProvider).getId();
		counter++;
		Assert.assertEquals(counter, identityProviderDAO.count());
		identityProviderDAO.delete(identityProviderId);
		counter--;
		Assert.assertEquals(counter, identityProviderDAO.count());
		assert(identityProviderDAO.count() == 0);
	}
	
	// Read
	
	
	//TODO Verificare se può essere un buon modo di gestire le eccezioni
	@Test(expected=EntityNotFoundException.class)
	public void findByIdAndExistTest() throws Exception {
		assert(organizationDAO.exists(organization.getId()));
		IdentityProvider identityProvider = new IdentityProvider();
		identityProvider.setEntityId("http://sample.prisma.it/sampleIdp");
		identityProvider.setOrganization(organization);
		Long identityProviderId = identityProviderDAO.create(identityProvider).getId();
		Assert.assertTrue(identityProviderDAO.exists(identityProviderId));
		IdentityProvider foundIdentityProvider = identityProviderDAO.findById(identityProviderId);
		Assert.assertEquals("http://sample.prisma.it/sampleIdp", foundIdentityProvider.getEntityId());
		identityProviderDAO.delete(identityProviderId);
		try {
			identityProviderDAO.findById(identityProviderId);	
		} catch (EJBException ejbe) {
			if(ejbe.getCausedByException()!=null)
				throw ejbe.getCausedByException();
			else 
				throw ejbe;
		}		
		assert(identityProviderDAO.count() == 0);
	}
	
//	@Test
//	public void findByEntityIdTest() {
//		IdentityProvider identityProvider = identityProviderDAO.findByEntityId("https://amctv.com/idp");
//		Assert.assertEquals(Long.valueOf(1), identityProvider.getId());
//	}
//	
//	@Test
//	public void findAllTest() {
//		Assert.assertEquals(identityProviderDAO.count(),
//				identityProviderDAO.findAll().size());
//	}
	
}
