package mat.dao.user;

import java.util.List;

import mat.dao.IDAO;
import mat.entity.User;
import mat.entity.UserPasswordHistory;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserPasswordHistoryDAO.
 */
public interface UserPasswordHistoryDAO  extends IDAO<UserPasswordHistory, String> {


	/**
	 * Gets the password history.
	 *
	 * @param userId the user id
	 * @return the password history
	 */
	List<UserPasswordHistory> getPasswordHistory(String userId);

	/**
	 * Adds the update password history.
	 *
	 * @param user the user
	 */
	void addByUpdateUserPasswordHistory(User user);
}
