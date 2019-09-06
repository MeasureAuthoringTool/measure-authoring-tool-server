package mat.cql;

import mat.cql.error.CQLError;

import java.util.List;

public class CQLValidationResult {
	private boolean isValid;
	private List<CQLError> errorList;
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public List<CQLError> getErrorList() {
		return errorList;
	}
	public void setErrorList(List<CQLError> errorList) {
		this.errorList = errorList;
	}

}
