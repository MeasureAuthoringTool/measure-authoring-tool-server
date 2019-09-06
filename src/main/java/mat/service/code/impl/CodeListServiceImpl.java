package mat.service.code.impl;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mat.result.SaveUpdateCodeListResult;
import mat.constants.ConstantMessages;
import mat.dto.CodeListSearchDTO;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.emory.mathcs.backport.java.util.Collections;
import mat.dto.OperatorDTO;
import mat.dto.UnitDTO;
import mat.model.valueset.MatValueSetTransferObject;
import mat.dto.QualityDataSetDTO;
import mat.service.code.CodeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("serial")
@Service
public class CodeListServiceImpl implements CodeListService {
	@Autowired CodeListService codeListService;
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(CodeListServiceImpl.class);

	@Override
	public int countSearchResultsWithFilter(String searchText, boolean defaultCodeList, int filter) {
		//TODO why is this even here?
		return 0;
	}

	@Override
	public List<CodeListSearchDTO> search(String searchText, int startIndex, int pageSize, String sortColumn, boolean isAsc, boolean defaultCodeList, int filter) {
		return null; //TODO implement this
	}

	@Override
	public List<?> getAllDataTypes() {
		List<?> ret = getCodeListService().getAllDataTypes();
		return ret;
	}

	@Override
	public List<OperatorDTO> getAllOperators() {
		return getCodeListService().getAllOperators();
	}	
		
	/**
	 * Gets the code list service.
	 * 
	 * @return the code list service
	 */
	public CodeListService getCodeListService() {
		return codeListService;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getListBoxData()
	 */
	@Override
	public CodeListService.ListBoxData getListBoxData() {
		logger.info("getListBoxData");
		CodeListService.ListBoxData data = getCodeListService().getListBoxData();
		return data;
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getQDSDataTypeForCategory(java.lang.String)
	 */
	@Override
	public List<?> getQDSDataTypeForCategory(String category) {
		return getCodeListService().getQDSDataTypeForCategory(category);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getQDSElements(java.lang.String, java.lang.String)
	 */
	@Override
	public List<QualityDataSetDTO> getQDSElements(String measureId,
			String version) {
		List<QualityDataSetDTO> qdsElements = getCodeListService().getQDSElements(measureId, version);
		List<QualityDataSetDTO> filteredQDSElements = new ArrayList<QualityDataSetDTO>();
		for(QualityDataSetDTO dataSet : qdsElements) {
			if((dataSet.getOid() != null) && !dataSet.getOid().equals(ConstantMessages.GENDER_OID)
					&& !dataSet.getOid().equals(ConstantMessages.RACE_OID) && !dataSet.getOid().equals(ConstantMessages.ETHNICITY_OID)
					&& !dataSet.getOid().equals(ConstantMessages.PAYER_OID)){
				filteredQDSElements.add(dataSet);
			} else {
				System.out.println();
			}
			
		}
		Collections.sort(filteredQDSElements, new Comparator<QualityDataSetDTO>() {
			@Override
			public int compare(QualityDataSetDTO o1, QualityDataSetDTO o2) {
				return o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName());
			}
		});
		return filteredQDSElements;
	}
	
	

	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	/*private UserService getUserService() {
		return (UserService)context.getBean("userService");
	}*/
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.codelist.service.CodeListService#saveQDStoMeasure(mat.model
	 * .MatValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCodeListResult saveQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject) {
		return getCodeListService().saveQDStoMeasure(matValueSetTransferObject);
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.codelist.service.CodeListService#saveUserDefinedQDStoMeasure
	 * (mat.model.valueset.MatValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCodeListResult saveUserDefinedQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject) {
		return getCodeListService().saveUserDefinedQDStoMeasure(matValueSetTransferObject);
	}
	

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.codelist.service.CodeListService#updateCodeListToMeasure(mat
	 * .model.MatValueSetTransferObject)
	 */
	public SaveUpdateCodeListResult updateCodeListToMeasure(MatValueSetTransferObject matValueSetTransferObject) {
		matValueSetTransferObject.scrubForMarkUp();
		return getCodeListService().updateQDStoMeasure(matValueSetTransferObject);
	}

	@Override
	public final SaveUpdateCodeListResult updateQDStoMeasure(
			MatValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCodeListResult result = null;
		//TODO implement this?
/*		matValueSetTransferObject.scrubForMarkUp();
		if (matValueSetTransferObject.getMatValueSet() != null) {
			result = updateVSACValueSetInElementLookUp(matValueSetTransferObject);
		} else if (matValueSetTransferObject.getCodeListSearchDTO() != null) {
			result = updateUserDefineQDMInElementLookUp(matValueSetTransferObject);
		}*/
		return result;
	}

	public List<UnitDTO> getAllCqlUnits() {
		logger.info("getAllCqlUnits");
		List<UnitDTO> data =  getCodeListService().getAllUnits();
		
		return data;
	}

	@Override
	public List<UnitDTO> getAllUnits() {
		return null; //TODO fix this
	}
}