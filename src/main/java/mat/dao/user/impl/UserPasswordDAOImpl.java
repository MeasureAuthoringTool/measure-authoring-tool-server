package mat.dao.user.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.user.UserPasswordDAO;
import mat.dao.search.GenericDAO;
import mat.entity.UserPassword;

@Repository("userPasswordDAO")
public class UserPasswordDAOImpl extends GenericDAO<UserPassword, String> implements UserPasswordDAO {
	
	public UserPasswordDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
}
