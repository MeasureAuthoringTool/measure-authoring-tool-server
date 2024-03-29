package mat.model.cql;

import java.util.ArrayList;
import java.util.List;

import mat.dto.RiskAdjustmentDTO;

public class CQLDefinitionsWrapper {

	
	private List<CQLDefinition> cqlDefinitions = new ArrayList<CQLDefinition>();
	
	private List<RiskAdjustmentDTO> riskAdjVarDTOList = new ArrayList<RiskAdjustmentDTO>();

	public List<CQLDefinition> getCqlDefinitions() {
		return cqlDefinitions;
	}

	public void setCqlDefinitions(List<CQLDefinition> cqlDefinitions) {
		this.cqlDefinitions = cqlDefinitions;
	}

	public List<RiskAdjustmentDTO> getRiskAdjVarDTOList() {
		return riskAdjVarDTOList;
	}

	public void setRiskAdjVarDTOList(List<RiskAdjustmentDTO> riskAdjVarDTOList) {
		this.riskAdjVarDTOList = riskAdjVarDTOList;
	}
}
