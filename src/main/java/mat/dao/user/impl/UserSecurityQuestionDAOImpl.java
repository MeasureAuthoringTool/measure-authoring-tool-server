package mat.dao.user.impl;

import mat.dao.user.UserSecurityQuestionDAO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.search.GenericDAO;
import mat.entity.UserSecurityQuestion;

@Repository("userSecurityQuestionDAO")
public class UserSecurityQuestionDAOImpl extends GenericDAO<UserSecurityQuestion, String> implements UserSecurityQuestionDAO {
	public UserSecurityQuestionDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
