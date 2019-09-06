package mat.dao.authentication.impl;

import mat.dao.authentication.SecurityRoleDAO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.entity.SecurityRole;

@Repository("securityRoleDAO")
public class SecurityRoleDAOImpl extends GenericDAO<SecurityRole, String> implements SecurityRoleDAO {
	
	public SecurityRoleDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
}
