package mat.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mat.umls.UMLSSessionTicket;
import mat.umls.VsacApiResult;
import mat.umls.VsacTicketInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.service.VSACApiService;

/** VSACAPIServiceImpl class. **/
@Configurable
@Service
public class VSACAPIServiceImpl implements VSACApiService {
	
	@Autowired VSACApiService vsacapi;

/*	public final void inValidateVsacUser() {
		String sessionId = getThreadLocalRequest().getSession().getId();
		this.vsacapi.inValidateVsacUser(sessionId);
	}*/
	

	//@Override
	public final boolean isAlreadySignedIn() {
/*		HttpServletRequest thread = getThreadLocalRequest();
		String sessionId = thread.getSession().getId();
		return this.vsacapi.isAlreadySignedIn(sessionId);*/
		//TODO handle this
		return true;
	}

	//@Override
	public final VsacTicketInformation getTicketGrantingToken() {
/*		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.getTicketGrantingTicket(sessionId);*/
		//TODO handle this
		return null;
	}

	@Override
	public final VsacTicketInformation getTicketGrantingTicket(String sessionId) {
		return UMLSSessionTicket.getTicket(sessionId);
	}

	public final boolean validateVsacUser(final String userName, final String password) {
/*		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.validateVsacUser(userName, password,sessionId);*/
		//TODO handle this
		return true;
	}

	public final VsacApiResult getMostRecentValueSetByOID(final String oid, final String release, String expansionId) {
/*		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.getMostRecentValueSetByOID(oid, release, expansionId, sessionId);*/
		//TODO handle this
		return null;
	}

	public VsacApiResult updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId) {
/*		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.updateCQLVSACValueSets(appliedQDMList, defaultExpId, sessionId);*/
		//TODO handle this
		return null;
	}

	public VsacApiResult getDirectReferenceCode(String url) {
/*		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.getDirectReferenceCode(url, sessionId);*/
		//TODO handle this
		return null;
	}

	@Override
	public void inValidateVsacUser(String sessionId) {

	}

	@Override
	public boolean isAlreadySignedIn(String sessionId) {
		return false;
	}

	@Override
	public boolean validateVsacUser(String userName, String password, String sessionId) {
		return false;
	}

	@Override
	public VsacApiResult getMostRecentValueSetByOID(String oid, String release, String expansionId, String sessionId) {
		return null;
	}

	@Override
	public VsacApiResult getAllExpIdentifierList(String sessionId) {
		return null;
	}

	@Override
	public VsacApiResult updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId, String sessionId) {
		return null;
	}

	@Override
	public VsacApiResult getDirectReferenceCode(String oid, String sessionId) {
		return null;
	}

	public VsacApiResult getVSACProgramsReleasesAndProfiles() {
		return this.vsacapi.getVSACProgramsReleasesAndProfiles();
	}
}