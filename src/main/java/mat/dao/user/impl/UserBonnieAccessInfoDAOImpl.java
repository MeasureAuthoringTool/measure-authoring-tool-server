package mat.dao.user.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.user.UserBonnieAccessInfoDAO;
import mat.dao.user.UserDAO;
import mat.dao.search.GenericDAO;
import mat.entity.User;
import mat.entity.UserBonnieAccessInfo;

@Repository("userBonnieAccessInfoDAO")
public class UserBonnieAccessInfoDAOImpl extends GenericDAO<UserBonnieAccessInfo, String> implements UserBonnieAccessInfoDAO{
	
	public UserBonnieAccessInfoDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Autowired
	private UserDAO userDAO;


	@Override
	public UserBonnieAccessInfo getUserBonnieAccessInfo(String userBonnieAccessId) {
		return find(userBonnieAccessId);
	}

	@Override
	public UserBonnieAccessInfo findByUserId(String userID) {
		User user = userDAO.find(userID);
		return user.getUserBonnieAccessInfo();
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
