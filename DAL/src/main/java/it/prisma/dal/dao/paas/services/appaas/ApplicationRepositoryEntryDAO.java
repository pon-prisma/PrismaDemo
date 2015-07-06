package it.prisma.dal.dao.paas.services.appaas;

import static it.prisma.dal.entities.paas.services.appaas.QApplicationRepositoryEntry.applicationRepositoryEntry;
import it.prisma.dal.dao.QueryDSLGenericDAO;
import it.prisma.dal.entities.paas.services.appaas.ApplicationRepositoryEntry;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.mysema.query.jpa.impl.JPAQuery;

@Local(ApplicationRepositoryEntryDAO.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class ApplicationRepositoryEntryDAO extends
		QueryDSLGenericDAO<ApplicationRepositoryEntry, Long> {

	public ApplicationRepositoryEntryDAO() {
		super(ApplicationRepositoryEntry.class);
	}

	public Collection<ApplicationRepositoryEntry> findAllPublic() {

		JPAQuery query = super.prepareJPAQuery(applicationRepositoryEntry)// .from(paaSServiceEvent)
				.where(applicationRepositoryEntry.shared.eq(true));

		query.orderBy(applicationRepositoryEntry.id.asc());

		return getListResults(query, applicationRepositoryEntry);
	}

	public Collection<ApplicationRepositoryEntry> findAllPrivate(
			long workgroupId) {

		JPAQuery query = super.prepareJPAQuery(applicationRepositoryEntry)
				.where(applicationRepositoryEntry.shared.eq(false))
				.where(applicationRepositoryEntry.workgroup.id.eq(workgroupId));

		query.orderBy(applicationRepositoryEntry.id.asc());

		return getListResults(query, applicationRepositoryEntry);
	}

}
