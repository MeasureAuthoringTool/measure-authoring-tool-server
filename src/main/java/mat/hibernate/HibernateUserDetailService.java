package mat.hibernate;


import mat.dao.user.UserDAO;
import mat.entity.MatUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class HibernateUserDetailService implements UserDetailsService {
	@Autowired
	private UserDAO userDAO;

	public UserDetails loadUserByUsername(String userId) {
		UserDetails userDetails = userDAO.getUser(userId);
		return userDetails;
	}

    public void saveUserDetails(MatUserDetails userdetails){
    	 userDAO.saveUserDetails(userdetails);
    }
}


