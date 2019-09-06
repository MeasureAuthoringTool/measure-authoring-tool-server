package mat.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mat.constants.MatConstants;
import mat.model.measuredetail.ManageMeasureDetailModel;
import mat.constants.MessageDelegate;
import mat.util.StringUtility;

public class ManageMeasureModelValidator {
	
	public List<String> validateMeasure(ManageMeasureDetailModel model){
		List<String> message = performCommonMeasureValidation(model);
		message.addAll(validateNQF(model));
		return message;
	}
	
	protected boolean isValidValue(String value) {
		return !value.equalsIgnoreCase("--Select--") && !value.equals("");
	}

	public List<String> validateMeasureWithClone(ManageMeasureDetailModel model, boolean isClone) {
		List<String> message = performCommonMeasureValidation(model);
		if(!isClone) {
			message.addAll(validateNQF(model));
		}
		return message;
	}
	
	protected List<String> validateNQF(ManageMeasureDetailModel model) {
		List<String> message = new ArrayList<String>();
		if(Optional.ofNullable(model.getEndorseByNQF()).orElse(false)) { 
			if(StringUtility.isEmptyOrNull(model.getNqfId())) {
				message.add(MessageDelegate.NQF_NUMBER_REQUIRED_ERROR);
			}
		}
		return message;
	}
	
	private List<String> performCommonMeasureValidation(ManageMeasureDetailModel model) {
		List<String> message = new ArrayList<String>();

		if ((model.getName() == null) || "".equals(model.getName().trim())) {
			message.add(MessageDelegate.getInstance().getMeasureNameRequiredMessage());
		}
		if ((model.getShortName() == null) || "".equals(model.getShortName().trim())) {
			message.add(MessageDelegate.getInstance().getAbvNameRequiredMessage());
		}
		
		String scoring = model.getMeasScoring();
		if ((scoring == null) || !isValidValue(model.getMeasScoring())) {
			message.add(MessageDelegate.s_ERR_MEASURE_SCORE_REQUIRED);
		}

		if((scoring.equalsIgnoreCase(MatConstants.CONTINUOUS_VARIABLE) && (model.isPatientBased()))) {
			message.add(MessageDelegate.CONTINOUS_VARIABLE_IS_NOT_PATIENT_BASED_ERROR);
		}
		return message;
	}
}
