package it.prisma.businesslayer.bizlib.iaas.key;

import it.prisma.businesslayer.bizlib.common.exceptions.ResourceNotFoundException;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.bizws.iaas.OpenstackHelper;
import it.prisma.businesslayer.exceptions.accounting.WorkgroupNotFoundException;
import it.prisma.dal.dao.accounting.WorkgroupDAO;
import it.prisma.dal.dao.iaas.services.IaaSKeyDAO;
import it.prisma.dal.entities.accounting.Workgroup;
import it.prisma.dal.entities.iaas.services.IaaSKey;
import it.prisma.domain.dsl.exceptions.notfound.PrismaNotFoundException;
import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRepresentation;
import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.api.exceptions.OS4JException;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.Keypair;

public class KeyMgmtBean {

	protected static Logger LOG = LogManager.getLogger(KeyMgmtBean.class);

	@Inject
	private OpenstackHelper openstackHelper;

	@Inject
	private IaaSKeyDAO iaaSKeyDAO;

	@Inject
	WorkgroupDAO workgroupDAO;

	public List<KeypairRepresentation> getKeyByWorkgroupId(Long workgroupId) {

		Workgroup workgroup = workgroupDAO.findById(workgroupId);
		if (workgroup == null)
			throw new ResourceNotFoundException(Workgroup.class, "workgroupId="+workgroupId);

		Collection<IaaSKey> list = iaaSKeyDAO.findByWorkgroupId(workgroupId);
		List<KeypairRepresentation> keys = new ArrayList<KeypairRepresentation>();
		for (IaaSKey k : list) {
			keys.add(new KeypairRepresentation(k.getName(), k.getPublicKey(), k
					.getFingerprint(), null));
		}
		return keys;
	}

	public KeypairRepresentation importKey(KeypairRequest key, Long workgroupId) {

		// Test if workgroup exists
		Workgroup workgroup = workgroupDAO.findById(workgroupId);
		if (workgroup == null)
			throw new WorkgroupNotFoundException();

		// Import KeyPair on Openstack
		Keypair keypair = openstackHelper.getOSClient(workgroupId).compute().keypairs()
				.create(key.getName(), key.getPublicKey());

		// Insert into DB
		try {
			iaaSKeyDAO.create(new IaaSKey(keypair.getName(), keypair
					.getFingerprint(), keypair.getPublicKey(), workgroup));
		} catch (Exception e) {
			// Rollback on openstack
			openstackHelper.getOSClient(workgroupId).compute().keypairs()
					.delete(key.getName());
			throw new PrismaWrapperException(e);
		}
		return new KeypairRepresentation(keypair.getName(),
				keypair.getPublicKey(), keypair.getFingerprint(), null);

	}

	public KeypairRepresentation generateKey(Long workgroupId, String name) {

		// Test if workgroup exists
		Workgroup workgroup = workgroupDAO.findById(workgroupId);
		if (workgroup == null)
			throw new WorkgroupNotFoundException();

		// Create KeyPair on Openstack
		Keypair keypair = openstackHelper.getOSClient(workgroupId).compute().keypairs()
				.create(name, null);

		// Insert into DB
		try {
			iaaSKeyDAO.create(new IaaSKey(keypair.getName(), keypair
					.getFingerprint(), keypair.getPublicKey(), workgroup));
		} catch (Exception e) {
			// Rollback on openstack
			openstackHelper.getOSClient(workgroupId).compute().keypairs()
					.delete(keypair.getName());
			throw new PrismaWrapperException(e);
		}

		return new KeypairRepresentation(keypair.getName(),
				keypair.getPublicKey(), keypair.getFingerprint(),
				keypair.getPrivateKey());

	}

	public Boolean deleteKey(Long workgroupId, String name)
			throws PrismaNotFoundException, PrismaWrapperException {

		try {
			// Test if workgroup exists
			Workgroup workgroup = workgroupDAO.findById(workgroupId);
			if (workgroup == null)
				throw new WorkgroupNotFoundException();

			// Check if exist
			IaaSKey k = iaaSKeyDAO.findByName(workgroupId, name);
			if (k == null)
				throw new PrismaNotFoundException("Key not found");

			ActionResponse response = openstackHelper.getOSClient(workgroupId).compute()
					.keypairs().delete(name);

			if (response.isSuccess()) {
				// Delete form DB
				iaaSKeyDAO.delete(k.getId());
				return true;
			}
			return false;

		} catch (OS4JException OSException) {
			throw new OpenstackHelper().throwException(OSException);
		}
		// } catch (Exception e) {
		// throw new PrismaWrapperException(e);
		// }
	}

}