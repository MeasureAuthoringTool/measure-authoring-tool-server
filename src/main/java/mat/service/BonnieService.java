package mat.service;

import java.io.IOException;


import mat.error.bonnie.BonnieAlreadyExistsException;
import mat.error.bonnie.BonnieBadParameterException;
import mat.error.bonnie.BonnieDoesNotExistException;
import mat.error.bonnie.BonnieServerException;
import mat.error.bonnie.BonnieUnauthorizedException;
import mat.error.bonnie.UMLSNotActiveException;
import mat.bonnie.result.BonnieOAuthResult;
import mat.bonnie.result.BonnieUserInformationResult;
import mat.umls.VsacTicketInformation;

public interface BonnieService {
	String getBonnieAccessLink();

	BonnieOAuthResult exchangeCodeForTokens(String code);

	BonnieUserInformationResult getBonnieUserInformationForUser(String userId) throws BonnieUnauthorizedException, BonnieServerException, Exception;

	Boolean updateOrUploadMeasureToBonnie(String measureId, String userId, VsacTicketInformation vsacTicket) throws BonnieUnauthorizedException, BonnieBadParameterException, BonnieDoesNotExistException, BonnieServerException, IOException, BonnieAlreadyExistsException, UMLSNotActiveException;
	
	public void revokeBonnieAccessTokenForUser(String userId) throws BonnieServerException, Exception;
	
	public void revokeAllBonnieAccessTokens(String userId, String reason) throws BonnieServerException, Exception;
}
