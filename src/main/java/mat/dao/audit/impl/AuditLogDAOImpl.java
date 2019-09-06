package mat.dao.audit.impl;

import mat.dao.audit.AuditLogDAO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.entity.AuditLog;

@Repository("auditLogDAO")
public class AuditLogDAOImpl extends GenericDAO<AuditLog, String> implements AuditLogDAO {

	public AuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
}
