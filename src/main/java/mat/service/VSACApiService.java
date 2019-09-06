package mat.service;

import java.util.List;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.umls.VsacApiResult;
import mat.umls.VsacTicketInformation;

public interface VSACApiService {
	void inValidateVsacUser(String sessionId);
	
	boolean isAlreadySignedIn(String sessionId);
		
	boolean validateVsacUser(String userName, String password, String sessionId);
	
	VsacApiResult getMostRecentValueSetByOID(String oid, String release, String expansionId, String sessionId);
	
	VsacApiResult getAllExpIdentifierList(String sessionId);

	VsacApiResult updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId,
			String sessionId);

	VsacApiResult getDirectReferenceCode(String oid, String sessionId);
	
	VsacApiResult getVSACProgramsReleasesAndProfiles();

	VsacTicketInformation getTicketGrantingTicket(String sessionId);
}