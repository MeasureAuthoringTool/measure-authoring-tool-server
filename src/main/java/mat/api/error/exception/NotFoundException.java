package mat.api.error.exception;

public class NotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	public int statusCode; 
	public NotFoundException(String message) {
		super(message);
		this.statusCode = 404;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	
}
