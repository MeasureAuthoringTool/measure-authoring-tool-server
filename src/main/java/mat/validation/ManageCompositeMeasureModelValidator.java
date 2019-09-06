package mat.validation;

import java.util.ArrayList;
import java.util.List;

import mat.measure.measuredetails.models.ManageCompositeMeasureDetailModel;
import mat.constants.MatConstants;
import mat.constants.MessageDelegate;

public class ManageCompositeMeasureModelValidator extends ManageMeasureModelValidator {
	public static final String ERR_COMPOSITE_MEASURE_SCORE_REQUIRED = "Composite Scoring Method is required. ";
	
	public List<String> validateMeasureWithClone(ManageCompositeMeasureDetailModel model, boolean isClone) {
		List<String> message = performCommonMeasureValidation(model);
		if(!isClone) {
			message.addAll(validateNQF(model));
		}

		return message;
	}
	

	private List<String> performCommonMeasureValidation(ManageCompositeMeasureDetailModel model) {
		List<String> message = new ArrayList<String>();

		if ((model.getName() == null) || "".equals(model.getName().trim())) {
			message.add(MessageDelegate.getInstance().getMeasureNameRequiredMessage());
		}
		if ((model.getShortName() == null)
				|| "".equals(model.getShortName().trim())) {
			message.add(MessageDelegate.getInstance().getAbvNameRequiredMessage());
		}
		
		String compositeScoring = model.getCompositeScoringMethod();
		if((compositeScoring == null) || !isValidValue(compositeScoring)) {
			message.add(ERR_COMPOSITE_MEASURE_SCORE_REQUIRED);
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
