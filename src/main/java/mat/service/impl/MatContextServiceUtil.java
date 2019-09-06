package mat.service.impl;

import mat.dao.user.UserDAO;
import mat.dao.library.CQLLibraryDAO;
import mat.dao.measure.MeasureDAO;
import mat.entity.SecurityRole;
import mat.model.library.CQLLibrary;
import mat.model.measure.Measure;
import mat.model.measure.MeasureShareDTO;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLLibraryShareDTO;
import mat.authentication.LoggedInUserUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatContextServiceUtil {
	private boolean isMeasure ;

	public static boolean isAlreadySignedIn(Date lastSignOut, Date lastSignIn, Date current){
		/*
		 * ASSUMPTION: while signed in... lastSignIn is updated every 2 minutes
		 * (1) lastSignOut == null --> see if last sign in time > 3 minutes ago
		 * (2) lastSignOut < lastSignIn --> see if last sign in time > 3 minutes ago
		 * (3)lastSignOut > lastSignIn --> not signed in
		 */
		boolean alreadySignedIn = (lastSignIn == null) ? false :
				((lastSignOut == null) || lastSignOut.before(lastSignIn)) ?
						((current.getTime() - lastSignIn.getTime()) < (3 * 60 * 1000)) :
						false;
		return alreadySignedIn;
	}

	/** The instance. */
	private static MatContextServiceUtil instance = new MatContextServiceUtil();

	/**
	 * Gets the.
	 * 
	 * @return the mat context service util
	 */
	public static MatContextServiceUtil get() {
		return instance;
	}

	public boolean isMeasureDraftable(MeasureDAO measureDAO, Measure measure) {
        List<Measure> measureList = new ArrayList<>();
        measureList.add(measure);
		List<Measure> measuresInSet = measureDAO.getAllMeasuresInSet(measureList);
        if(measure.isDraft()) {
            return false;
        }
        return measuresInSet.stream().filter(m -> m.isDraft()).count() == 0;
    }
	
	/**
	 * Checks if is current measure editable.
	 *
	 * @param measureDAO the measure DAO
	 * @param measureId the measure id
	 * @return true, if is current measure editable
	 */
	public boolean isCurrentMeasureEditable(MeasureDAO measureDAO,
			String measureId) {
		return isCurrentMeasureEditable(measureDAO, measureId, true);
	}
	
	/**
	 * Checks if is current measure editable.
	 *
	 * @param measureDAO the measure dao
	 * @param measureId the measure id
	 * @param checkForDraft the check for draft
	 * @return true, if is current measure editable
	 */
	public boolean isCurrentMeasureEditable(MeasureDAO measureDAO,
			String measureId, boolean checkForDraft) {

		Measure measure = measureDAO.find(measureId);
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		MeasureShareDTO dto = measureDAO.extractDTOFromMeasure(measure);
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		ShareLevel shareLevel = measureDAO.findShareLevelForUser(measureId,
				currentUserId, dto.getMeasureSetId());
		boolean isSharedToEdit = false;
		if (shareLevel != null) {
			isSharedToEdit = ShareLevel.MODIFY_ID.equals(shareLevel.getId());
		}
		boolean isEditable = (isOwner || isSuperUser || isSharedToEdit);
		
		if(checkForDraft){
			isEditable = isEditable && dto.isDraft();
		}
		
		return isEditable;
	}
	
	/**
	 * Checks if is current measure is draftable.
	 *
	 * @param measureDAO the measure dao
	 * @param userDAO the user DAO
	 * @param measureId the measure id
	 * @return true, if is current measure editable
	 */
	public boolean isCurrentMeasureDraftable(MeasureDAO measureDAO,
			UserDAO userDAO, String measureId) {
		
		return isCurrentMeasureEditable(measureDAO, measureId, false);
	}
	
	/**
	 * Checks if is current measure is clonable.
	 *
	 * @param measureDAO the measure dao
	 * @param measureId the measure id
	 * @return true, if is current measure editable
	 */
	public boolean isCurrentMeasureClonable(MeasureDAO measureDAO,
			String measureId) {

		Measure measure = measureDAO.find(measureId);
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		MeasureShareDTO dto = measureDAO.extractDTOFromMeasure(measure);
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		boolean isComposite = measure.getIsCompositeMeasure();
		
		boolean isClonable = (isOwner || isSuperUser) && !isComposite && measure.getReleaseVersion().startsWith("v5");
		return isClonable;
	}
	
	/**
	 * Checks if is current measure editable.
	 *
	 * @param cqlLibraryDAO the cql library DAO
	 * @param libraryId the measure id
	 * @param checkForDraft the check for draft
	 * @return true, if is current measure editable
	 */
	public boolean isCurrentCQLLibraryEditable(CQLLibraryDAO cqlLibraryDAO,
			String libraryId, boolean checkForDraft) {

		CQLLibrary cqlLibrary = cqlLibraryDAO.find(libraryId);
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		CQLLibraryShareDTO dto = cqlLibraryDAO.extractDTOFromCQLLibrary(cqlLibrary);
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		ShareLevel shareLevel = cqlLibraryDAO.findShareLevelForUser(libraryId,
				currentUserId, dto.getCqlLibrarySetId());
		boolean isSharedToEdit = false;
		if (shareLevel != null) {
			isSharedToEdit = ShareLevel.MODIFY_ID.equals(shareLevel.getId());
		}
		boolean isEditable = (isOwner || isSuperUser || isSharedToEdit);
		
		if(checkForDraft){
			isEditable = isEditable && dto.isDraft();
		}
		
		return isEditable;
	}
	
	/**
	 * Checks if is current CQL library editable.
	 *
	 * @param cqlLibraryDAO the cql library DAO
	 * @param libraryId the measure id
	 * @return true, if is current CQL library editable
	 */
	public boolean isCurrentCQLLibraryEditable(CQLLibraryDAO cqlLibraryDAO,
			String libraryId){
		return isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId, true);
	}
	
	/**
	 * Checks if is current CQL library draftable.
	 *
	 * @param cqlLibraryDAO the cql library DAO
	 * @param libraryId the measure id
	 * @return true, if is current CQL library draftable
	 */
	public boolean isCurrentCQLLibraryDraftable(CQLLibraryDAO cqlLibraryDAO,
			String libraryId){
		return isCurrentCQLLibraryEditable(cqlLibraryDAO, libraryId, false);
	}


	public boolean isMeasure() {
		return isMeasure;
	}


	public void setMeasure(boolean isMeasure) {
		this.isMeasure = isMeasure;
	}

}
