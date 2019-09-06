package mat.validation;

import java.util.ArrayList;
import java.util.List;
import mat.constants.MessageDelegate;
import mat.model.authentication.MyAccountModel;

/**
 * The Class MyAccountModelValidator.
 */
public class MyAccountModelValidator {

	/**
	 * Validate.
	 * 
	 * @param model
	 *            the model
	 * @return the list
	 */
	public List<String> validate(MyAccountModel model){
		List<String> message = new ArrayList<String>();
		if("".equals(model.getFirstName().trim())) {
			message.add(MessageDelegate.getInstance().getFirstNameRequiredMessage());
		}
		if(model.getFirstName().length() < 2) {
			message.add(MessageDelegate.getInstance().getFirstMinMessage());
		}
		if("".equals(model.getLastName().trim())) {
			message.add(MessageDelegate.getInstance().getLastNameRequiredMessage());
		}
		if("".equals(model.getOrganisation().trim())) {
			message.add(MessageDelegate.getInstance().getOrgRequiredMessage());
		}
		if("".equals(model.getOid().trim())) {
			message.add(MessageDelegate.getInstance().getOIDRequiredMessage());
		}
		/*if("".equals(model.getRootoid().trim())) {
			message.add(MessageDelegate.getInstance().getRootOIDRequiredMessage());
		}*/
		if("".equals(model.getEmailAddress().trim())) {
			message.add(MessageDelegate.getInstance().getEmailIdRequired());
		}
		String emailRegExp  = "^\\S+@\\S+\\.\\S+$";
		if (!(model.getEmailAddress().trim().matches(emailRegExp))){
			message.add(MessageDelegate.getInstance().getEmailIdFormatIncorrectMessage());
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
		
		return message;
	}
	
	
}
