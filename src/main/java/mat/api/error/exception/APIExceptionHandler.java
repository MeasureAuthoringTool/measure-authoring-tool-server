package mat.api.error.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import mat.api.error.dto.APIErrorDTO;
import mat.api.error.dto.ValidationErrorDTO;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { NotFoundException.class })
	public ResponseEntity<Object> handleNotFoundException(NotFoundException e, WebRequest request) {
		APIErrorDTO error = new APIErrorDTO(e.getMessage(), getURI(request), 404);
		return handleExceptionInternal(e, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler(value = { ValidationException.class })
	public ResponseEntity<Object> handleValidationException(ValidationException e, WebRequest request) {
		ValidationErrorDTO error = new ValidationErrorDTO(e.getMessage(), getURI(request), 422, e.getErrors());
		return handleExceptionInternal(e, error, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
	}
	
	private String getURI(WebRequest request) {
		return request.getDescription(false).replace("uri=", "");
	}
	
}
