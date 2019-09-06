package mat.api.measures.validator;

import mat.api.error.ErrorCode;
import mat.api.measures.dto.CreateMeasureDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CreateNewMeasureValidator implements Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        return CreateMeasureDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CreateMeasureDTO createMeasureDTO = (CreateMeasureDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", ErrorCode.MISSING_FIELD.getValue(), "Measure name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eCQMAbbreviatedTitle", ErrorCode.MISSING_FIELD.getValue(), "eCQM Abbreviated Title is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "measureScoring", ErrorCode.MISSING_FIELD.getValue(), "Measure Scoring is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "isPatientBased", ErrorCode.MISSING_FIELD.getValue(), "Patient-based Indicator is required.");

        if(StringUtils.isNotEmpty(createMeasureDTO.getName())) {
            if(!hasAtleastOneLetter(createMeasureDTO.getName())) {
                errors.rejectValue("name", ErrorCode.INVALID.getValue(), "Measure Name must contain at least one letter.");
            }
        }

        if(StringUtils.isNotEmpty(createMeasureDTO.geteCQMAbbreviatedTitle())) {
            if(!hasAtleastOneLetter(createMeasureDTO.geteCQMAbbreviatedTitle())) {
                errors.rejectValue("eCQMAbbreviatedTitle", ErrorCode.INVALID.getValue(), "eCQM Abbreviated Title must contain at least one letter.");
            }
        }

        if(StringUtils.isNotEmpty(createMeasureDTO.getMeasureScoring())) {
            if(!isValidMeasureScoring(createMeasureDTO.getMeasureScoring())) {
                errors.rejectValue("measureScoring", ErrorCode.INVALID.getValue(), "Measure Scoring must be either Cohort, Continuous Variable, Ratio, or Proportion.");
            }

            if(!isValidPatientBasedIndicator(createMeasureDTO.getMeasureScoring(), createMeasureDTO.getIsPatientBased())) {
                errors.rejectValue("isPatientBased", ErrorCode.INVALID.getValue(), "Patient-based Indicator must be false when using Continuous Variable measure scoring.");
            }
        }
    }

    private boolean isValidPatientBasedIndicator(String s, boolean isPatientBased) {
        if("Continuous Variable".equals(s) && isPatientBased) {
            return false;
        }

        return true;
    }

    private boolean isValidMeasureScoring(String scoring) {
        return scoring.equals("Cohort") || scoring.equals("Proportion") || scoring.equals("Continuous Variable") || scoring.equals("Ratio");
    }

    private boolean hasAtleastOneLetter(String s) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if(Character.isAlphabetic(c)) {
                return true;
            }
        }

        return false;
    }
}
