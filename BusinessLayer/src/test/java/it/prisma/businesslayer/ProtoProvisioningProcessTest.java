package it.prisma.businesslayer;

//import it.prisma.businesslayer.orchestrator.config.CustomApplicationScopedProducer;
//
//import java.util.HashMap;
//
//import javax.inject.Inject;
//
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.Archive;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jbpm.test.JbpmJUnitBaseTestCase;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.kie.api.io.ResourceType;
//import org.kie.api.runtime.KieSession;
//import org.kie.api.runtime.manager.RuntimeEngine;
//import org.kie.api.runtime.manager.RuntimeEnvironment;
//import org.kie.api.runtime.manager.RuntimeManager;
//import org.kie.api.runtime.process.ProcessInstance;
//
//@RunWith(Arquillian.class)
public class ProtoProvisioningProcessTest extends JbpmJUnitBaseTestCase {
//
//	@Deployment()
//    public static Archive<?> createDeployment() {
//		
//		Maven.resolver().loadPomFromFile("pom.xml")
//		  .importDependencies(ScopeType.TEST, ScopeType.PROVIDED)
//		  .resolve().withTransitivity().asFile();
//
//		Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies()
//		  .resolve().withTransitivity().asFile();
//		
//		JavaArchive arc = ShrinkWrap.create(JavaArchive.class, "proto-provisioning-process-test.war")
//        	.addPackage("it.prisma.businesslayer")        	
//        	.addPackage("com.google.common.cache.CacheBuilder")
//        	.addPackage("org.jbpm.services.task")
//                .addPackage("org.jbpm.services.task.wih") // work items org.jbpm.services.task.wih
//                .addPackage("org.jbpm.services.task.annotations")
//                .addPackage("org.jbpm.services.task.api")
//                .addPackage("org.jbpm.services.task.impl")
//                .addPackage("org.jbpm.services.task.events")
//                .addPackage("org.jbpm.services.task.exception")
//                .addPackage("org.jbpm.services.task.identity")
//                .addPackage("org.jbpm.services.task.factories")
//                .addPackage("org.jbpm.services.task.internals")
//                .addPackage("org.jbpm.services.task.internals.lifecycle")
//                .addPackage("org.jbpm.services.task.lifecycle.listeners")
//                .addPackage("org.jbpm.services.task.query")
//                .addPackage("org.jbpm.services.task.util")
//                .addPackage("org.jbpm.services.task.commands") // This should not be required here
//                .addPackage("org.jbpm.services.task.deadlines") // deadlines
//                .addPackage("org.jbpm.services.task.deadlines.notifications.impl")
//                .addPackage("org.jbpm.services.task.subtask")
//                .addPackage("org.jbpm.services.task.rule")
//                .addPackage("org.jbpm.services.task.rule.impl")
//
//                .addPackage("org.kie.api.runtime.manager")
//                .addPackage("org.kie.internal.runtime.manager")
//                .addPackage("org.kie.internal.runtime.manager.context")
//                .addPackage("org.kie.internal.runtime.manager.cdi.qualifier")
//                
//                .addPackage("org.jbpm.runtime.manager.impl")
//                .addPackage("org.jbpm.runtime.manager.impl.cdi")                               
//                .addPackage("org.jbpm.runtime.manager.impl.factory")
//                .addPackage("org.jbpm.runtime.manager.impl.jpa")
//                .addPackage("org.jbpm.runtime.manager.impl.manager")
//                .addPackage("org.jbpm.runtime.manager.impl.task")
//                .addPackage("org.jbpm.runtime.manager.impl.tx")
//                
//                .addPackage("org.jbpm.shared.services.api")
//                .addPackage("org.jbpm.shared.services.impl")
//                .addPackage("org.jbpm.shared.services.impl.tx")
//                
//                .addPackage("org.jbpm.kie.services.api")
//                .addPackage("org.jbpm.kie.services.impl")
//                .addPackage("org.jbpm.kie.services.cdi.producer")
//                .addPackage("org.jbpm.kie.services.api.bpmn2")
//                .addPackage("org.jbpm.kie.services.impl.bpmn2")
//                .addPackage("org.jbpm.kie.services.impl.event.listeners")
//                .addPackage("org.jbpm.kie.services.impl.audit")
//                
//                .addPackage("org.jbpm.kie.services.impl.example")
//        	.addPackage("it.prisma.businesslayer.orchestrator.config")
//        	.addAsDirectory("src/main/resources")
//            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//        
//        return arc;
//    }
//	
//	@Inject
//	private CustomApplicationScopedProducer customApplicationScopedProducer;//=new CustomApplicationScopedProducer();
//	
//	//@Inject
//	private RuntimeEnvironment runtimeEnvironment;
//	
//	public ProtoProvisioningProcessTest() {
//
//		// setup data source, enable persistence
//
//		super(false, false);
//
//	}
//
//	@Test
//	public void testProcess() {
//
//		
//		runtimeEnvironment=customApplicationScopedProducer.produceEnvironmentForTest();
//		// create runtime manager with single process - hello.bpmn
//		RuntimeManager runtimeManager = createRuntimeManager(Strategy.SINGLETON, new HashMap<String, ResourceType>(), runtimeEnvironment, "manager");
//		
//		//RuntimeManager runtimeManager = createRuntimeManager("business-processes/services/APPaaS/proto-provisioning.bpmn","business-processes/misc/dummy.bpmn");
//		
//
//		// take RuntimeManager to work with process engine
//
//		RuntimeEngine runtimeEngine = getRuntimeEngine();
//		
//		// get access to KieSession instance
//
//		KieSession ksession = runtimeEngine.getKieSession();
//
//		
//		//ksession.getWorkItemManager().registerWorkItemHandler(
//		// start process
//
//		ProcessInstance processInstance = ksession
//				.startProcess("it.prisma.orchestrator.bp.services.appaas.provisioning");
//
//		// check whether the process instance has completed successfully
//
//		assertProcessInstanceCompleted(processInstance.getId(), ksession);
//
//		// check what nodes have been triggered
//
//		assertNodeTriggered(processInstance.getId(), "StartProcess", "Hello",
//				"EndProcess");
//
//	}
//
}