package it.prisma.dal.dao.paas.services.appaas;

import it.prisma.dal.dao.paas.services.AbstractPaaSServiceDAO;
import it.prisma.dal.entities.paas.services.PaaSService.PaaSServiceType;
import it.prisma.dal.entities.paas.services.QPaaSService;
import it.prisma.dal.entities.paas.services.appaas.AppaaSEnvironment;
import it.prisma.dal.entities.paas.services.appaas.QAppaaSEnvironment;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.mysema.query.jpa.impl.JPAQuery;

/**
 * APPaaSEnvironmentDAO class which implements the DAO for the
 * {@link APPaaSEnvironment} class.
 */

@Local(APPaaSEnvironmentDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class APPaaSEnvironmentDAO extends AbstractPaaSServiceDAO<AppaaSEnvironment, Long> {

	protected QAppaaSEnvironment qAppaaSEnvironment;

	public APPaaSEnvironmentDAO() {
		super(AppaaSEnvironment.class);
		qAppaaSEnvironment = QAppaaSEnvironment.appaaSEnvironment;
	}

	/**
	 * @param appId
	 * @param considerDeletedToo specify if the deleted AppaaSEnvironment have to be included
	 * @return
	 */
	public Collection<AppaaSEnvironment> findByAppId(Long appId, boolean considerDeletedToo) {
		if (appId == null)
			throw new NullPointerException("AppId cannot be null");

		JPAQuery query = super.prepareJPAQuery(qAppaaSEnvironment).where(qAppaaSEnvironment.appaaS.id.eq(appId));
		
		if (!considerDeletedToo) {
			query.join(qAppaaSEnvironment.paaSService, QPaaSService.paaSService)
			.where(ignoreDeletedPredicate());
		}
		
		return getListResults(query, qAppaaSEnvironment);		
	}

	/**
	 * @param envName
	 * @param appId
	 * @return the AppaaSEnvironment <b> Deleted AppaaSEnvironment are ignored </b>
	 */
	public AppaaSEnvironment findByNameAndAppName(String envName, Long appId) {
		if (appId == null)
			throw new NullPointerException("AppId cannot be null");
		if (envName == null)
			throw new NullPointerException("EnvName cannot be null");

		AppaaSEnvironment appaasEnv = super
				.prepareJPAQuery()
				.from(qAppaaSEnvironment)
				.join(qAppaaSEnvironment.paaSService, QPaaSService.paaSService)
				.where(QPaaSService.paaSService.type
						.eq(PaaSServiceType.APPaaSEnvironment.toString())
						.and(QPaaSService.paaSService.name.eq(envName))
						.and(qAppaaSEnvironment.appaaS.id.eq(appId)))
				.where(ignoreDeletedPredicate())		
				.uniqueResult(qAppaaSEnvironment);
		// if (appaasEnv == null)
		// throw new EntityNotFoundException(
		// "No APPaaSEnvironment entry found for name <" + envName
		// + "> and app <" + appId + ">");
		return appaasEnv;
	}
	
	/**
	 * @param envName
	 * @return the AppaaSEnvironment <b> Deleted AppaaSEnvironment are ignored </b>
	 */
	public AppaaSEnvironment findByName(String envName) {
		if (envName == null)
			throw new NullPointerException("EnvName cannot be null");

		AppaaSEnvironment appaasEnv = super
				.prepareJPAQuery()
				.from(qAppaaSEnvironment)
				.join(qAppaaSEnvironment.paaSService, QPaaSService.paaSService)
				.where(QPaaSService.paaSService.type
						.eq(PaaSServiceType.APPaaSEnvironment.toString())
						.and(QPaaSService.paaSService.name.eq(envName)))
				.where(ignoreDeletedPredicate())
				.uniqueResult(qAppaaSEnvironment);

		return appaasEnv;
	}
}
