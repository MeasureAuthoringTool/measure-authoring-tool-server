package mat.api.error;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import mat.api.error.dto.FieldErrorDTO;

import java.util.ArrayList;
import java.util.List;

public class ValidatorErrorUtility {

    public static List<FieldErrorDTO> convertResultToValidationError(BindingResult result) {
        List<FieldErrorDTO> errors = new ArrayList<>();
        for(FieldError fieldError: result.getFieldErrors()) {
            FieldErrorDTO error = new FieldErrorDTO(fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage());
            errors.add(error);
        }

        return errors;
    }
}
