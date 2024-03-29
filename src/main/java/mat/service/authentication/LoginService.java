package mat.service.authentication;


import java.util.HashMap;
import java.util.List;

import mat.model.authentication.LoginModel;
import mat.entity.SecurityQuestions;
import mat.entity.UserSecurityQuestion;

import mat.result.ForgottenLoginIDResult;
import mat.result.ForgottenPasswordResult;
import mat.result.LoginResult;
import mat.authentication.SecurityQuestionOptions;


public interface LoginService {
	
	/**
	 * Checks if is valid user.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param oneTimePassword
	 * 		  2 factor one time password/security-code           
	 * @return the login model
	 */
	public LoginModel  isValidUser(String username, String password, String oneTimePassword); 
	
	/**
	 * Forgot password.
	 * 
	 * @param email
	 *            the email
	 * @param securityQuestion
	 *            the security question
	 * @param securityAnswer
	 *            the security answer
	 * @param invalidUserCounter
	 *            the invalid user counter
	 * @return the forgotten password result
	 */
	ForgottenPasswordResult forgotPassword(String email,
										   String securityQuestion, String securityAnswer);
	
	/**
	 * Change password security answers.
	 * 
	 * @param model
	 *            the model
	 * @return the login result
	 */
	LoginResult changePasswordSecurityAnswers(LoginModel model);

    ForgottenPasswordResult forgotPassword(String loginId, String securityQuestion, String securityAnswer, String ipAddress);

    /* (non-Javadoc)
     * {@inheritDoc}
     */
    ForgottenLoginIDResult forgotLoginID(String email, String ipAddress);

    /**
	 * Sign out.
	 */
	public void signOut();
	
	/**
	 * Change temp password.
	 * 
	 * @param email
	 *            the email
	 * @param password
	 *            the password
	 * @return the login model
	 */
	public LoginModel changeTempPassword(String email, String password);
	
	/**
	 * Gets the security question options.
	 * 
	 * @param userid
	 *            the userid
	 * @return the security question options
	 */
	public SecurityQuestionOptions getSecurityQuestionOptions(String userid);
	
	/**
	 * Gets the security questions answers.
	 * 
	 * @param userid
	 *            the userid
	 * @return the security questions answers
	 */
	public List<UserSecurityQuestion> getSecurityQuestionsAnswers(String userid);
	
	/**
	 * Gets the footer ur ls.
	 * 
	 * @return the footer ur ls
	 */
	List<String> getFooterURLs();
	
	/**
	 * Update on sign out.
	 * 
	 * @param userId
	 *            the user id
	 * @param email
	 *            the email
	 * @param activityType
	 *            the activity type
	 * @return the string
	 */
	public String updateOnSignOut(String userId, String email, String activityType);
	
	/**
	 * Gets the security question options for email.
	 * 
	 * @param email
	 *            the email
	 * @return the security question options for email
	 */
	SecurityQuestionOptions getSecurityQuestionOptionsForEmail(String email);
	
	/**
	 * Forgot login id.
	 * 
	 * @param email
	 *            the email
	 * @return the forgotten login id result
	 */
	ForgottenLoginIDResult forgotLoginID(String email);
	
	/**
	 * Gets the security question.
	 * 
	 * @param userid
	 *            the userid
	 * @return the security question
	 */
	String getSecurityQuestion(String userid);
	
	/**
	 * Validate password.
	 * 
	 * @param userID
	 *            the user id
	 * @param enteredPassword
	 *            the entered password
	 * @return the hash map
	 */
	public HashMap<String,String> validatePassword(String userID,String enteredPassword);
	
	/**
	 * Validate password creation date.
	 *
	 * @param userID the user id
	 * @return the hash map
	 */
	public HashMap<String,String> validatePasswordCreationDate(String userID);
	
	/**
	 * Validate new password.
	 *
	 * @param userID the user id
	 * @param newPassword the new password
	 * @return the hash map
	 */
	public HashMap<String, String> validateNewPassword(String userID,
			String newPassword);
	
	/**
	 * Checks if is locked user.
	 * 
	 * @param loginId
	 *            the login id
	 * @return true, if is locked user
	 */
	public boolean isLockedUser(String loginId);
	
	/**
	 * Gets the security questions.
	 * 
	 * @return the security questions
	 */
	public List<SecurityQuestions> getSecurityQuestions();
	
	/**
	 * Checks if is valid password.
	 * 
	 * @param userId
	 *            the user id
	 * @param password
	 *            the password
	 * @return true, if is valid password
	 */
	boolean isValidPassword(String userId, String password);
	

}
