/**
 * 
 */
package mat.validation;

import java.util.ArrayList;
import java.util.List;

import mat.constants.MessageDelegate;
import mat.model.admin.ManageUsersDetailModel;

public class AdminManageUserModelValidator {
	
	
	/**
	 * Checks if is valid users detail.
	 * 
	 * @param model
	 *            the model
	 * @return the list
	 */
	public List<String> isValidUsersDetail(ManageUsersDetailModel model) {
		List<String> message = new ArrayList<String>();
		if("".equals(model.getFirstName().trim())) {
			message.add(MessageDelegate.getInstance().getFirstNameRequiredMessage());
		}
		if("".equals(model.getLastName().trim())) {
			message.add(MessageDelegate.getInstance().getLastNameRequiredMessage());
		}
		if("".equals(model.getEmailAddress().trim())) {
			message.add(MessageDelegate.getInstance().getEmailIdRequired());
		}
		if("".equals(model.getPhoneNumber().trim())) {
			
			message.add(MessageDelegate.getInstance().getPhoneRequiredMessage());
		}
		
		String phoneNum = model.getPhoneNumber();
		int i, numCount;
		numCount=0;
		for(i=0;i<phoneNum.length(); i++){
			if(Character.isDigit(phoneNum.charAt(i))) {
				numCount++;
			}
		}
		if(numCount != 10) {
			message.add(MessageDelegate.getInstance().getPhoneTenDigitMessage());
		}
		
		if("".equals(model.getOrganizationId().trim())) {
			if(model.isActive()) {
				message.add(MessageDelegate.getInstance().getOrgRequiredMessage());
			}
		}
		if("".equals(model.getOid().trim())) {
			if(model.isActive()) {
				message.add(MessageDelegate.getInstance().getOIDRequiredMessage());
			}
		}
		/*if("".equals(model.getRootOid().trim())) {
			message.add(MessageDelegate.getInstance().getRootOIDRequiredMessage());
		}*/
		if(model.getFirstName().length() < 2) {
			message.add(MessageDelegate.getInstance().getFirstMinMessage());
		}
		if(model.getOid().length() > 50) {
			message.add(MessageDelegate.getInstance().getOIDTooLongMessage());
		}
		/*if(model.getRootOid().length() > 50) {
			message.add(MessageDelegate.getInstance().getRootOIDTooLongMessage());
		}*/
		return message;
	}
	
}
