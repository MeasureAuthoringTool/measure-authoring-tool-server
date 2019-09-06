package mat.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import mat.service.BonnieService;
import mat.bonnie.result.BonnieOAuthResult;
import mat.umls.VsacTicketInformation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import mat.dao.user.UserBonnieAccessInfoDAO;
import mat.dao.user.UserDAO;
import mat.dao.measure.MeasureDAO;
import mat.entity.User;
import mat.entity.UserBonnieAccessInfo;
import mat.model.measure.Measure;
import mat.authentication.LoggedInUserUtil;
import mat.bonnie.api.BonnieAPI;
import mat.bonnie.api.BonnieAPIv1;
import mat.bonnie.api.result.BonnieCalculatedResult;
import mat.bonnie.api.result.BonnieMeasureResult;
import mat.export.ExportResult;
import mat.authentication.EncryptDecryptToken;
import mat.service.SimpleEMeasureService;
import mat.error.bonnie.BonnieAlreadyExistsException;
import mat.error.bonnie.BonnieBadParameterException;
import mat.error.bonnie.BonnieDoesNotExistException;
import mat.error.bonnie.BonnieNotFoundException;
import mat.error.bonnie.BonnieServerException;
import mat.error.bonnie.BonnieUnauthorizedException;
import mat.error.bonnie.UMLSNotActiveException;
import mat.bonnie.result.BonnieUserInformationResult;

@SuppressWarnings("serial")
@Service
public class BonnieServiceImpl implements BonnieService {
	private static final Logger logger = LogManager.getLogger(EncryptDecryptToken.class);
	
	@Autowired
	private BonnieAPIv1 bonnieApi;
	
	@Autowired
	private EncryptDecryptToken encryptDecryptToken;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private UserBonnieAccessInfoDAO userBonnieAccessInfoDAO;
	
	@Autowired
	private MeasureDAO measureDAO;
	
	@Autowired
	@Qualifier("simpleEMeasureServiceImpl")
	private SimpleEMeasureService simpleEMeasureService;

	@Override
	public String getBonnieAccessLink() {
		String baseURL = bonnieApi.getBonnieBaseURL();
		String responseType = bonnieApi.getResponseType();
		String clientId = bonnieApi.getClientId();
		String redirectURI = bonnieApi.getRedirectURI();

		if (redirectURI == null && clientId == null && responseType == null) {
			return "";
		} else {
			return baseURL + "/oauth/authorize?response_type=" + responseType + "&client_id=" + clientId
					+ "&redirect_uri=" + redirectURI;
		}
	}

	@Override
	public BonnieOAuthResult exchangeCodeForTokens(String code) {
		BonnieOAuthResult result = null;
		try {
			result = bonnieApi.getBonnieOAuthResult(code);
			saveBonnieAccessInfo(LoggedInUserUtil.getLoggedInUser(),result);

		} catch (Exception exn) {
			exn.printStackTrace();
			return result;
		}

		return result;
	}
	
	private UserBonnieAccessInfo getUserBonnieAccessInfo(String userId) throws BonnieUnauthorizedException {
		UserBonnieAccessInfo userInformation = null;
		try {
			userInformation = validateOrRefreshBonnieTokensForUser(userId);
		} catch (BonnieUnauthorizedException e) {
			deleteUserBonnieAccessInfo(userInformation);
			throw e;
		}
		return userInformation;
	}
	
	@Override
	public Boolean updateOrUploadMeasureToBonnie(String measureId, String userId, VsacTicketInformation vsacTicket) throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieDoesNotExistException, BonnieServerException, IOException, BonnieAlreadyExistsException, UMLSNotActiveException {
		Boolean isInitialBonnieUpload = null;
		UserBonnieAccessInfo userInformation = getUserBonnieAccessInfo(userId);
		String userAccessToken = userInformation.getAccessToken();
		
		Measure measure = measureDAO.find(measureId);
		String measureSetId = measure.getMeasureSet().getId();
		BonnieMeasureResult bonnieMeasureResults = null;
		try {
			bonnieMeasureResults = bonnieApi.getMeasureById(userAccessToken, measureSetId);
		} catch (BonnieUnauthorizedException e) {
			deleteUserBonnieAccessInfo(userInformation);
			throw e;
		}
		
		Date exportDate = new Date();
		ExportResult export = null;
		try {
			export = simpleEMeasureService.getEMeasureZIP(measureId, exportDate);
		} catch (Exception e1) {
			throw new BonnieServerException();
		}
		
		String currentReleaseVersion = StringUtils.replace(measure.getReleaseVersion(), ".", "_");
		
		byte[] zipFileContents = export.zipbarr;
		String fileName = export.measureName + "_" + currentReleaseVersion + ".zip";
		String calculationType = measure.getPatientBased() ? "patient" : "episode";
		String vsacTicketGrantingTicket = vsacTicket.getTicket();
		String vsacTicketExpiration = String.valueOf(vsacTicket.getTimeout().getTime());
		
		
		if(bonnieMeasureResults != null && bonnieMeasureResults.getMeasureExsists()) {
			bonnieApi.updateMeasureInBonnie(userAccessToken, measureSetId, zipFileContents, fileName, null, calculationType, vsacTicketGrantingTicket, vsacTicketExpiration);
			isInitialBonnieUpload = false;
		} else {
			bonnieApi.uploadMeasureToBonnie(userAccessToken, zipFileContents, fileName, null, calculationType, vsacTicketGrantingTicket, vsacTicketExpiration);
			isInitialBonnieUpload = true;
		}
		
		return isInitialBonnieUpload;
		
	}
	
	public BonnieCalculatedResult getBonnieExportForMeasure(String userId, String measureId) throws IOException, BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException, BonnieBadParameterException, BonnieDoesNotExistException{
		UserBonnieAccessInfo userInformation = getUserBonnieAccessInfo(userId);
		BonnieCalculatedResult caluclatedResult = null;
		try {
			caluclatedResult = bonnieApi.getCalculatedResultsForMeasure(userInformation.getAccessToken(), measureId);
		} catch (BonnieUnauthorizedException e) {
			deleteUserBonnieAccessInfo(userInformation);
			throw e;
		} catch (BonnieNotFoundException e) {
			throw e;
		} catch (BonnieServerException e) {
			throw e;
		} catch (BonnieBadParameterException e) {
			throw e;
		} catch (BonnieDoesNotExistException e) {
			throw e;
		}
		return caluclatedResult;
	}
	
	private BonnieOAuthResult refreshBonnieTokens(String userId) throws BonnieUnauthorizedException {
		BonnieOAuthResult result = null;
		User user = userDAO.find(userId);
		try {
			user.getUserBonnieAccessInfo().setRefreshToken(user.getUserBonnieAccessInfo().getRefreshToken());
			result = bonnieApi.getBonnieRefreshResult(user.getUserBonnieAccessInfo());
		} catch (BonnieUnauthorizedException e) {
			deleteUserBonnieAccessInfo(user.getUserBonnieAccessInfo());
			throw e;
		}
		
		saveBonnieAccessInfo(userId,result);

		return result;
	}

	private void saveBonnieAccessInfo(String userId, BonnieOAuthResult result) {
		User user = userDAO.find(userId);
		UserBonnieAccessInfo userBonnieAccessInfo = user.getUserBonnieAccessInfo();
		if (userBonnieAccessInfo == null) {
			userBonnieAccessInfo = new UserBonnieAccessInfo();
			userBonnieAccessInfo.setUser(user);
		} 
		userBonnieAccessInfo.setAccessToken(encryptDecryptToken.encryptKey(result.getAccessToken()));
		userBonnieAccessInfo.setRefreshToken( encryptDecryptToken.encryptKey(result.getRefreshToken()));
		userBonnieAccessInfoDAO.save(userBonnieAccessInfo);
		logger.info("Done saving "+userBonnieAccessInfo.getId());
	}

	public BonnieUserInformationResult getBonnieUserInformationForUser(String userId)
			throws BonnieUnauthorizedException, BonnieServerException, IOException, BonnieBadParameterException, BonnieDoesNotExistException {
		
		UserBonnieAccessInfo bonnieAccessInfo = validateOrRefreshBonnieTokensForUser(userId);
		BonnieUserInformationResult bonnieInformationResult = bonnieApi.getUserInformationByToken(bonnieAccessInfo.getAccessToken());
		return bonnieInformationResult;
	}
	
	public void revokeBonnieAccessTokenForUser(String userId) throws BonnieServerException, Exception {
		UserBonnieAccessInfo bonnieAccessInfo = validateOrRefreshBonnieTokensForUser(userId);
		revokeBonnieAccessTokenForUser(bonnieAccessInfo);
	}

	private UserBonnieAccessInfo validateOrRefreshBonnieTokensForUser(String userId) throws BonnieUnauthorizedException {
		UserBonnieAccessInfo bonnieAccessInfo = userBonnieAccessInfoDAO.findByUserId(userId);
		if (bonnieAccessInfo == null) {
			throw new BonnieUnauthorizedException();
		}
		refreshBonnieTokens(userId);
		return bonnieAccessInfo;
	}

	private void deleteUserBonnieAccessInfo(UserBonnieAccessInfo bonnieAccessInfo) {
		if(bonnieAccessInfo != null) {
			userBonnieAccessInfoDAO.delete(Integer.toString(bonnieAccessInfo.getId()));
		}
	}

	public BonnieAPI getBonnieApi() {
		return bonnieApi;
	}

	public void setBonnieApi(BonnieAPIv1 bonnieApi) {
		this.bonnieApi = bonnieApi;
	}

	@Override
	public void revokeAllBonnieAccessTokens(String userId, String reason) throws BonnieServerException, Exception {
		logger.info("Revoke All Bonnie Access Tokens issued by user " + userId + " for reason " + reason + ".");
		List<UserBonnieAccessInfo> userBonnieAccessInfoList = userBonnieAccessInfoDAO.find();
		for(UserBonnieAccessInfo userBonnieAccessInfo: userBonnieAccessInfoList) {
			revokeBonnieAccessTokenForUser(userBonnieAccessInfo);
		}
	}

	private void revokeBonnieAccessTokenForUser(UserBonnieAccessInfo userBonnieAccessInfo)
			throws BonnieServerException, BonnieUnauthorizedException, Exception {
		bonnieApi.revokeBonnieToken(userBonnieAccessInfo.getAccessToken(), userBonnieAccessInfo.getRefreshToken());
		deleteUserBonnieAccessInfo(userBonnieAccessInfo);
	}
}
