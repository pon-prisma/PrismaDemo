package it.prisma.dal.dao.paas.services;

//import it.prisma.dal.dao.accounting.IdentityProviderDAO;
//import it.prisma.dal.dao.accounting.OrganizationDAO;
//import it.prisma.dal.dao.accounting.RoleDAO;
//import it.prisma.dal.dao.accounting.UserAccountDAO;
//import it.prisma.dal.dao.accounting.WorkgroupDAO;
//import it.prisma.dal.entities.accounting.IdentityProvider;
//import it.prisma.dal.entities.accounting.Organization;
//import it.prisma.dal.entities.accounting.Role;
//import it.prisma.dal.entities.accounting.UserAccount;
//import it.prisma.dal.entities.accounting.Workgroup;
//import it.prisma.dal.entities.paas.services.PaaSService;
//
//import javax.inject.Inject;
//
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.Archive;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
//import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;

//@RunWith(Arquillian.class)
public class PaaSServiceTest {

//	@Deployment
//	public static Archive<?> createDeployment() {
//		MavenDependencyResolver resolver = DependencyResolvers.use(
//				MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");
//		return ShrinkWrap
//				.create(WebArchive.class, "test.war")
//				.addAsResource("test-persistence.xml",
//						"META-INF/persistence.xml")
//				.addAsWebInfResource("xa-orchestrator-ds.xml")
//				.addPackages(true, "it.prisma.dal")
//				.addAsLibraries(
//						resolver.artifacts("com.mysema.querydsl:querydsl-core",
//								"com.mysema.querydsl:querydsl-jpa",
//								"com.mysema.querydsl:querydsl-apt")
//								.resolveAsFiles())
//				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
//	}
//	
//	// DAO to test
//	@Inject private OrganizationDAO organizationDAO;
//	@Inject private IdentityProviderDAO identityProviderDAO;
//	@Inject private UserAccountDAO userAccountDAO;
//	@Inject private WorkgroupDAO workgroupDAO;
//	@Inject private RoleDAO roleDAO;
//	@Inject private PaaSServiceDAO paaSServiceDAO;
//	
//	private Organization organization;
//	private IdentityProvider identityProvider;
//	private Role role;
//	private UserAccount userAccount;
//	private Workgroup workgroup;
//	
//	// Setup test
//	
//	@Before
//	public void init() {
//		assert(workgroupDAO.count() == 0);
//		assert(userAccountDAO.count() == 0);
//		assert(organizationDAO.count() == 0);
//		assert(identityProviderDAO.count() == 0);
//		assert(roleDAO.count() == 0);
//		
//		Organization anOrganization = new Organization();
//		anOrganization.setName("Moe's Tavern");
//		anOrganization
//				.setDescription("Moe's Tavern, once known as Moe's Bar is the "
//						+ "local tavern in Springfield. The bar is named after its owner, Moe Szyslak.");
//		anOrganization
//				.setLogoUri("http://simpsonswiki.com/wiki/File:Moe%27s_Tavern.png");
//		anOrganization
//				.setWebsiteUri("http://simpsonswiki.com/wiki/Moe's_Tavern");
//		anOrganization.setWebsiteLabel("The Simpsons Wiki: Moe's Tavern");
//		organization = organizationDAO.create(anOrganization);
//		
//		IdentityProvider anIdentityProvider = new IdentityProvider();
//		anIdentityProvider.setEntityId("http://sample.prisma.it/sampleIdp");
//		anIdentityProvider.setOrganization(organization);
//		identityProvider = identityProviderDAO.create(anIdentityProvider);
//		
//		Role aRole = new Role();
//		aRole.setName("PRISMA_USER");
//		aRole.setDescription("PRISMA registered user.");
//		role = roleDAO.create(aRole);
//		
//		UserAccount anUserAccount = new UserAccount();
//		anUserAccount.setId(1L);
//		anUserAccount.setIdentityProvider(anIdentityProvider);
//		anUserAccount.setNameIdOnIdentityProvider("jdoe");
//		anUserAccount.setEmail("jdoe@prisma.miur.gov");
//		anUserAccount.setSuspended(false);
//		anUserAccount.setEnabled(false);
//		userAccount=userAccountDAO.create(anUserAccount);
//		
//		
//		Workgroup aWorkgroup = new Workgroup();
//		aWorkgroup.setId(1L);
//		aWorkgroup.setApproved(true);
//		aWorkgroup.setDescription("Workgroup 1 - Description");
//		aWorkgroup.setLabel("Workgroup 1");
//		aWorkgroup.setOrganization(anOrganization);
//		aWorkgroup.setUserAccountByCreatedByUserAccountId(anUserAccount);
//		workgroup = workgroupDAO.create(aWorkgroup);
//		
//		assert(workgroupDAO.count() == 1);
//		assert(userAccountDAO.count() == 1);
//		assert(organizationDAO.count() == 1);
//		assert(identityProviderDAO.count() == 1);
//		assert(roleDAO.count() == 1);
//	}
//	
//	@After
//	public void clear() {
//		assert(workgroupDAO.count() == 1);
//		assert(userAccountDAO.count() == 1);
//		assert(organizationDAO.count() == 1);
//		assert(identityProviderDAO.count() == 1);
//		assert(roleDAO.count() == 1);
//		
//		identityProviderDAO.delete(identityProvider.getId());
//		organizationDAO.delete(organization.getId());
//		roleDAO.delete(role.getId());
//		workgroupDAO.delete(workgroup.getId());
//		userAccountDAO.delete(userAccount.getId());
//		
//		assert(workgroupDAO.count() == 0);
//		assert(userAccountDAO.count() == 0);
//		assert(organizationDAO.count() == 0);
//		assert(identityProviderDAO.count() == 0);
//		assert(roleDAO.count() == 0);
//	}
//	
//	// Create, update and delete
//	
//	@Test
//	public void createUpdateAndDeleteTest() {
//		assert(workgroupDAO.count() == 1);
//		assert(userAccountDAO.count() == 1);
//		assert(organizationDAO.count() == 1);
//		assert(identityProviderDAO.count() == 1);
//		assert(roleDAO.count() == 1);
//					
//		long counter = paaSServiceDAO.count();
//		
//		PaaSService aPaaSService = new PaaSService();
//		aPaaSService.setDescription("PaaS Service description");
//		aPaaSService.setDomainName("mysvc.prisma.it");
//		aPaaSService.setIaaSFlavor("IaaSFlavor");
//		aPaaSService.setLoadBalancingInstances(1);
//		aPaaSService.setName("My PaaS Service");
//		aPaaSService.setNotificationEmail("user@email.it");
//		aPaaSService.setSecureConnectionEnabled(false);
//		aPaaSService.setType(PaaSService.PaaSServiceType.QueueaaS.toString());
//		aPaaSService.setUserAccount(userAccount);
//		aPaaSService.setWorkgroup(workgroup);
//		
//		PaaSService createdPaaSService = paaSServiceDAO.create(aPaaSService);
//		counter++;
//		
//		//Create check
//		Assert.assertEquals(counter, paaSServiceDAO.count());
//		
//		//Update Check
//		createdPaaSService.setStatus("STOPPED");
//		paaSServiceDAO.update(createdPaaSService);		
//		Assert.assertEquals("STOPPED", paaSServiceDAO.findById(createdPaaSService.getId()).getStatus());
//		
//		//Delete Check
//		paaSServiceDAO.delete(createdPaaSService.getId());
//		counter--;
//		Assert.assertEquals(counter, userAccountDAO.count());
//		
//		assert(paaSServiceDAO.count() == 0);
//	}

}
