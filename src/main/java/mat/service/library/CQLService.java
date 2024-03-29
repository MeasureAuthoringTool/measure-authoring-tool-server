package mat.service.library;

import java.io.IOException;
import java.util.List;

import mat.cql.result.GetUsedCQLArtifactsResult;
import mat.result.SaveUpdateCQLResult;
import mat.cql.error.InvalidLibraryException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import mat.model.valueset.CQLValueSetTransferObject;
import mat.model.code.MatCodeTransferObject;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLCodeWrapper;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryAssociation;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;

public interface CQLService {

    /**
     * Parses the cql.
     *
     * @param cqlBuilder the cql builder
     * @return the CQL model
     */
    CQLModel parseCQL(String cqlBuilder);

	/**
	 * Gets the CQL data.
	 *
	 * @param measureId the measure id
	 * @return the CQL data
	 */
	SaveUpdateCQLResult getCQLData(String xmlString);

	/**
	 * Save and modify cql general info.
	 *
	 * @param currentMeasureId the current measure id
	 * @param context the context
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(
            String currentMeasureId, String context, String libraryComment);
	
	/**
	 * Save and modify functions.
	 *
	 * @param measureId the measure id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param functionsList the functions list
	 * @param isFormatable flag to determine if the function should be formatted on save
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyFunctions(String measureId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
                                               List<CQLFunctions> functionsList, boolean isFormatable);
	
	/**
	 * Save and modify parameters.
	 *
	 * @param measureId the measure id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @param isFormatable flag to determine if the parameter should be formatted on save

	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyParameters(String measureId, CQLParameter toBeModifiedObj, CQLParameter currentObj,
                                                List<CQLParameter> parameterList, boolean isFormatable);
	
	/**
	 * Save and modify definitions.
	 *
	 * @param measureId the measure id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @param isFormatable flag to determine if the definition should be formatted on save
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyDefinitions(String xml, CQLDefinition toBeModifiedObj, CQLDefinition currentObj,
                                                 List<CQLDefinition> definitionList, boolean isFormatable);
	
	/**
	 * Delete definition
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @return the save and update result
	 */
	SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj,
                                         List<CQLDefinition> definitionList);
	
	/**
	 * Delete functions
	 * 
	 * @param measureId the measure id
	 * @param toBeDeltedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param functionsList the functions list
	 * @return the save and update result
	 */
	SaveUpdateCQLResult deleteFunctions(String measureId, CQLFunctions toBeDeltedObj,
                                        List<CQLFunctions> functionsList);
	
	/**
	 * Delete parameter
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @return the save and update result
	 */
	SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj,
                                        List<CQLParameter> parameterList);
	
	/**
	 * Gets the CQL data type list.
	 *
	 * @return the CQL data type list
	 */
	CQLKeywords getCQLKeyWords();
	
	/**
	 * Gets the CQL file data.
	 *
	 * @param measureId the measure id
	 * @return the CQL file data
	 */
	SaveUpdateCQLResult getCQLFileData(String xmlString);

	String createParametersXML(CQLParameter parameter);

	String getJSONObjectFromXML();

	String createDefinitionsXML(CQLDefinition definition);

	String getSupplementalDefinitions();

	String getCqlString(CQLModel cqlModel);

	String getDefaultCodeSystems();

	GetUsedCQLArtifactsResult getUsedCQlArtifacts(String measureId);

	SaveUpdateCQLResult parseCQLStringForError(String cqlFileString);

	CQLQualityDataModelWrapper getCQLValusets(String measureID, CQLQualityDataModelWrapper cqlQualityDataModelWrapper);

	SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject);

	SaveUpdateCQLResult saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject);

	SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject);

	SaveUpdateCQLResult saveAndModifyIncludeLibrayInCQLLookUp(String measureId, CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException;

	SaveUpdateCQLResult deleteInclude(String currentMeasureId,
                                      CQLIncludeLibrary toBeModifiedIncludeObj,
                                      List<CQLIncludeLibrary> viewIncludeLibrarys);

	void saveCQLAssociation(CQLIncludeLibrary currentObj, String measureId);

	void deleteCQLAssociation(CQLIncludeLibrary currentObj, String measureId);

	SaveUpdateCQLResult updateCQLLookUpTag(String xml, CQLQualityDataSetDTO modifyWithDTO,
                                           CQLQualityDataSetDTO modifyDTO);

	SaveUpdateCQLResult deleteValueSet(String xml, String toBeDelValueSetId);

	int countNumberOfAssociation(String associatedWithId);

	SaveUpdateCQLResult parseCQLLibraryForErrors(CQLModel cqlModel);

	List<CQLLibraryAssociation> getAssociations(String id);

	SaveUpdateCQLResult saveCQLCodes(String xml, MatCodeTransferObject codeTransferObject);

	CQLCodeWrapper getCQLCodes(String xmlString);

	SaveUpdateCQLResult deleteCode(String xml, String toBeDeletedCodeId);

	SaveUpdateCQLResult saveCQLCodeSystem(String xml, CQLCodeSystem codeSystem);

	SaveUpdateCQLResult getCQLLibraryData(String xmlString);

	SaveUpdateCQLResult getCQLDataForLoad(String xmlString);
	
	String createIncludeLibraryXML(CQLIncludeLibrary includeLibrary) throws MarshalException, ValidationException, IOException, MappingException;
}
