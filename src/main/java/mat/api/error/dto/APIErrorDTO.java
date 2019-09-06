package mat.api.error.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class APIErrorDTO {
	private String message;
	private String uri;
	private int statusCode;
	private String date;

	public APIErrorDTO(String message, String uri, int statusCode) {
		this.message = message;
		this.uri = uri;
		this.statusCode = statusCode;
		date = LocalDateTime.now().toString();
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getDate() {
		return date;
	}
}
