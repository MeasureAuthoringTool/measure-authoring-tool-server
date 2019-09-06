package mat.dao.audit.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mat.dao.audit.CQLLibraryAuditLogDAO;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dto.AuditLogDTO;
import mat.dto.SearchHistoryDTO;
import mat.dao.search.GenericDAO;
import mat.dao.user.impl.UserDAOImpl;
import mat.entity.CQLAuditLog;
import mat.model.library.CQLLibrary;
import mat.authentication.LoggedInUserUtil;

@Repository("cqlLibraryAuditLogDAO")
public class CQLLibraryAuditLogDAOImpl extends GenericDAO<CQLAuditLog, String> implements CQLLibraryAuditLogDAO {
	
	private static final String ACTIVITY_TYPE = "activityType";
	private static final String USER_COMMENT = "User Comment";

	@Autowired private UserDAOImpl userDAO;
	
	public CQLLibraryAuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public boolean recordCQLLibraryEvent(CQLLibrary cqlLibrary, String event, String additionalInfo){
		Session session = null;
		boolean result = false;
		try {
			final CQLAuditLog cqlAuditLog = new CQLAuditLog();
			cqlAuditLog.setActivityType(event);
			cqlAuditLog.setTime(new Date());
			cqlAuditLog.setCqlLibrary(cqlLibrary);
			cqlAuditLog.setUserId(userDAO.find(LoggedInUserUtil.getLoggedInUser()).getFullName());
			cqlAuditLog.setAdditionalInfo(additionalInfo);
			session = getSessionFactory().getCurrentSession();
			session.saveOrUpdate(cqlAuditLog);			
			result = true;
		}
		catch (final Exception e) { 
			e.printStackTrace();
		}
    	return result;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.dao.audit.CQLLibraryAuditLogDAO#searchHistory(java.lang.String, int, int, java.util.List)
	 */
	@Override
	public SearchHistoryDTO searchHistory(String cqlId, int startIndex, int numberOfRows,List<String> filterList){

		final SearchHistoryDTO searchHistoryDTO = new SearchHistoryDTO();

		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<AuditLogDTO> query = cb.createQuery(AuditLogDTO.class);
		final Root<CQLAuditLog> root = query.from(CQLAuditLog.class);

		final Predicate predicate = getPredicateForAuditLog(cqlId, filterList, cb, root);

		query.select(cb.construct(
						AuditLogDTO.class, 
						 root.get("id"),
						 root.get(ACTIVITY_TYPE),
						 root.get("userId"),
						 root.get("time"),
						 root.get("additionalInfo")));
		query.where(predicate);
		query.orderBy(cb.desc(root.get("time")));

		final TypedQuery<AuditLogDTO> q = session.createQuery(query);

		if(numberOfRows > 0){
			q.setFirstResult(startIndex);
			q.setMaxResults(numberOfRows);
		}

		final List<AuditLogDTO> logResults = q.getResultList();

		searchHistoryDTO.setLogs(logResults);

		searchHistoryDTO.setLogs(logResults);
		searchHistoryDTO.setTotalResults(logResults.size());
		return searchHistoryDTO; 
	}
	
	
	private Predicate getPredicateForAuditLog(String cqlId, List<String> filterList, CriteriaBuilder cb, Root<CQLAuditLog> root) {
		final Predicate p1 = cb.equal(root.get("cqlLibrary").get("id"), cqlId);
		Predicate p2 = null;
		
		if(CollectionUtils.isNotEmpty(filterList)) {
			final In<String> in = cb.in(root.get(ACTIVITY_TYPE));
			filterList.add(USER_COMMENT);
			filterList.forEach(in::value);
			p2 = cb.not(in);
		} else {
			p2 = cb.notEqual(root.get(ACTIVITY_TYPE), USER_COMMENT);
		}
		
		return cb.and(p1, p2);
	}
		
}
