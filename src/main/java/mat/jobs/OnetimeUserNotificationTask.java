package mat.jobs;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import mat.dao.MatFlagDAO;
import mat.dao.user.UserDAO;
import mat.entity.MatFlag;
import mat.entity.User;
import mat.service.UserService;
import mat.service.impl.UserServiceImpl;

/**
 * The Class OnetimeUserNotificationTask.
 */
public class OnetimeUserNotificationTask {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(OnetimeUserNotificationTask.class);
	
	private UserDAO userDAO;
	private UserService userService;
	private MatFlagDAO matFlagDAO;
	
	/**
	 * Send onetime user notification emails.
	 */
	public void sendOnetimeUserNotificationEmails(){
		logger.info("Hi there this is OnetimeUserNotificationTask..");
		
		List<User> users = userDAO.find();
		if(doesJobNeedExecution()){
			sendUserLoginIdEmail(users);
			sendUserNewTempPasswordEmail(users);
		}
	}

	/**
	 * Send user login id email.
	 * 
	 * @param users
	 *            the users
	 */
	private void sendUserLoginIdEmail(List<User> users) {
		for(User user:users){
			((UserServiceImpl)userService).notifyUserOfNewAccount(user);
		}
	}
	
	/**
	 * Send user new temp password email.
	 * 
	 * @param users
	 *            the users
	 */
	private void sendUserNewTempPasswordEmail(List<User> users) {
		for(User user:users){
			userService.requestResetLockedPassword(user.getId());
		}
	}
	
	/**
	 * Does job need execution.
	 * 
	 * @return true, if successful
	 */
	private boolean doesJobNeedExecution() {
		boolean result = false;
		
		MatFlag flag = matFlagDAO.find().get(0);
		if(flag.getFlag().equals("0")){
			flag.setFlag("1");
			matFlagDAO.save(flag);
			result=true;
		}
				
		return result;
	}
	
	/**
	 * Gets the user dao.
	 * 
	 * @return the user dao
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * Sets the user dao.
	 * 
	 * @param userDAO
	 *            the new user dao
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * Sets the user service.
	 * 
	 * @param userService
	 *            the new user service
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	public UserService getUserService() {
		return userService;
	}
	
	/**
	 * Gets the mat flag dao.
	 * 
	 * @return the mat flag dao
	 */
	public MatFlagDAO getMatFlagDAO() {
		return matFlagDAO;
	}

	/**
	 * Sets the mat flag dao.
	 * 
	 * @param matFlagDAO
	 *            the new mat flag dao
	 */
	public void setMatFlagDAO(MatFlagDAO matFlagDAO) {
		this.matFlagDAO = matFlagDAO;
	}

}
