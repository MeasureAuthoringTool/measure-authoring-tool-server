package mat.api.error.exception;

import java.util.ArrayList;
import java.util.List;

import mat.api.error.dto.FieldErrorDTO;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;
	private List<FieldErrorDTO> errors;
	
	public ValidationException(String message) {
		super(message);
		this.errors = new ArrayList<>();
	}
	
	public ValidationException(String message, List<FieldErrorDTO> errors) {
		super(message);
		this.errors = errors;
	}
	
	public ValidationException(String message, FieldErrorDTO error) {
		super(message);
		this.errors = new ArrayList<>();
		errors.add(error);
	}

	public List<FieldErrorDTO> getErrors() {
		return errors;
	}

	public void setErrors(List<FieldErrorDTO> errors) {
		this.errors = errors;
	}
}
