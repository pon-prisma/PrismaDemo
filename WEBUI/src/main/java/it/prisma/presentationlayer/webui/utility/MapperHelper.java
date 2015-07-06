package it.prisma.presentationlayer.webui.utility;

import it.prisma.domain.dsl.iaas.vmaas.request.VMDeployRequest;
import it.prisma.domain.dsl.iaas.vmaas.request.VmDetails;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation.BIPentahoConfigurationVariant;
import it.prisma.domain.dsl.paas.services.biaas.BIaaSRepresentation.BIProductType;
import it.prisma.domain.dsl.paas.services.biaas.pentaho.request.PentahoConsoleOnlyDeployDetails;
import it.prisma.domain.dsl.paas.services.biaas.pentaho.request.PentahoServerAndConsoleDeployDetails;
import it.prisma.domain.dsl.paas.services.biaas.pentaho.request.PentahoServerOnlyDeployDetails;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.biaas.request.BIaaSDetails;
import it.prisma.domain.dsl.paas.services.dbaas.request.Backup;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.dbaas.request.DBaaSDetails;
import it.prisma.domain.dsl.paas.services.dbaas.request.ExtraUser;
import it.prisma.domain.dsl.paas.services.generic.request.Account;
import it.prisma.domain.dsl.paas.services.generic.request.Environment;
import it.prisma.domain.dsl.paas.services.generic.request.SecureConnection;
import it.prisma.domain.dsl.paas.services.generic.request.ServiceDetails;
import it.prisma.domain.dsl.paas.services.mqaas.MQaaSDeployRequest;
import it.prisma.domain.dsl.paas.services.mqaas.QueueDetails;
import it.prisma.presentationlayer.webui.datavalidation.forms.DeployForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.iaas.VMForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.DBForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.MQForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.biaas.BIClientForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.biaas.BIForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.biaas.BIServerAndClientForm;
import it.prisma.presentationlayer.webui.datavalidation.forms.paas.biaas.BIServerForm;
import it.prisma.presentationlayer.webui.security.userdetails.PrismaUserDetails;


public class MapperHelper {

	public static BIaaSDeployRequest getDeployRequest(PrismaUserDetails user, BIForm form) {
		BIaaSDeployRequest bi = new BIaaSDeployRequest();
		bi.setAccount(getAccount(user));
		bi.setServiceDetails(getServiceDetails(form));
		
		BIaaSDetails biaasDetails = new BIaaSDetails();
		biaasDetails.setProductType(BIProductType.Pentaho.toString());
		// TODO: mettere la versione a DB
		biaasDetails.setProductVersion("5.1");
		//TODO verificare se i parametri esistono veramente
		biaasDetails.setDiskEncryptionEnabled(false);
		biaasDetails.setDiskSize(1);
		
		

		if(form instanceof BIServerForm){
			biaasDetails.setConfigurationVariant(BIPentahoConfigurationVariant.Server_only.toString());			
			BIServerForm biServerForm = (BIServerForm) form;
			
			PentahoServerOnlyDeployDetails serverDetails = new PentahoServerOnlyDeployDetails();
			serverDetails.setAdminPassword(biServerForm.getRootPassword());

			biaasDetails.setProductSpecificDetails(serverDetails);
			
		} else if(form instanceof BIClientForm) {
			biaasDetails.setConfigurationVariant(BIPentahoConfigurationVariant.Console_only.toString());
			BIClientForm clientForm = (BIClientForm) form;
		
			PentahoConsoleOnlyDeployDetails clientDetails = new PentahoConsoleOnlyDeployDetails();
			clientDetails.setBiServerIP(clientForm.getIp());
			clientDetails.setDesignerUsername(clientForm.getUsername());
			clientDetails.setDesignerPassword(clientForm.getUsernamePassword());
			clientDetails.setUserPublicKey(clientForm.getKey());
		
			biaasDetails.setProductSpecificDetails(clientDetails);

		} else if(form instanceof BIServerAndClientForm) {
			biaasDetails.setConfigurationVariant(BIPentahoConfigurationVariant.Server_w_Console.toString());			

			BIServerAndClientForm detailForm = (BIServerAndClientForm) form;
		
			PentahoServerAndConsoleDeployDetails details = new PentahoServerAndConsoleDeployDetails();
			details.setAdminPassword(detailForm.getRootPassword());
			details.setDesignerUsername(detailForm.getUsername());
			details.setDesignerPassword(detailForm.getRepeatUsernamePassword());

			details.setUserPublicKey(detailForm.getKey());

			biaasDetails.setProductSpecificDetails(details);

		}
		bi.setBIaasDetails(biaasDetails);
		return bi;
	}
	
	public static MQaaSDeployRequest getDeployRequest(PrismaUserDetails user, MQForm form) {
		QueueDetails queueDetails = new QueueDetails();
		queueDetails.setUsername(form.getUsername());
		queueDetails.setPassword(form.getUserPassword());

		MQaaSDeployRequest mq = new MQaaSDeployRequest();
		mq.setAccount(getAccount(user));
		mq.setServiceDetails(getServiceDetails(form));
		mq.setMQaasDetails(queueDetails);
		return mq;
	}
	
	public static VMDeployRequest getDeployRequest(PrismaUserDetails user, VMForm form) {

		VmDetails vmDetails = new VmDetails();
//		vmDetails.setDiskPartition(form.getDiskPartition());
		vmDetails.setImageName(form.getImageName());
		vmDetails.setKey(form.getKey());

		vmDetails.setNetworks(form.getNetwork());
//		vmDetails.setScript(form.getScript());
		vmDetails.setVolume(form.getVolume());

		VMDeployRequest vmDeployRequest = new VMDeployRequest();
		vmDeployRequest.setAccount(getAccount(user));
		vmDeployRequest.setServiceDetails(getServiceDetails(form));
		vmDeployRequest.setVmDetails(vmDetails);
		
		return vmDeployRequest;
	}
	
	public static DBaaSDeployRequest getDeployRequest(PrismaUserDetails user, DBForm form) {
		
		DBaaSDetails dbaas = new DBaaSDetails();
		Backup bk = new Backup();
		bk.setInterval(1);
		dbaas.setBackup(bk);

		dbaas.setDiskEncryptionEnabled(false);

		dbaas.setProductType(form.getDbmsSelect());
		dbaas.setProductVersion("TODO");
		dbaas.setDatabaseName(form.getDatabaseName());
		if (form.getOtherUser() != null) {
			ExtraUser eu = new ExtraUser();
			eu.setUsername(form.getUsername());
			eu.setPassword(form.getUsernamePassword());
			dbaas.setExtraUser(eu);
		}

		dbaas.setRootPassword(form.getRootPassword());
		dbaas.setDiskSize(new Integer(form.getVolume()));

		DBaaSDeployRequest db = new DBaaSDeployRequest();
		db.setAccount(getAccount(user));
		db.setServiceDetails(getServiceDetails(form));
		db.setDBaasDetails(dbaas);
		return db;
		
	}
	
	private static Account getAccount(PrismaUserDetails user){
		Account account = new Account();
		account.setUserId(user.getUserData().getUserAccountId());
		account.setWorkgroupId(user.getActiveWorkgroupMembership().getWorkgroupId());
		return account;
	}
	
	private static ServiceDetails getServiceDetails(DeployForm form){
		/**
		 * Set the Environment
		 */
		Environment env = new Environment();
		env.setAvailabilityZone(form.getZoneSelect());
		env.setIaaSFlavor(form.getFlavorSelect());
		env.setQos(form.getQosSelect());

		/**
		 * Set Service Details
		 */
		ServiceDetails svcDet = new ServiceDetails();
		svcDet.setEnvironment(env);
		svcDet.setName(form.getServiceName());
		svcDet.setDescription(form.getDescription());
		svcDet.setDomainName(form.getUrl() + form.getDomainName());
		//TODO 
		svcDet.setLoadBalancingInstances(1);
		svcDet.setNotificationEmail(form.getNotificationEmail());
		svcDet.setPublicVisibility(true);

		SecureConnection secureConnection = new SecureConnection();
		secureConnection.setCertificatePath("TODO");
		svcDet.setSecureConnection(secureConnection);
		
		svcDet.setEnvironment(env);

		return svcDet;
	}
}
