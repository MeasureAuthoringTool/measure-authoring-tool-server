package mat.service.authentication.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import mat.authentication.LoggedInUserUtil;
import mat.authentication.PasswordVerifier;
import mat.authentication.SecurityQuestionVerifier;
import mat.constants.ConstantMessages;
import mat.constants.MessageDelegate;
import mat.dictionary.CheckDictionaryWordInPassword;
import mat.entity.*;
import mat.model.authentication.LoginModel;
import mat.service.*;
import mat.result.LoginResult;
import mat.service.audit.TransactionAuditService;
import mat.service.authentication.LoginCredentialService;
import mat.service.authentication.SecurityQuestionsService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import mat.dao.user.UserDAO;
import mat.dao.user.UserPasswordHistoryDAO;
import mat.result.ForgottenLoginIDResult;
import mat.result.ForgottenPasswordResult;
import mat.authentication.SecurityQuestionOptions;
import mat.service.authentication.LoginService;

@SuppressWarnings("serial")
@Service
public class LoginServiceImpl implements LoginService {
	
	private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class);
	
	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";
	static int AUDIT_LOG_USER_ID_LENGTH = 40;
	
	@Autowired private UserDAO userDAO;
	@Autowired private UserPasswordHistoryDAO userPasswordHistoryDAO;
	@Autowired private UserService userService;	
	@Autowired private LoginCredentialService loginCredentialService;
	@Autowired private TransactionAuditService auditService;
	@Autowired private SecurityQuestionsService securityQuestionsService;
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public SecurityQuestionOptions getSecurityQuestionOptions(String userid) {
		return userService.getSecurityQuestionOptions(userid);
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public String getSecurityQuestion(String userid) {
		return userService.getSecurityQuestion(userid);
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public SecurityQuestionOptions getSecurityQuestionOptionsForEmail(String email) {
		return userService.getSecurityQuestionOptionsForEmail(email);
	}

	@Override
	public ForgottenLoginIDResult forgotLoginID(String email) {
		return null; //TODO implement this
	}


	@Override
	public LoginModel isValidUser(String userId, String password, String oneTimePassword) {
		// Code to create New session ID everytime user log's in. Story Ref - MAT1222
/*		HttpSession session = getThreadLocalRequest().getSession(false);
		if ((session != null) && !session.isNew()) {
			session.invalidate();
		}
		session = getThreadLocalRequest().getSession(true);
		LoginModel loginModel = loginCredentialService.isValidUser(userId, password, oneTimePassword,session.getId());*/
		//return loginModel;
        //TODO handle this
        return null;
	}

	@Override
	public ForgottenPasswordResult forgotPassword(String email, String securityQuestion, String securityAnswer) {
		return null; //TODO implement this
	}

	/* (non-Javadoc)
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValidPassword(String userId, String password) {
		boolean isValid = loginCredentialService.isValidPassword(userId, password);
		return isValid;
	}

	@Override
	public ForgottenPasswordResult forgotPassword(String loginId, String securityQuestion, String securityAnswer, String ipAddress) {
		
		// don't pass invalidUserCounter to server anymore
		ForgottenPasswordResult forgottenPasswordResult = userService.requestForgottenPassword(loginId, securityQuestion, securityAnswer, 1);
		logger.info("Login ID --- " + loginId);
		String truncatedLoginId = loginId;
		if (loginId.length() > AUDIT_LOG_USER_ID_LENGTH) {
			truncatedLoginId = loginId.substring(0, AUDIT_LOG_USER_ID_LENGTH);
		}
		if (forgottenPasswordResult.getFailureReason() > 0) {
			logger.info("Forgot Password Failed ====> CLient IPAddress :: " + ipAddress);
			auditService.recordTransactionEvent(UUID.randomUUID().toString(), null, "FORGOT_PASSWORD_EVENT", 
					truncatedLoginId, "[IP: " + ipAddress + " ]" + "Forgot Password Failed for " + loginId, ConstantMessages.DB_LOG);
		} else {
			logger.info("Forgot Password Success ====> CLient IPAddress :: " + ipAddress);
			auditService.recordTransactionEvent(UUID.randomUUID().toString(), null, "FORGOT_PASSWORD_EVENT", 
					truncatedLoginId, "[IP: " + ipAddress + " ]" + "Forgot Password Success for" + loginId, ConstantMessages.DB_LOG);
		}
		return forgottenPasswordResult;
	}
	
	/* (non-Javadoc)
	 * {@inheritDoc}
	 */
	@Override
	public ForgottenLoginIDResult forgotLoginID(String email, String ipAddress) {
		ForgottenLoginIDResult forgottenLoginIDResult = userService.requestForgottenLoginID(email);
		if (!forgottenLoginIDResult.isEmailSent()) {
			logger.info("CLient IPAddress :: " + ipAddress);
			String message = null;
			String truncatedEmail = email;
			if (email.length() > AUDIT_LOG_USER_ID_LENGTH) {
				truncatedEmail = email.substring(0, AUDIT_LOG_USER_ID_LENGTH);
			}
			if (forgottenLoginIDResult.getFailureReason() == 5) {
				logger.info(" User ID Found and but user already logged in : IP Address Location :" + ipAddress);
				message = MessageDelegate.getInstance().getLoginFailedAlreadyLoggedInMessage();
				// this is to show success message on client side.
				forgottenLoginIDResult.setEmailSent(true);
				// Failure reason un-set : burp suite showing different values
				// in response for invalid user. It should be same for valid and
				// invalid user.
				forgottenLoginIDResult.setFailureReason(0);
				// Illegal activity is logged in Transaction Audit table with IP
				// Address of client requesting for User Id.
				auditService.recordTransactionEvent(UUID.randomUUID().toString(), null, "FORGOT_USER_EVENT", 
						truncatedEmail, "[IP: " + ipAddress + " ]" + "[EMAIL Entered: " + email + " ]" + message, ConstantMessages.DB_LOG);
			} else if (forgottenLoginIDResult.getFailureReason() == 4) {
				message = MessageDelegate.getInstance().getEmailNotFoundMessage();
				logger.info(" User ID : " + email + " Not found in User Table IP Address Location :" + ipAddress);
				// this is to show success message on client side.
				forgottenLoginIDResult.setEmailSent(true);
				// Failure reason un-set : burp suite showing different values
				// in response for invalid user. It should be same for valid and
				// invalid user.
				forgottenLoginIDResult.setFailureReason(0);
				// Illegal activity is logged in Transaction Audit table with IP
				// Address of client requesting for User Id.
				auditService.recordTransactionEvent(UUID.randomUUID().toString(), null, "FORGOT_USER_EVENT", 
						truncatedEmail, "[IP: " + ipAddress + " ]" + "[EMAIL Entered: " + email + " ]" + message, ConstantMessages.DB_LOG);
			}
			
		}
		return forgottenLoginIDResult;
	}
	
	/**
	 * Method to find IP address of Client *.
	 * 
	 * @param request
	 *            the request
	 * @return the client ip addr
	 */
	private String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if ((ip == null) || (ip.length() == 0) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public void signOut() {
		loginCredentialService.signOut();
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public LoginResult changePasswordSecurityAnswers(LoginModel model) {
		LoginModel loginModel = model;
		model.scrubForMarkUp();
		LoginResult result = new LoginResult();
		logger.info("LoggedInUserUtil.getLoggedInLoginId() ::::" + LoggedInUserUtil.getLoggedInLoginId());
		logger.info("loginModel.getPassword()() ::::" + loginModel.getPassword());
		String markupRegExp = "<[^>]+>";
		String noMarkupTextPwd = loginModel.getPassword().trim().replaceAll(markupRegExp, "");
		loginModel.setPassword(noMarkupTextPwd);
		PasswordVerifier verifier = new PasswordVerifier(loginModel.getLoginId(), loginModel.getPassword(), loginModel.getPassword());
		
		if (verifier.isValid()) {
			SecurityQuestionVerifier sverifier = new SecurityQuestionVerifier(
					loginModel.getQuestion1(), loginModel.getQuestion1Answer(),
					loginModel.getQuestion2(), loginModel.getQuestion2Answer(),
					loginModel.getQuestion3(), loginModel.getQuestion3Answer());
			
			if (sverifier.isValid()) {
				String resultMessage = callCheckDictionaryWordInPassword(loginModel.getPassword());
				if (resultMessage.equalsIgnoreCase("SUCCESS")) {
					boolean isSuccessful = loginCredentialService.changePasswordSecurityAnswers(loginModel);
					result.setSuccess(isSuccessful);
				} else {
					logger.info("Server Side Validation Failed in changePasswordSecurityAnswers for User:" + LoggedInUserUtil.getLoggedInUser());
					result.setSuccess(false);
					result.setFailureReason(LoginResult.DICTIONARY_EXCEPTION);
				}
			} else {
				logger.info("Server Side Validation Failed in changePasswordSecurityAnswers for User:" + LoggedInUserUtil.getLoggedInUser());
				result.setSuccess(false);
				result.setMessages(sverifier.getMessages());
				result.setFailureReason(LoginResult.SERVER_SIDE_VALIDATION_SECURITY_QUESTIONS);
			}
			
		} else {
			logger.info("Server Side Validation Failed in changePasswordSecurityAnswers for User:" + LoggedInUserUtil.getLoggedInUser());
			result.setSuccess(false);
			result.setMessages(verifier.getMessages());
			result.setFailureReason(LoginResult.SERVER_SIDE_VALIDATION_PASSWORD);
			
		}
		return result;
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public LoginModel changeTempPassword(String email, String changedpassword) {
		
		LoginModel loginModel = new LoginModel();
		
		String resultMessage = callCheckDictionaryWordInPassword(changedpassword);
		
		if (resultMessage.equalsIgnoreCase("EXCEPTION")) {
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MessageDelegate.getInstance().getGenericErrorMessage());
		} else if (resultMessage.equalsIgnoreCase("SUCCESS")) {
			loginModel = loginCredentialService.changeTempPassword(email, changedpassword);
		} else {
			loginModel.setLoginFailedEvent(true);
			loginModel.setErrorMessage(MessageDelegate.getInstance().getMustNotContainDictionaryWordMessage());
		}
		
		return loginModel;
	}
	
	/* (non-Javadoc)
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getFooterURLs() {
		List<String> footerURLs = userService.getFooterURLs();
		return footerURLs;
	}
	
	/* (non-Javadoc)
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, String> validatePassword(String userID, String enteredPassword) {
		String ifMatched = FAILURE;
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if ((enteredPassword == null) || enteredPassword.equals("")) {
			resultMap.put("message", MessageDelegate.getInstance().getPasswordRequiredErrorMessage());
		} else {
			MatUserDetails userDetails = (MatUserDetails) userDAO.getUser(userID);
			if (userDetails != null) {
				String hashPassword = userService.getPasswordHash(userDetails.getUserPassword().getSalt(), enteredPassword);
				if (hashPassword.equalsIgnoreCase(userDetails.getUserPassword().getPassword())) {
					ifMatched = SUCCESS;
				} else {
					int currentPasswordlockCounter = userDetails.getUserPassword().getPasswordlockCounter();
					logger.info("CurrentPasswordLockCounter value:" + currentPasswordlockCounter);
					if (currentPasswordlockCounter == 2) {
						String resultStr = updateOnSignOut(userDetails.getId(), userDetails.getEmailAddress(), "SIGN_OUT_EVENT");
						if (resultStr.equals(SUCCESS)) {
							Date currentDate = new Date();
							Timestamp currentTimeStamp = new Timestamp(currentDate.getTime());
							userDetails.setSignOutDate(currentTimeStamp);
							userDetails.setSessionId(null);
							userDetails.getUserPassword()
							.setPasswordlockCounter(0);
							userDAO.saveUserDetails(userDetails);
							resultMap.put("message", "REDIRECT");
							logger.info("Locking user out with LOGIN ID ::" + userDetails.getLoginId() + " :: USER ID ::" + userDetails.getId());
						}
					} else {
						resultMap.put("message", MessageDelegate.getInstance().getPasswordMismatchErrorMessage());
						userDetails.getUserPassword().setPasswordlockCounter(currentPasswordlockCounter + 1);
						userDAO.saveUserDetails(userDetails);
					}
					
				}
			}
		}
		resultMap.put("result", ifMatched);
		return resultMap;
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, String> validateNewPassword(String userID,String newPassword) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		MatUserDetails userDetails = (MatUserDetails) userDAO.getUser(userID);
		String ifMatched = FAILURE;
		
		if (userDetails != null) {
			String hashPassword = userService.getPasswordHash(userDetails .getUserPassword().getSalt(), newPassword);
			
			if (hashPassword.equalsIgnoreCase(userDetails.getUserPassword().getPassword())) {
				ifMatched = SUCCESS;
			}

			if(ifMatched.equals(FAILURE)){
				List<UserPasswordHistory> passwordHistory = userPasswordHistoryDAO.getPasswordHistory(userDetails.getId());
				for(int i=0; i<passwordHistory.size(); i++){
					hashPassword = userService.getPasswordHash(passwordHistory.get(i).getSalt(), newPassword);
					if (hashPassword.equalsIgnoreCase(passwordHistory.get(i).getPassword())) {
						ifMatched = SUCCESS;
						break;
					}
				}
			}
		}
		
		resultMap.put("result", ifMatched);
		return resultMap;
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, String> validatePasswordCreationDate(String userID) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		MatUserDetails userDetails = (MatUserDetails) userDAO.getUser(userID);
		String ifMatched = FAILURE;
		Calendar calender = GregorianCalendar.getInstance();
		SimpleDateFormat currentDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = currentDateFormat.format(calender.getTime());
		String passwordCreationDate=currentDateFormat.format(userDetails.getUserPassword().getCreatedDate());
		if (userDetails != null) {
			if(currentDate.equals(passwordCreationDate)){
				ifMatched=SUCCESS;
			}
		}
		resultMap.put("result", ifMatched);
		return resultMap;
	}
	
	/**
	 * Redirect to html page.
	 * 
	 * @param html
	 *            the html
	 */
/*	public void redirectToHtmlPage(String html) {
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		String path = Window.Location.getPath();
		path = path.substring(0, path.lastIndexOf('/'));
		path += html;
		urlBuilder.setPath(path);
		Window.Location.replace(urlBuilder.buildString());
	}*/
	
	/**
	 * Call check dictionary word in password.
	 * 
	 * @param changedpassword
	 *            the changedpassword
	 * @return the string
	 */
	private String callCheckDictionaryWordInPassword(String changedpassword) {
		String returnMessage = FAILURE;
		try {
			boolean result = CheckDictionaryWordInPassword.containsDictionaryWords(changedpassword);
			if (result) {
				returnMessage = SUCCESS;
			}
		} catch (IOException e) {
			returnMessage = "EXCEPTION";
			e.printStackTrace();
		}
		return returnMessage;
		
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public List<UserSecurityQuestion> getSecurityQuestionsAnswers(String userID) {
		User user = userService.getById(userID);
		List<UserSecurityQuestion> secQuestions = new ArrayList<UserSecurityQuestion>(user.getUserSecurityQuestions());
		logger.info("secQuestions Length " + secQuestions.size());
		return secQuestions;
	}

	@Override
	public String updateOnSignOut(String userId, String emailId, String activityType) {
		//TODO implement this?
		//UMLSSessionTicket.remove(getThreadLocalRequest().getSession().getId());
		String resultStr = userService.updateOnSignOut(userId, emailId, activityType);
		SecurityContextHolder.clearContext();
		//TODO implement this?
/*		getThreadLocalRequest().getSession().invalidate();
		logger.info("User Session Invalidated at :::: " + new Date());
		logger.info("In UserServiceImpl Signout Update " + resultStr);*/
		return resultStr;
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLockedUser(String loginId) {
		return userService.isLockedUser(loginId);
	}
	
	/* 
	 * {@inheritDoc}
	 */
	@Override
	public List<SecurityQuestions> getSecurityQuestions() {
		logger.info("Found...." + securityQuestionsService);
		return securityQuestionsService.getSecurityQuestions();
	}
}
