package mat.model.cql;

import java.util.ArrayList;
import java.util.List;


public class CQLParametersWrapper {
	private List<CQLParameter> cqlParameterList = new ArrayList<CQLParameter>();
	
	public List<CQLParameter> getCqlParameterList() {
		return cqlParameterList;
	}
	
	public void setCqlParameterList(List<CQLParameter> cqlParameterList) {
		this.cqlParameterList = cqlParameterList;
	}
}
