package mat.api.error.dto;

import java.util.List;

public class ValidationErrorDTO extends APIErrorDTO {
    private List<FieldErrorDTO> errors;

    public ValidationErrorDTO(String message, String uri, int statusCode, List<FieldErrorDTO> errors) {
        super(message, uri, statusCode);
        this.errors = errors;
    }

    public List<FieldErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldErrorDTO> errors) {
        this.errors = errors;
    }
}
