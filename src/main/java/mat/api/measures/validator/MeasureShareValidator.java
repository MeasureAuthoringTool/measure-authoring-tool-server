package mat.api.measures.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import mat.api.error.ErrorCode;
import mat.api.measures.dto.ShareMeasureDTO;
import mat.model.measure.MeasureShareDTO;

public class MeasureShareValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return MeasureShareDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		ShareMeasureDTO shareMeasureDTO = (ShareMeasureDTO) o;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", ErrorCode.MISSING_FIELD.getValue(), "userId is requierd");
	}

}
