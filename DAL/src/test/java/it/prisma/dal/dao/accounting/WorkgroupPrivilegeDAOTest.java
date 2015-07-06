package it.prisma.dal.dao.accounting;

import it.prisma.dal.entities.accounting.WorkgroupPrivilege;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class WorkgroupPrivilegeDAOTest {
	
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
	
	@Inject WorkgroupPrivilegeDAO workgroupPrivilegeDAO;
	
	@Test
	public void createAndDeleteTest() {
		assert(workgroupPrivilegeDAO.count() == 0);
		long counter = workgroupPrivilegeDAO.count();
		WorkgroupPrivilege aWorkgroupPrivilege = new WorkgroupPrivilege();
		aWorkgroupPrivilege.setName("WG_ADMIN");
		aWorkgroupPrivilege.setDescription("Workgroup Admin");
		counter++;
		WorkgroupPrivilege createdWorkgroupPrivilege = workgroupPrivilegeDAO.create(aWorkgroupPrivilege);
		Assert.assertEquals(counter, workgroupPrivilegeDAO.count());
		counter--;
		workgroupPrivilegeDAO.delete(createdWorkgroupPrivilege.getId());
		Assert.assertEquals(counter, workgroupPrivilegeDAO.count());
		assert(workgroupPrivilegeDAO.count() == 0);
	}
	
	@Test
	public void findByNameTest() {
		assert(workgroupPrivilegeDAO.count() == 0);
		WorkgroupPrivilege aWorkgroupPrivilege = new WorkgroupPrivilege();
		aWorkgroupPrivilege.setName("WG_ADMIN");
		aWorkgroupPrivilege.setDescription("Workgroup Admin");
		WorkgroupPrivilege createdWorkgroupPrivilege = workgroupPrivilegeDAO.create(aWorkgroupPrivilege);
		WorkgroupPrivilege foundPrivilege = workgroupPrivilegeDAO.findByName("WG_ADMIN");
		Assert.assertEquals(createdWorkgroupPrivilege.getId(), foundPrivilege.getId());
		workgroupPrivilegeDAO.delete(createdWorkgroupPrivilege.getId());
		assert(workgroupPrivilegeDAO.count() == 0);
	}

}
