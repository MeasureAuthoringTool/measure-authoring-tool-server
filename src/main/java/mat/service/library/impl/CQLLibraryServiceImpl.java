package mat.service.library.impl;

import java.util.List;

import mat.dto.CQLLibraryOwnerReportDTO;
import mat.model.valueset.CQLValueSetTransferObject;
import mat.model.code.MatCodeTransferObject;
import mat.model.library.CQLLibrary;
import mat.model.cql.*;
import mat.service.library.CQLLibraryServiceInterface;
import mat.umls.VsacApiResult;
import mat.result.SaveCQLLibraryResult;
import mat.xml.XmlProcessor;
import mat.cql.result.GetUsedCQLArtifactsResult;
import mat.result.SaveUpdateCQLResult;
import mat.cql.error.InvalidLibraryException;
import mat.error.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CQLLibraryServiceImpl implements CQLLibraryServiceInterface {
	private static final long serialVersionUID = -2412573290030426288L;
	@Autowired CQLLibraryServiceInterface cqlLibraryServiceInterface;
	@Override
	public SaveCQLLibraryResult search(String searchText, int filter, int startIndex, int pageSize) {
		return this.getCQLLibraryService().search(searchText,filter, startIndex,pageSize);
	}

	@Override
	public void save(CQLLibrary cqlLibrary) {

	}

	@Override
	public SaveCQLLibraryResult searchForIncludes(String setId, String libraryName, String searchText){
		return this.getCQLLibraryService().searchForIncludes(setId, libraryName, searchText);
	}
	
	@Override
	public SaveCQLLibraryResult searchForReplaceLibraries(String setId) {
		return this.getCQLLibraryService().searchForReplaceLibraries(setId);

	}
	
	@Override
	public CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryID){
		return this.getCQLLibraryService().findCQLLibraryByID(cqlLibraryID);
	}
	
	@Override
	public SaveCQLLibraryResult saveFinalizedVersion(String libraryId, boolean isMajor, String version, boolean ignoreUnusedLibraries){
		return this.getCQLLibraryService().saveFinalizedVersion(libraryId, isMajor, version, ignoreUnusedLibraries);
	}
	/**
	 * Gets the measure library service.
	 * 
	 * @return the measure library service
	 */
	public CQLLibraryServiceInterface getCQLLibraryService(){
		return cqlLibraryServiceInterface;
	}
	
	
	public SaveCQLLibraryResult save(CQLLibraryDataSetObject cqlLibraryDataSetObject) {
		return this.getCQLLibraryService().save(cqlLibraryDataSetObject);
	}
	
	public String createCQLLookUpTag(String libraryName,String version) {
		return this.getCQLLibraryService().createCQLLookUpTag(libraryName, version);
	}
	
	public XmlProcessor loadCQLXmlTemplateFile() {
		return this.getCQLLibraryService().loadCQLXmlTemplateFile();
	}
	public SaveUpdateCQLResult getCQLData(String id) {
		return this.getCQLLibraryService().getCQLData(id);
	}
	
	public SaveUpdateCQLResult getCQLDataForLoad(String id) {
		return this.getCQLLibraryService().getCQLDataForLoad(id);
	}
	
	@Override
	public boolean isLibraryLocked(String id) {
		return this.getCQLLibraryService().isLibraryLocked(id);
	}

	@Override
	public SaveCQLLibraryResult resetLockedDate(String currentLibraryId, String userId) {
		return this.getCQLLibraryService().resetLockedDate(currentLibraryId, userId);
	}

	@Override
	public SaveCQLLibraryResult updateLockedDate(String currentLibraryId, String userId) {
		return this.getCQLLibraryService().updateLockedDate(currentLibraryId, userId);
	}

	@Override
	public SaveCQLLibraryResult getAllRecentCQLLibrariesForUser(String userId) {
		return this.getCQLLibraryService().getAllRecentCQLLibrariesForUser(userId);
	}
	
	@Override
	public void isLibraryAvailableAndLogRecentActivity(String libraryid, String userId){
		this.getCQLLibraryService().isLibraryAvailableAndLogRecentActivity(libraryid, userId);
	}

	@Override
	public String getCQLLookUpXml(String libraryName, String versionText, XmlProcessor xmlProcessor, String mainXPath) {
		return null;
	}

	@Override
	public SaveUpdateCQLResult getLibraryCQLFileData(String libraryId){
	   return this.getCQLLibraryService().getLibraryCQLFileData(libraryId);
	}
	
	@Override
	public SaveUpdateCQLResult getCQLLibraryFileData(String libraryId) {
		return this.getCQLLibraryService().getCQLLibraryFileData(libraryId);
	}
	
	@Override
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String libraryId, String libraryValue, String libraryComment){
		return this.getCQLLibraryService().saveAndModifyCQLGeneralInfo(libraryId, libraryValue, libraryComment);
	}

	@Override
	public SaveCQLLibraryResult saveDraftFromVersion(String libraryId) {
		return this.getCQLLibraryService().saveDraftFromVersion(libraryId);
	}
	
	@Override
	public SaveCQLLibraryResult getUserShareInfo(String cqlId, final String searchText){
		return this.getCQLLibraryService().getUserShareInfo(cqlId, searchText);
	}
	@Override
	public void updateUsersShare(SaveCQLLibraryResult result) {
		this.getCQLLibraryService().updateUsersShare(result);
	} 
	@Override
	public SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException {
		return this.getCQLLibraryService().saveIncludeLibrayInCQLLookUp(libraryId, toBeModifiedObj, currentObj, incLibraryList);
	}
	@Override
	public SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj,
			List<CQLIncludeLibrary> viewIncludeLibrarys) {
		return this.getCQLLibraryService().deleteInclude(libraryId, toBeModifiedIncludeObj, viewIncludeLibrarys);
	}
	@Override
	public GetUsedCQLArtifactsResult getUsedCqlArtifacts(String libraryId) {
		return this.getCQLLibraryService().getUsedCqlArtifacts(libraryId);
	}
	@Override
	public int countNumberOfAssociation(String Id) {
		return this.getCQLLibraryService().countNumberOfAssociation(Id);
	}
	@Override
	public SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject) {
		return this.getCQLLibraryService().saveCQLValueset(valueSetTransferObject);
	}
	@Override
	public SaveUpdateCQLResult deleteValueSet(String toBeDelValueSetId, String libraryId) {
		return this.getCQLLibraryService().deleteValueSet(toBeDelValueSetId, libraryId);
	}
	
	@Override
	public SaveUpdateCQLResult deleteCode(String toBeDeletedId, String libraryId) {
		return this.getCQLLibraryService().deleteCode(toBeDeletedId, libraryId);
	}
	@Override
	public SaveUpdateCQLResult saveAndModifyDefinitions(String libraryId, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable) {
		return this.getCQLLibraryService().saveAndModifyDefinitions(libraryId, toBeModifiedObj, currentObj, definitionList, isFormatable);
	}
	@Override
	public
	SaveUpdateCQLResult saveAndModifyFunctions(String libraryId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList, boolean isFormatable) {
		return this.getCQLLibraryService().saveAndModifyFunctions(libraryId, toBeModifiedObj, currentObj, functionsList, isFormatable);
	}
	@Override
	public SaveUpdateCQLResult saveAndModifyParameters(String libraryId, CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList, boolean isFormatable) {
		return this.getCQLLibraryService().saveAndModifyParameters(libraryId, toBeModifiedObj, currentObj, parameterList, isFormatable);
	}
	@Override
	public SaveUpdateCQLResult deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj,
			List<CQLDefinition> definitionList) {
		return this.getCQLLibraryService().deleteDefinition(libraryId, toBeDeletedObj, definitionList);
	}
	@Override
	public SaveUpdateCQLResult deleteFunctions(String libraryId, CQLFunctions toBeDeletedObj,
			List<CQLFunctions> functionsList) {
		return this.getCQLLibraryService().deleteFunctions(libraryId, toBeDeletedObj, functionsList);
	}
	@Override
	public SaveUpdateCQLResult deleteParameter(String libraryId, CQLParameter toBeDeletedObj,
			List<CQLParameter> parameterList) {
		return this.getCQLLibraryService().deleteParameter(libraryId, toBeDeletedObj, parameterList);
	}
	@Override
	public SaveUpdateCQLResult saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject) {
		return this.getCQLLibraryService().saveCQLUserDefinedValueset(matValueSetTransferObject);
	}
	@Override
	public SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject) {
		return this.getCQLLibraryService().modifyCQLValueSets(matValueSetTransferObject);
	}
	@Override
	public VsacApiResult updateCQLVSACValueSets(String currentCQLLibraryId, String expansionId, String sessionId) {
		//TODO pass in sessionId
		return this.getCQLLibraryService().updateCQLVSACValueSets(currentCQLLibraryId, expansionId, sessionId);
	}
	@Override
	public CQLKeywords getCQLKeywordsLists() {
		return this.getCQLLibraryService().getCQLKeywordsLists();
	}

	@Override
	public List<CQLLibraryAssociation> getAssociations(String Id) {
		return null;
	}

	@Override
	public 
	void transferLibraryOwnerShipToUser(List<String> list, String toEmail){
		this.getCQLLibraryService().transferLibraryOwnerShipToUser(list, toEmail);
	}

	@Override
	public List<CQLLibraryOwnerReportDTO> getCQLLibrariesForOwner() {
		return null;
	}

	@Override
	public SaveUpdateCQLResult saveCQLCodestoCQLLibrary(MatCodeTransferObject transferObject){
		return this.getCQLLibraryService().saveCQLCodestoCQLLibrary(transferObject);
	}
	
	@Override
	public SaveUpdateCQLResult saveCQLCodeListToCQLLibrary(
			List<CQLCode> codeList, String libraryId) {
		return this.getCQLLibraryService().saveCQLCodeListToCQLLibrary(codeList, libraryId);
	}
	
	@Override
	public SaveUpdateCQLResult modifyCQLCodeInCQLLibrary(CQLCode codeToReplace, CQLCode replacementCode,
			String cqlLibraryId) {
		return this.getCQLLibraryService().modifyCQLCodeInCQLLibrary(codeToReplace, replacementCode, cqlLibraryId);
	}

	@Override
	public void saveCQLLibraryExport(CQLLibrary cqlLibrary, String cqlXML) {

	}

	@Override
	public final void deleteCQLLibrary(final String cqllibId, String loginUserId, String password) throws AuthenticationException {
		 this.getCQLLibraryService().deleteCQLLibrary(cqllibId, loginUserId, password);
	}
	@Override
	public CQLQualityDataModelWrapper saveValueSetList(List<CQLValueSetTransferObject> transferObjectList,
			List<CQLQualityDataSetDTO> appliedValueSetList, String cqlLibraryId) {
		return this.getCQLLibraryService().saveValueSetList(transferObjectList, appliedValueSetList, cqlLibraryId);
	}
}
