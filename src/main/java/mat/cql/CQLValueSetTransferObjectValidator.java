package mat.cql;

import java.util.List;

import mat.constants.ConstantMessages;
import mat.model.valueset.CQLValueSetTransferObject;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.util.StringUtility;

public class CQLValueSetTransferObjectValidator {
	public boolean isValid(CQLValueSetTransferObject object) {
		boolean isValid = true;
		if(object.getMatValueSet() != null){
			if(StringUtility.isEmptyOrNull(object.getMatValueSet().getDisplayName())) {
				isValid = false;
			} 
		} else if(object.getUserDefinedText().trim().isEmpty()){
			isValid = false;
		} 
		
		List<CQLQualityDataSetDTO> existingQDSList = object.getAppliedQDMList();
		for (CQLQualityDataSetDTO dataSetDTO : existingQDSList) {
			if (dataSetDTO.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID) && dataSetDTO
					.getName().equalsIgnoreCase(object.getCqlQualityDataSetDTO().getName())) {
				isValid = false;
				break;
			}
		}
		
		return isValid;
	}
}
