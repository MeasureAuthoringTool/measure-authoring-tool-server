package mat.dao.audit.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.audit.TransactionAuditLogDAO;
import mat.dao.search.GenericDAO;
import mat.entity.TransactionAuditLog;

@Repository("transactionAuditLogDAO")
public class TransactionAuditLogDAOImpl extends GenericDAO<TransactionAuditLog, String> implements TransactionAuditLogDAO{

	public TransactionAuditLogDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
