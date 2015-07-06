package it.prisma.dal.dao.accounting;

import static it.prisma.dal.entities.accounting.QAuthToken.authToken;
import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.accounting.AuthToken;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(AuthTokenDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class AuthTokenDAO extends QueryDSLGenericDAO<AuthToken, String> {
	
	static final Logger LOG = LogManager.getLogger(AuthTokenDAO.class
			.getName());
	public AuthTokenDAO() {
		super(AuthToken.class);
	}

	/**
	 * 
	 * @param userAccountId
	 * @return
	 */
	public Collection<AuthToken> findByUser(Long userAccountId) {
		byte isSession=0;
		Date date = new Date();
		JPAQuery query = super.prepareJPAQuery(authToken)//
				.where(authToken.userAccount.id.eq(userAccountId),authToken.expiresAt.after(date), authToken.session.eq(isSession));
		return getListResults(query, authToken);
	}
	
	/**
	 * 
	 * @param userAccountId
	 * @return first session token or null
	 */
	public AuthToken findSessionTokenByUser(Long userAccountId) {
		byte isSession=1;
		Date date = new Date();
		JPAQuery query = super.prepareJPAQuery(authToken)//
				.where(authToken.userAccount.id.eq(userAccountId),authToken.expiresAt.after(date),authToken.session.eq(isSession));
			return (AuthToken) query.singleResult(authToken);
	}
	
//	@Schedule(dayOfWeek="Tue", hour="15", minute="55") 
	@Schedule
	public void revokeTokenSchedule(){
		Date date = new Date();
		JPAQuery query = super.prepareJPAQuery(authToken)
				.where(authToken.expiresAt.before(date));
		deleteAll(getListResults(query, authToken));
		LOG.info("Eliminazione schedulata dei token scaduti effettuata");
	}

}
