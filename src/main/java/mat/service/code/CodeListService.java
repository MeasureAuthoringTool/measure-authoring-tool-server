package mat.service.code;

import java.util.List;


import mat.dto.OperatorDTO;
import mat.dto.UnitDTO;
import mat.result.SaveUpdateCodeListResult;
import mat.dto.CodeListSearchDTO;
import mat.model.valueset.MatValueSetTransferObject;
import mat.dto.QualityDataSetDTO;

public interface CodeListService {
    List<CodeListSearchDTO> search(String searchText, int startIndex, int pageSize, String sortColumn,
                                   boolean isAsc, boolean defaultCodeList, int filter);

    class ListBoxData {
		private List<?> authorsList;
		private List<?> categoryList;
		private List<?> codeSystemList;
		private List<?> logicalOperatorList;
		private List<?> measureStewardList;
		private List<?> measureTypeList;
		private List<?> operatorList;
		private List<?> operatorTypeList;
		private List<?> relAssocOperatorList;
		private List<?> relTimingOperatorList;
		private List<?> scoringList;
		private List<?> statusList;
		private List<?> unitList;
		private List<?> cqlUnitList;
		private List<?> unitTypeList;
		private List<?> unitTypeMatrixList;
		
		/**
		 * Gets the authors list.
		 * 
		 * @return the authors list
		 */
		public List<?> getAuthorsList() {
			return authorsList;
		}
		
		/**
		 * Gets the category list.
		 * 
		 * @return the category list
		 */
		public List<?> getCategoryList() {
			return categoryList;
		}
		
		/**
		 * Gets the code system list.
		 * 
		 * @return the code system list
		 */
		public List<?> getCodeSystemList() {
			return codeSystemList;
		}
		
		/**
		 * Gets the logical operator list.
		 * 
		 * @return the logical operator list
		 */
		public List<?> getLogicalOperatorList() {
			return logicalOperatorList;
		}
		
		/**
		 * Gets the measure steward list.
		 * 
		 * @return the measure steward list
		 */
		public List<?> getMeasureStewardList() {
			return measureStewardList;
		}
		
		/**
		 * Gets the measure type list.
		 * 
		 * @return the measure type list
		 */
		public List<?> getMeasureTypeList() {
			return measureTypeList;
		}
		
		//US 171. Retrieve the Operators from DB
		/**
		 * Gets the operator list.
		 * 
		 * @return the operator list
		 */
		public List<?> getOperatorList() {
			return operatorList;
		}
		
		/**
		 * Gets the operator type list.
		 * 
		 * @return the operator type list
		 */
		public List<?> getOperatorTypeList() {
			return operatorTypeList;
		}
		
		/**
		 * Gets the rel assoc operator list.
		 * 
		 * @return the rel assoc operator list
		 */
		public List<?> getRelAssocOperatorList() {
			return relAssocOperatorList;
		}
		
		/**
		 * Gets the rel timing operator list.
		 * 
		 * @return the rel timing operator list
		 */
		public List<?> getRelTimingOperatorList() {
			return relTimingOperatorList;
		}
		
		//US 421. Retrieve the scoring choices from DB
		/**
		 * Gets the scoring list.
		 * 
		 * @return the scoring list
		 */
		public List<?> getScoringList() {
			return scoringList;
		}
		
		/**
		 * Gets the status list.
		 * 
		 * @return the status list
		 */
		public List<?> getStatusList() {
			return statusList;
		}
		
		//US 62. Retrieve the Units from DB
		/**
		 * Gets the unit list.
		 * 
		 * @return the unit list
		 */
		public List<?> getUnitList() {
			return unitList;
		}
		
		/**
		 * @return the cqlUnitList
		 */
		public List<?> getCqlUnitList() {
			return cqlUnitList;
		}

		/**
		 * @param cqlUnitList the cqlUnitList to set
		 */
		public void setCqlUnitList(List<?> cqlUnitList) {
			this.cqlUnitList = cqlUnitList;
		}

		/**
		 * Gets the unit type list.
		 * 
		 * @return the unit type list
		 */
		public List<?> getUnitTypeList() {
			return unitTypeList;
		}
		
		/**
		 * Gets the unit type matrix list.
		 * 
		 * @return the unit type matrix list
		 */
		public List<?> getUnitTypeMatrixList() {
			return unitTypeMatrixList;
		}
		
		/**
		 * Sets the authors list.
		 * 
		 * @param authorsList
		 *            the new authors list
		 */
		public void setAuthorsList(List<?> authorsList) {
			this.authorsList = authorsList;
		}
		
		/**
		 * Sets the category list.
		 * 
		 * @param categoryList
		 *            the new category list
		 */
		public void setCategoryList(List<?> categoryList) {
			this.categoryList = categoryList;
		}
		
		/**
		 * Sets the code system list.
		 * 
		 * @param codeSystemList
		 *            the new code system list
		 */
		public void setCodeSystemList(List<?> codeSystemList) {
			this.codeSystemList = codeSystemList;
		}
		
		/**
		 * Sets the logical operator list.
		 * 
		 * @param logicalOperatorList
		 *            the new logical operator list
		 */
		public void setLogicalOperatorList(
				List<?> logicalOperatorList) {
			this.logicalOperatorList = logicalOperatorList;
		}
		
		/**
		 * Sets the measure steward list.
		 * 
		 * @param measureStewardList
		 *            the new measure steward list
		 */
		public void setMeasureStewardList(List<?> measureStewardList) {
			this.measureStewardList = measureStewardList;
		}
		
		/**
		 * Sets the measure type list.
		 * 
		 * @param measureTypeList
		 *            the new measure type list
		 */
		public void setMeasureTypeList(List<?> measureTypeList) {
			this.measureTypeList = measureTypeList;
		}
		
		/**
		 * Sets the operator list.
		 * 
		 * @param operatorList
		 *            the new operator list
		 */
		public void setOperatorList(List<?> operatorList) {
			this.operatorList = operatorList;
		}
		
		/**
		 * Sets the operator type list.
		 * 
		 * @param operatorTypeList
		 *            the new operator type list
		 */
		public void setOperatorTypeList(List<?> operatorTypeList) {
			this.operatorTypeList = operatorTypeList;
		}
		
		/**
		 * Sets the rel assoc operator list.
		 * 
		 * @param relAssocOperatorList
		 *            the new rel assoc operator list
		 */
		public void setRelAssocOperatorList(
				List<?> relAssocOperatorList) {
			this.relAssocOperatorList = relAssocOperatorList;
		}
		
		/**
		 * Sets the rel timing operator list.
		 * 
		 * @param relTimingOperatorList
		 *            the new rel timing operator list
		 */
		public void setRelTimingOperatorList(
				List<?> relTimingOperatorList) {
			this.relTimingOperatorList = relTimingOperatorList;
		}
		
		/**
		 * Sets the scoring list.
		 * 
		 * @param scoringList
		 *            the new scoring list
		 */
		public void setScoringList(List<?> scoringList) {
			this.scoringList = scoringList;
		}
		
		/**
		 * Sets the status list.
		 * 
		 * @param statusList
		 *            the new status list
		 */
		public void setStatusList(List<?> statusList) {
			this.statusList = statusList;
		}
		
		/**
		 * Sets the unit list.
		 * 
		 * @param unitList
		 *            the new unit list
		 */
		public void setUnitList(List<?> unitList) {
			this.unitList = unitList;
		}
		
		/**
		 * Sets the unit type list.
		 * 
		 * @param unitTypeList
		 *            the new unit type list
		 */
		public void setUnitTypeList(List<?> unitTypeList) {
			this.unitTypeList = unitTypeList;
		}
		
		/**
		 * Sets the unit type matrix list.
		 * 
		 * @param unitTypeMatrixList
		 *            the new unit type matrix list
		 */
		public void setUnitTypeMatrixList(List<?> unitTypeMatrixList) {
			this.unitTypeMatrixList = unitTypeMatrixList;
		}

		
	}
	
	
	/**
	 * Gets the all data types.
	 * 
	 * @return the all data types
	 */
	List<?> getAllDataTypes();
	
	/**
	 * Gets the all operators.
	 * 
	 * @return the all operators
	 */
	List<OperatorDTO> getAllOperators();
	
	/**
	 * Gets the list box data.
	 * 
	 * @return the list box data
	 */
	public ListBoxData getListBoxData();
	
	/**
	 * Gets the qds data type for category.
	 * 
	 * @param category
	 *            the category
	 * @return the QDS data type for category
	 */
	List<?> getQDSDataTypeForCategory(String category);
	
	/**
	 * Gets the qDS elements.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param vertsion
	 *            the vertsion
	 * @return the qDS elements
	 */
	public List<QualityDataSetDTO> getQDSElements(String measureId, String vertsion);
	
	
	/**
	 * Save qds to measure.
	 * 
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @return the save update code list result
	 */
	SaveUpdateCodeListResult saveQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject);
	
	/**
	 * Save user defined qds to measure.
	 * 
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @return the save update code list result
	 */
	SaveUpdateCodeListResult saveUserDefinedQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject);
	
	
	
	/**
	 * Update code list to measure.
	 * 
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @return the save update code list result
	 */
	SaveUpdateCodeListResult updateCodeListToMeasure(MatValueSetTransferObject matValueSetTransferObject);
	
	/**
	 * Gets all of the cql units
	 * @return a list of the cql unit dtos
	 */
	List<UnitDTO> getAllCqlUnits();

	List<UnitDTO> getAllUnits();
	SaveUpdateCodeListResult updateQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject);
	int countSearchResultsWithFilter(String searchText, boolean defaultCodeList, int filter);
}
