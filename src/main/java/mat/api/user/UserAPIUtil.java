package mat.api.user;

import org.hibernate.ObjectNotFoundException;

import mat.entity.User;

public class UserAPIUtil {

	public static boolean doesUserExist(User user) {
		try {
			if(user.getFirstName() == null) {
				return false;
			}
		} catch (ObjectNotFoundException e) {
			return false;
		}
		
		return true;
	}
}
