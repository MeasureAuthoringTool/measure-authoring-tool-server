/**
 * 
 */
package mat.validation;

import mat.constants.MessageDelegate;
import mat.model.admin.ManageOrganizationDetailModel;

import java.util.ArrayList;
import java.util.List;

public class AdminManageOrganizationModelValidator {
	
	
	/** Checks if is valid Organization detail.
	 * 
	 * @param model the model
	 * @return the list */
	public List<String> getValidationErrors(ManageOrganizationDetailModel model) {
		List<String> message = new ArrayList<String>();
		if ("".equals(model.getOrganization().trim())) {
			message.add(MessageDelegate.getInstance().getOrgRequiredMessage());
		}
		if("".equals(model.getOid().trim())) {
			message.add(MessageDelegate.getInstance().getOIDRequiredMessage());
		}
		
		if(model.getOid().length() > 50) {
			message.add(MessageDelegate.getInstance().getOIDTooLongMessage());
		}
		return message;
	}
	
	/** Checks if is valid Organization detail.
	 * 
	 * @param model the model
	 * @return boolean */
	public boolean isManageOrganizationDetailModelValid(ManageOrganizationDetailModel model) {
		boolean isModelValid = true;
		List<String> validationErrors = getValidationErrors(model);
		if(validationErrors.size() > 0) {
			isModelValid = false;
		}
		return isModelValid;
	}
}
