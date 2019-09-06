package mat.dao.audit.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.audit.EmailAuditLogDAO;
import mat.dao.search.GenericDAO;
import mat.entity.EmailAuditLog;

@Repository("emailAuditLogDAO")
public class EmailAuditLogDAOImpl extends GenericDAO<EmailAuditLog, String> implements EmailAuditLogDAO {
	
	public EmailAuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
