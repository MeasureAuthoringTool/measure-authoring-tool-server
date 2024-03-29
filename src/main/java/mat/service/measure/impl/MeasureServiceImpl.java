package mat.service.measure.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import mat.cql.result.GetUsedCQLArtifactsResult;
import mat.dto.QualityDataSetDTO;
import mat.entity.MeasureType;
import mat.entity.Organization;
import mat.entity.RecentMSRActivityLog;
import mat.measure.measuredetails.translate.ManageMeasureDetailModelMapper;
import mat.measure.measuredetails.translate.MeasureDetailModelMapper;
import mat.model.*;
import mat.model.code.MatCodeTransferObject;
import mat.model.component.ComponentMeasureTabObject;
import mat.model.measure.ManageMeasureSearchModel;
import mat.model.measure.ManageMeasureShareModel;
import mat.model.measure.MeasureSearchModel;
import mat.model.measure.MeasureXmlModel;
import mat.model.valueset.CQLValueSetTransferObject;
import mat.model.valueset.MatValueSet;
import mat.result.*;
import mat.measure.measuredetails.models.ManageCompositeMeasureDetailModel;
import mat.model.measuredetail.ManageMeasureDetailModel;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeWrapper;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.service.measure.MeasureLibraryService;
import mat.service.measure.MeasureService;
import mat.umls.VsacApiResult;
import mat.cql.error.InvalidLibraryException;
import mat.error.AuthenticationException;
import mat.error.MatException;
import mat.error.measure.DeleteMeasureException;
import mat.measure.measuredetails.models.MeasureDetailsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeasureServiceImpl implements MeasureService {
	private static final long serialVersionUID = 2280421300224680146L;

	@Autowired MeasureLibraryService measureLibraryService;
	@Override
	public void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName) {
		this.getMeasureLibraryService().appendAndSaveNode(measureXmlModel, nodeName);
		
	}
	
	@Override
	public void cloneMeasureXml(boolean creatingDraft, String oldMeasureId,
			String clonedMeasureId) {
		this.getMeasureLibraryService().cloneMeasureXml(creatingDraft, oldMeasureId, clonedMeasureId);
		
	}
	
	@Override
	public void createAndSaveElementLookUp(List<QualityDataSetDTO> list,
			String measureID, String expProfileToAllQDM) {
		this.getMeasureLibraryService().createAndSaveElementLookUp(list, measureID, expProfileToAllQDM);
	}
	
	@Deprecated
	@Override
	public int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId) {
		return this.getMeasureLibraryService().generateAndSaveMaxEmeasureId(measureId);
	}
	
	/** Gets the all recent measure for user.
	 * 
	 * @param userId - String userId.
	 * @return ManageMeasureSearchModel */
	@Override
	public ManageMeasureSearchModel getAllRecentMeasureForUser(String userId) {
		return this.getMeasureLibraryService().getAllRecentMeasureForUser(userId);
	}
	
	@Override
	public QualityDataModelWrapper getAppliedQDMFromMeasureXml(
			String measureId, boolean checkForSupplementData) {
		return this.getMeasureLibraryService().getAppliedQDMFromMeasureXml(measureId, checkForSupplementData);
	}
	
	@Override
	public CQLQualityDataModelWrapper getCQLAppliedQDMFromMeasureXml(String measureId, boolean checkForSupplementData) {
		return this.getMeasureLibraryService().getCQLAppliedQDMFromMeasureXml(measureId, checkForSupplementData);
	}

	@Override
	public int getMaxEMeasureId() {
		return this.getMeasureLibraryService().getMaxEMeasureId();
	}
	
	@Override
	public ManageMeasureDetailModel getMeasure(String key) {
		return this.getMeasureLibraryService().getMeasure(key);
	}
	
	@Override
	public ManageCompositeMeasureDetailModel getCompositeMeasure(String measureId) {
		return this.getMeasureLibraryService().getCompositeMeasure(measureId);
	}
	
	/**
	 * Gets the measure library service.
	 * 
	 * @return the measure library service
	 */
	public MeasureLibraryService getMeasureLibraryService(){
		return measureLibraryService;
	}
	
	@Override
	public MeasureXmlModel getMeasureXmlForMeasure(String measureId) {
		return this.getMeasureLibraryService().getMeasureXmlForMeasure(measureId);
	}
	
	@Override
	public List<RecentMSRActivityLog> getRecentMeasureActivityLog(String userId) {
		return this.getMeasureLibraryService().getRecentMeasureActivityLog(userId);
	}
	
	@Override
	public ManageMeasureShareModel getUsersForShare(String userName, String measureId,
													int startIndex, int pageSize) {
		return this.getMeasureLibraryService().getUsersForShare(userName, measureId, startIndex, pageSize);
	}
	
	@Override
	public boolean isMeasureLocked(String id) {
		return this.getMeasureLibraryService().isMeasureLocked(id);
	}
	
	@Override
	public SaveMeasureResult resetLockedDate(String measureId, String userId) {
		return this.getMeasureLibraryService().resetLockedDate(measureId, userId);
	}
	
	@Override
	public SaveMeasureResult save(ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().save(model);
	}
	
	@Override
	public SaveMeasureResult saveCompositeMeasure(ManageCompositeMeasureDetailModel model) {
		return this.getMeasureLibraryService().saveCompositeMeasure(model);
	}
	
	@Override
	@Deprecated
	public void saveAndDeleteMeasure(String measureID,  String loginUserId) {
		this.getMeasureLibraryService().saveAndDeleteMeasure(measureID,loginUserId);
	}
	
	@Override
	public void deleteMeasure(String measureId, String loggedInUserId) throws DeleteMeasureException, AuthenticationException {
		this.getMeasureLibraryService().deleteMeasure(measureId, loggedInUserId);
	}
	
	@Override
	public SaveMeasureResult saveFinalizedVersion(String measureId, boolean isMajor, String version,  boolean shouldPackage, boolean ignoreUnusedLibraries) {
		return this.getMeasureLibraryService().saveFinalizedVersion(measureId, isMajor, version, shouldPackage, ignoreUnusedLibraries);
	}
	
	@Override
	public SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().saveMeasureDetails(model);
	}
	
	@Override
	public void saveMeasureXml(MeasureXmlModel measureXmlModel) {
		this.getMeasureLibraryService().saveMeasureXml(measureXmlModel);
		
	}
	
	@Override
	public ManageMeasureSearchModel search(MeasureSearchModel advancedSearchModel) {
		return this.getMeasureLibraryService().search(advancedSearchModel);
	}
	
	@Override
	public TransferOwnerShipModel searchUsers(String searchText, int startIndex,
											  int pageSize) {
		return this.getMeasureLibraryService().searchUsers(searchText, startIndex, pageSize);
	}
	
	@Override
	public void transferOwnerShipToUser(List<String> list, String toEmail) {
		this.getMeasureLibraryService().transferOwnerShipToUser(list, toEmail);
		
	}
	
	@Override
	public SaveMeasureResult updateLockedDate(String measureId, String userId) {
		return this.getMeasureLibraryService().updateLockedDate(measureId, userId);
	}
	
	@Override
	public void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
								 QualityDataSetDTO modifyDTO, String measureId) {
		this.getMeasureLibraryService().updateMeasureXML(modifyWithDTO, modifyDTO, measureId);
		
	}
	
	@Override
	public void updatePrivateColumnInMeasure(String measureId, boolean isPrivate) {
		this.getMeasureLibraryService().updatePrivateColumnInMeasure(measureId, isPrivate);
	}
	
	@Override
	public void updateUsersShare(ManageMeasureShareModel model) {
		this.getMeasureLibraryService().updateUsersShare(model);
	}
	
	@Override
	public ValidateMeasureResult validateExports(final String measureId) throws Exception {
		return this.getMeasureLibraryService().validateExports(measureId);
	}; 

	@Override
	public ValidateMeasureResult createExports(String key , List<MatValueSet> matValueSetList, boolean shouldCreateArtifacts)
			throws MatException {
		return this.getMeasureLibraryService().createExports(key, matValueSetList, shouldCreateArtifacts);
	}
	
	@Override
	public String getHumanReadableForNode(String measureId, String populationSubXML){
		return this.getMeasureLibraryService().getHumanReadableForNode(measureId, populationSubXML);
	}
	
	@Override
	public SaveMeasureResult saveMeasureAtPackage(ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().saveMeasureAtPackage(model);
	}
	
	@Override
	public SortedClauseMapResult saveSubTreeInMeasureXml(MeasureXmlModel measureXmlModel , String nodeName, String nodeUUID) {
		return this.getMeasureLibraryService().saveSubTreeInMeasureXml(measureXmlModel , nodeName, nodeUUID);
	}
	
	@Override
	public HashMap<String, String> checkAndDeleteSubTree(String measureId, String subTreeUUID){
		return this.getMeasureLibraryService().checkAndDeleteSubTree(measureId, subTreeUUID);
	}
	
	@Override
	public boolean isSubTreeReferredInLogic(String measureId, String subTreeUUID){
		return this.getMeasureLibraryService().isSubTreeReferredInLogic(measureId, subTreeUUID);
	}
	
	@Override
	public ManageMeasureSearchModel getComponentMeasures(String measureId){
		return getMeasureLibraryService().getComponentMeasures(measureId);
	}
	
	@Override
	public ValidateMeasureResult validatePackageGrouping(
			ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().validatePackageGrouping(
				model);
	}
	
	@Override
	public ValidateMeasureResult validateMeasureXmlinpopulationWorkspace(
			MeasureXmlModel measureXmlModel) {
		return this.getMeasureLibraryService().validateMeasureXmlAtCreateMeasurePackager(measureXmlModel);
	}
	
	@Override
	public void updateMeasureXmlForDeletedComponentMeasureAndOrg(String measureId) {
		
		this.getMeasureLibraryService().updateMeasureXmlForDeletedComponentMeasureAndOrg(measureId);
	}
	
	@Override
	public ValidateMeasureResult validateForGroup(ManageMeasureDetailModel model) {
		
		return this.getMeasureLibraryService().validateForGroup(model);
	}
	
	/**
	 * Validates and packages the measure when the user versions or packages a measure
	 * @param mode the measure details
	 */
	@Override
	public SaveMeasureResult validateAndPackageMeasure(ManageMeasureDetailModel model) {
		return this.getMeasureLibraryService().validateAndPackage(model, true);
	}
	
	@Override
	public List<MeasureType> getAllMeasureTypes(){
		return this.getMeasureLibraryService().getAllMeasureTypes();
	}
	
	@Override
	public List<Organization> getAllOrganizations() {
		
		return this.getMeasureLibraryService().getAllOrganizations();
	}
	
	@Override
	public SortedClauseMapResult saveSubTreeOccurrence(MeasureXmlModel measureXmlModel, String nodeName, String nodeUUID) {
		return this.getMeasureLibraryService().saveSubTreeOccurrence(measureXmlModel, nodeName, nodeUUID);
		
	}
	
	@Override
	public boolean isQDMVariableEnabled(String measureId, String subTreeUUID) {
		return this.getMeasureLibraryService().isQDMVariableEnabled(measureId, subTreeUUID);
	}
	
	@Override
	public LinkedHashMap<String, String> getSortedClauseMap(String measureId) {
		return this.getMeasureLibraryService().getSortedClauseMap(measureId);
	}
	
	@Override
	public SortedClauseMapResult getMeasureXmlForMeasureAndSortedSubTreeMap(
			String currentMeasureId) {
		return this.getMeasureLibraryService().getMeasureXmlForMeasureAndSortedSubTreeMap(currentMeasureId);
	}
	
	@Override
	public MeasureDetailResult getUsedStewardAndDevelopersList(String measureId) {
		return this.getMeasureLibraryService().getUsedStewardAndDevelopersList(measureId);
	}
	
	@Override
	public void updateMeasureXMLForExpansionIdentifier(List<QualityDataSetDTO> modifyWithDTOList,
			String measureId, String expansionProfile) {
		this.getMeasureLibraryService().updateMeasureXMLForExpansionIdentifier(modifyWithDTOList, measureId, expansionProfile);
	}
	
	@Override
	public void updateCQLMeasureXMLForExpansionProfile(List<CQLQualityDataSetDTO> modifyWithDTOList,
			String measureId, String expansionProfile) {
		this.getMeasureLibraryService().updateCQLMeasureXMLForExpansionIdentifier(modifyWithDTOList, measureId, expansionProfile);
	}
	
	@Override
	public QualityDataModelWrapper getDefaultSDEFromMeasureXml(String measureId) {
		return this.getMeasureLibraryService().getDefaultSDEFromMeasureXml(measureId);
	}
	
	@Override
	public CQLQualityDataModelWrapper getDefaultCQLSDEFromMeasureXml(String measureId) {
		return this.getMeasureLibraryService().getDefaultCQLSDEFromMeasureXml(measureId);
	}
	
	@Override
	public CQLModel parseCQL(String cqlBuilder) {
		return this.getMeasureLibraryService().parseCQL(cqlBuilder);
	}
	
	@Override
	public SaveUpdateCQLResult saveAndModifyDefinitions(String measureId, CQLDefinition toBemodifiedObj,
														CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable){
		return this.getMeasureLibraryService().saveAndModifyDefinitions(measureId, toBemodifiedObj, currentObj, definitionList, isFormatable);
	}
	
	@Override
	public SaveUpdateCQLResult saveAndModifyParameters(String measureId, CQLParameter toBemodifiedObj,
			CQLParameter currentObj, List<CQLParameter> parameterList, boolean isFormatable){
		return this.getMeasureLibraryService().saveAndModifyParameters(measureId, toBemodifiedObj, currentObj, parameterList, isFormatable);
	}
	
	@Override
	public SaveUpdateCQLResult saveAndModifyFunctions(String measureId, CQLFunctions toBeModifiedObj,
			CQLFunctions currentObj, List<CQLFunctions> functionsList,boolean isFormatable){
		return this.getMeasureLibraryService().saveAndModifyFunctions(measureId, toBeModifiedObj, currentObj, functionsList, isFormatable);
	}
	
	@Override
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(
			String currentMeasureId, String context, String comments) {
		return this.getMeasureLibraryService().saveAndModifyCQLGeneralInfo(currentMeasureId, context, comments);
	}
	
	@Override
	public CQLKeywords getCQLKeywordsList() {
		return this.getMeasureLibraryService().getCQLKeywordsLists();
	}

	@Override
	public String getJSONObjectFromXML() {
		return this.getMeasureLibraryService().getJSONObjectFromXML();
	}

	@Override
	public void createAndSaveCQLLookUp(List<QualityDataSetDTO> list, String measureID, String expProfileToAllQDM) {
		this.getMeasureLibraryService().createAndSaveCQLLookUp(list, measureID, expProfileToAllQDM);
		
	}
	
	@Override
	public SaveUpdateCQLResult parseCQLStringForError( String cqlFileString) {
		return this.getMeasureLibraryService().parseCQLStringForError(cqlFileString);
	}

	@Override
	public SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj, 
			List<CQLDefinition> definitionList) {
		return this.getMeasureLibraryService().deleteDefinition(measureId, toBeDeletedObj, definitionList);
	}

	@Override
	public SaveUpdateCQLResult deleteFunctions(String measureId, CQLFunctions toBeDeletedObj, 
			List<CQLFunctions> functionsList) {
		return this.getMeasureLibraryService().deleteFunctions(measureId, toBeDeletedObj, functionsList);
	}

	@Override
	public SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj, 
			List<CQLParameter> parameterList) {
		return this.getMeasureLibraryService().deleteParameter(measureId, toBeDeletedObj, parameterList);
	}
	
	@Override
	public GetUsedCQLArtifactsResult getUsedCQLArtifacts(String measureId) {
		return this.getMeasureLibraryService().getUsedCqlArtifacts(measureId);
		
	}
	
	@Override
	public SaveUpdateCQLResult deleteValueSet(String toBeDeletedValueSetId, String measureID) {
		return this.getMeasureLibraryService().deleteValueSet(toBeDeletedValueSetId,  measureID);
		
	}
	
	@Override
	public SaveUpdateCQLResult deleteCode(String toBeDeletedId, String measureID) {
		return this.getMeasureLibraryService().deleteCode(toBeDeletedId,  measureID);
		
	}

	@Override
	public CQLQualityDataModelWrapper getCQLValusets(String measureID) {
		return this.getMeasureLibraryService().getCQLValusets(measureID);
	}

	@Override
	public SaveUpdateCQLResult saveCQLValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject) {
		return this.getMeasureLibraryService().saveCQLValuesettoMeasure(valueSetTransferObject);
	}
	
	@Override
	public SaveUpdateCQLResult saveCQLUserDefinedValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject) {
		return this.getMeasureLibraryService().saveCQLUserDefinedValuesettoMeasure(valueSetTransferObject);
	}
	
	@Override
	public SaveUpdateCQLResult updateCQLValuesetsToMeasure(
			CQLValueSetTransferObject matValueSetTransferObject) {
		return this.getMeasureLibraryService().modifyCQLValueSetstoMeasure(matValueSetTransferObject);
	}
	
	@Override
	public SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String measureId, CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException{
		return this.getMeasureLibraryService().saveIncludeLibrayInCQLLookUp(measureId, toBeModifiedObj, currentObj, incLibraryList);
	}
	
	@Override
	public SaveUpdateCQLResult getMeasureCQLDataForLoad(String measureId) {
		return this.getMeasureLibraryService().getMeasureCQLDataForLoad(measureId);
	}

	@Override
	public SaveUpdateCQLResult getMeasureCQLData(String measureId) {
		return this.getMeasureLibraryService().getMeasureCQLData(measureId);
	}
	@Override
	public SaveUpdateCQLResult getMeasureCQLFileData(String measureId) {
		return this.getMeasureLibraryService().getMeasureCQLFileData(measureId);
	}
	
	@Override
	public SaveUpdateCQLResult getMeasureCQLLibraryData(String measureId) {
		return this.getMeasureLibraryService().getMeasureCQLLibraryData(measureId);
	}
	
	@Override
	public SaveUpdateCQLResult deleteInclude(String currentMeasureId,
			CQLIncludeLibrary toBeModifiedIncludeObj,
			List<CQLIncludeLibrary> viewIncludeLibrarys) {
		return this.getMeasureLibraryService().deleteInclude(currentMeasureId, toBeModifiedIncludeObj, viewIncludeLibrarys);
	}

	@Override
	public VsacApiResult updateCQLVSACValueSets(String currentMeasureId, String expansionId) {
		return null; //TODO implement this
	}
	
/*	@Override
	public VsacApiResult updateCQLVSACValueSets(String currentMeasureId, String expansionId) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.getMeasureLibraryService().updateCQLVSACValueSets(currentMeasureId, expansionId, sessionId);
	}*/
	
	@Override
	public SaveUpdateCQLResult saveCQLCodestoMeasure(MatCodeTransferObject transferObject){
		return this.getMeasureLibraryService().saveCQLCodestoMeasure(transferObject);
	}
	
	@Override
	public SaveUpdateCQLResult saveCQLCodeListToMeasure(List<CQLCode> codeList, String measureId){
		return this.getMeasureLibraryService().saveCQLCodeListToMeasure(codeList, measureId);
	}
	
	@Override
	public CQLCodeWrapper getCQLCodes(String measureID){
		return this.getMeasureLibraryService().getCQLCodes(measureID);
	}
	@Override
	public CQLQualityDataModelWrapper saveValueSetList(List<CQLValueSetTransferObject> transferObjectList,
			List<CQLQualityDataSetDTO> appliedValueSetList, String measureId) {
		 return this.getMeasureLibraryService().saveValueSetList(transferObjectList, appliedValueSetList, measureId);
	}

	@Override
	public SaveUpdateCQLResult modifyCQLCodeInMeasure(CQLCode modifyCQLCode, CQLCode refCode, String measureId) {
		return this.getMeasureLibraryService().modifyCQLCodeInMeasure(modifyCQLCode, refCode, measureId);
	}

	@Override
	public ManageMeasureSearchModel searchComponentMeasures(MeasureSearchModel searchModel) {
		return this.getMeasureLibraryService().searchComponentMeasures(searchModel);
	}

	@Override
	public ManageCompositeMeasureDetailModel buildCompositeMeasure(ManageCompositeMeasureDetailModel compositeMeasure) {
		return this.getMeasureLibraryService().buildCompositeMeasure(compositeMeasure);
	}

	@Override
	public CompositeMeasureValidationResult validateCompositeMeasure(
			ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
		return this.getMeasureLibraryService().validateCompositeMeasure(manageCompositeMeasureDetailModel);
	}
	
	@Override
	public List<ComponentMeasureTabObject> getCQLLibraryInformationForComponentMeasure(String compositeMeasureId) {
		return this.getMeasureLibraryService().getCQLLibraryInformationForComponentMeasure(compositeMeasureId);
	}

	@Override
	public GenericResult checkIfMeasureIsUsedAsComponentMeasure(String currentMeasureId) {
		return this.getMeasureLibraryService().checkIfMeasureIsUsedAsComponentMeasure(currentMeasureId);
	}

	@Override
	public Boolean isCompositeMeasure(String currentMeasureId) {
		return this.getMeasureLibraryService().isCompositeMeasure(currentMeasureId);
	}

	@Override
	public MeasureDetailsModel getMeasureDetailsAndLogRecentMeasure(String measureId, String userId) {
		MeasureDetailsModel measureDetailsModel = null;
		ManageMeasureDetailModel manageMeasureDetailModel = getManageMeasureDetailModel(measureId, userId);
		MeasureDetailModelMapper measureDetailModelMapper = new ManageMeasureDetailModelMapper(manageMeasureDetailModel);
		measureDetailsModel = measureDetailModelMapper.getMeasureDetailsModel(isCompositeMeasure(measureId));
		return measureDetailsModel;
	}

	@Deprecated
	@Override
	public ManageMeasureDetailModel getMeasureAndLogRecentMeasure(String currentMeasureId, String loggedinUserId) {
		return getManageMeasureDetailModel(currentMeasureId, loggedinUserId);
	}

	private ManageMeasureDetailModel getManageMeasureDetailModel(String currentMeasureId, String loggedinUserId) {
		ManageMeasureDetailModel manageMeasureDetailModel;
		if(isCompositeMeasure(currentMeasureId)) {
			manageMeasureDetailModel = getCompositeMeasure(currentMeasureId);
		} else {
			manageMeasureDetailModel = getMeasure(currentMeasureId);
		}
		
		if(manageMeasureDetailModel != null){
			getMeasureLibraryService().recordRecentMeasureActivity(currentMeasureId, loggedinUserId);
		}
		return manageMeasureDetailModel;
	}

	@Override
	public int generateAndSaveMaxEmeasureId(boolean isEditable, String measureId) {
		return this.getMeasureLibraryService().generateAndSaveMaxEmeasureId(isEditable, measureId);
	}
}
