package mat.validation;

import mat.constants.MessageDelegate;
import mat.model.valueset.CQLValueSetTransferObject;

public class ValueSetNameInputValidator {
	public String validate(CQLValueSetTransferObject matValueSetTransferObject){
		String valuesetName = matValueSetTransferObject.getUserDefinedText();
		
		if(valuesetName == null || valuesetName.isEmpty()) {
			return "Value Set Name cannot be empty.";
		}
		
		else if(valuesetName.matches(".*[\\*\\?:\\-\\|\\!\"\\+;%].*")) {
			return MessageDelegate.getInstance().getINVALID_CHARACTER_VALIDATION_ERROR();
		}
		
		else {
			return "";
		}
	}
}
