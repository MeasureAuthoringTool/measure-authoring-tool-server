package mat.service.authentication;


import mat.model.authentication.SecurityQuestionsModel;
import mat.model.authentication.MyAccountModel;
import mat.result.SaveMyAccountResult;


public interface MyAccountService {
	
	/**
	 * Gets the my account.
	 * 
	 * @return the my account
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	MyAccountModel getMyAccount() throws IllegalArgumentException;
	
	/**
	 * Save my account.
	 * 
	 * @param model
	 *            the model
	 * @return the save my account result
	 */
	SaveMyAccountResult saveMyAccount(MyAccountModel model);
	
	/**
	 * Gets the security questions.
	 * 
	 * @return the security questions
	 */
	SecurityQuestionsModel getSecurityQuestions();
	
	/**
	 * Save security questions.
	 * 
	 * @param model
	 *            the model
	 * @return the save my account result
	 */
	SaveMyAccountResult saveSecurityQuestions(SecurityQuestionsModel model);
	
	/**
	 * Change password.
	 * 
	 * @param password
	 *            the password
	 * @return the save my account result
	 */
	SaveMyAccountResult changePassword(String password);
	//US212
	/**
	 * Sets the user sign in date.
	 * 
	 * @param userid
	 *            the new user sign in date
	 */
	public void setUserSignInDate(String userid);
	
	/**
	 * Sets the user sign out date.
	 * 
	 * @param userid
	 *            the new user sign out date
	 */
	public void setUserSignOutDate(String userid);
	
}
