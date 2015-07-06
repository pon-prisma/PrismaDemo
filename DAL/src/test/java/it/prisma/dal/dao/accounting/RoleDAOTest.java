package it.prisma.dal.dao.accounting;

import it.prisma.dal.entities.accounting.Role;

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
public class RoleDAOTest {
	
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
	
	@Inject private RoleDAO roleDAO;
	
	@Test
	public void createAndDeleteTest() {
		assert(roleDAO.count() == 0);
		long counter = roleDAO.count();
		Role aRole = new Role();
		aRole.setName("PRISMA_ROLE");
		aRole.setDescription("Just a test role");
		Role createdRole = roleDAO.create(aRole);
		counter++;
		Assert.assertEquals(counter, roleDAO.count());
		roleDAO.delete(createdRole.getId());
		counter--;
		Assert.assertEquals(counter, roleDAO.count());
		assert(roleDAO.count() == 0);
	}

}
