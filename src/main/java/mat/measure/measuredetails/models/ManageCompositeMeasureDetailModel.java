package mat.measure.measuredetails.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.model.BaseModel;
import mat.model.measure.ManageMeasureSearchModel;
import mat.model.measuredetail.ManageMeasureDetailModel;
import mat.measurepackage.MeasurePackageOverview;

public class ManageCompositeMeasureDetailModel extends ManageMeasureDetailModel implements BaseModel {

	private String compositeScoringMethod;
	private String compositeScoringAbbreviation; 
	private List<ManageMeasureSearchModel.Result> appliedComponentMeasures = new ArrayList<ManageMeasureSearchModel.Result>();
	private Map<String, String> aliasMapping = new HashMap<>();
	private Map<String, MeasurePackageOverview> packageMap = new HashMap<>();

	@Override
	public void scrubForMarkUp() {
		
	}

	public String getCompositeScoringMethod() {
		return compositeScoringMethod;
	}


	public void setCompositeScoringMethod(String compositeScoringMethod) {
		this.compositeScoringMethod = compositeScoringMethod;
	}
	
	public List<ManageMeasureSearchModel.Result> getAppliedComponentMeasures() {
		return appliedComponentMeasures;
	}

	public void setAppliedComponentMeasures(List<ManageMeasureSearchModel.Result> appliedComponentMeasures) {
		this.appliedComponentMeasures = appliedComponentMeasures;
	}
	
	public Map<String, String> getAliasMapping() {
		return aliasMapping;
	}

	public void setAliasMapping(Map<String, String> aliasMapping) {
		this.aliasMapping = aliasMapping;
	}
	
	public Map<String, MeasurePackageOverview> getPackageMap() {
		return packageMap;
	}

	public void setPackageMap(Map<String, MeasurePackageOverview> packageMap) {
		this.packageMap = packageMap;
	}

	public String getCompositeScoringAbbreviation() {
		return compositeScoringAbbreviation;
	}

	public void setCompositeScoringAbbreviation(String compositeScoringAbbreviation) {
		this.compositeScoringAbbreviation = compositeScoringAbbreviation;
	}
}