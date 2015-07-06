package it.prisma.businesslayer.bizws.iaas.key;

import it.prisma.businesslayer.bizlib.common.exceptions.BadRequestException;
import it.prisma.businesslayer.bizlib.iaas.key.KeyMgmtBean;
import it.prisma.businesslayer.bizws.BaseWS;
import it.prisma.businesslayer.bizws.config.exceptionhandling.PrismaWrapperException;
import it.prisma.businesslayer.bizws.iaas.OpenstackHelper;
import it.prisma.businesslayer.exceptions.accounting.WorkgroupNotFoundException;
import it.prisma.domain.dsl.exceptions.notfound.PrismaNotFoundException;
import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRepresentation;
import it.prisma.domain.dsl.iaas.openstack.keypairs.KeypairRequest;

import java.util.List;

import javax.inject.Inject;

import org.openstack4j.api.exceptions.OS4JException;

public class KeyWSImpl extends BaseWS implements KeyWS {

	@Inject
	protected KeyMgmtBean keyMngBean;

	@Override
	public List<KeypairRepresentation> listAllKeys(Long workgroupId) {
		try {
			return keyMngBean.getKeyByWorkgroupId(workgroupId);
		} catch (Exception e) {
			throw new PrismaWrapperException(e);
		}
	}

	@Override
	public KeypairRepresentation importKey(KeypairRequest key, Long workgroupId) {
		try{
			return keyMngBean.importKey(key, workgroupId);
		} catch (WorkgroupNotFoundException workgroupNotFoundException){
			throw new BadRequestException("Workgroup Not Found");
		} catch(OS4JException e){
			throw new OpenstackHelper().throwException(e);
		} catch(Exception e){
			throw new PrismaWrapperException(e);
		} 
	}		

	
	@Override
	public KeypairRepresentation generateKey(Long workgroupId, String name) {
		try{
			return keyMngBean.generateKey(workgroupId, name);
		} catch (WorkgroupNotFoundException workgroupNotFoundException){
			throw new BadRequestException("Workgroup Not Found");
		} catch(OS4JException e){
			throw new OpenstackHelper().throwException(e);
		} catch(Exception e){
			throw new PrismaWrapperException(e);
		}
	}

	

	@Override
	public Boolean deleteKey(Long workgroupId, String name) {
		try {
			return keyMngBean.deleteKey(workgroupId, name);
		} catch (PrismaNotFoundException e) {
			throw new PrismaWrapperException(e);
		}
	}
}