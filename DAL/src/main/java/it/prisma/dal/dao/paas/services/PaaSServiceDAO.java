package it.prisma.dal.dao.paas.services;

import it.prisma.dal.entities.paas.services.PaaSService;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.QPaaSService;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import com.mysema.query.jpa.impl.JPAQuery;

@Local(PaaSServiceDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class PaaSServiceDAO extends AbstractPaaSServiceDAO<PaaSService, Long> {

	private QPaaSService qPaaSService;

	public PaaSServiceDAO() {
		super(PaaSService.class);
		qPaaSService = QPaaSService.paaSService;
	}

	/**
	 * @param domainName 
	 * @return the PaaSService or null if there is no PaaSService found
	 * domain name. <b> Deleted PaaSServices are ignored </b>
	 */
	public PaaSService findByDomainName(String domainName) {
		if(domainName==null)
			throw new IllegalArgumentException("DomainName cannot be null.");

		JPAQuery query = super.prepareJPAQuery().from(qPaaSService)
				.where(qPaaSService.domainName.equalsIgnoreCase(domainName))
				.where(ignoreDeletedPredicate());

		return query.singleResult(qPaaSService);
	}
	

	/**
	 * @param type
	 * @param appName
	 * @param workgroupId
	 * @return the PaaSService or null if there is no PaaSService found
	 * domain name. <b> Deleted PaaSServices are ignored </b>
	 */
	public PaaSService findByTypeAndNameAndWorkgroup(PaaSServiceType type,
			String appName, Long workgroupId) {
		if(appName==null)
			throw new IllegalArgumentException("ServiceName cannot be null.");

		if (workgroupId == null)
			throw new IllegalArgumentException("WorkgroupId must be > 0.");

		JPAQuery query = super.prepareJPAQuery().from(qPaaSService)
				.where(qPaaSService.name.eq(appName))
				.where(qPaaSService.workgroup.id.eq(workgroupId));

		if (type != null)
			query.where(qPaaSService.type.eq(type.toString()));
		
		query.where(ignoreDeletedPredicate());

		return query.singleResult(qPaaSService);
	}

	/**
	 * @param type
	 * @param userAccountId
	 * @param workgroupId
	 * @param considerDeletedToo specify if the deleted PaaSServices have to be included
	 * @return
	 */
	public Collection<PaaSService> findByTypeAndUserAndWorkgroup(
			PaaSService.PaaSServiceType type, Long userAccountId,
			Long workgroupId, boolean considerDeletedToo) {

		if (!(userAccountId != null || workgroupId != null))
			throw new IllegalArgumentException(
					"Either userAccountId or workgroupId > 0.");

		JPAQuery query = super.prepareJPAQuery().from(qPaaSService);

		if (type != null)
			query.where(qPaaSService.type.eq(type.toString()));

		if (userAccountId != null)
			query.where(qPaaSService.userAccount.id.eq(userAccountId));

		if (workgroupId != null)
			query.where(qPaaSService.workgroup.id.eq(workgroupId));
		
		if (!considerDeletedToo) {
			query.where(ignoreDeletedPredicate());
		}

		return getListResults(query, qPaaSService);
	}

	/**
	 * @param type
	 * @param considerDeletedToo specify if the deleted PaaSServices have to be included
	 * @return
	 */
	public Collection<PaaSService> findByType(PaaSService.PaaSServiceType type, boolean considerDeletedToo) {
		JPAQuery query = super.prepareJPAQuery()
				.from(qPaaSService)
				.where(qPaaSService.type.eq(type.toString()));
		
		if (!considerDeletedToo) {
			query.where(ignoreDeletedPredicate());
		}
		return query.list(qPaaSService);
	}

	/**
	 * @param type
	 * @param considerDeletedToo specify if the deleted PaaSServices have to be included
	 * @return
	 */
	public long countByType(PaaSService.PaaSServiceType type, boolean considerDeletedToo) {
		JPAQuery query = prepareJPAQuery().from(qPaaSService)
				.where(qPaaSService.type.eq(type.toString()));
		if (!considerDeletedToo) {
			query.where(ignoreDeletedPredicate());
		}
		return query.count();
	}
}
