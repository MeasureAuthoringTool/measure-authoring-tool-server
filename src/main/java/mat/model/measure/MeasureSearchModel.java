package mat.model.measure;

import java.util.ArrayList;
import java.util.List;

import mat.model.SearchModel;


public class MeasureSearchModel extends SearchModel {
	public static final String ONLY_MY_MEASURE = "Only My Measures";
	public static final String VERSION_MEASURE = "Versioned Measures";
	public static final String DRAFT_MEASURE ="Draft Measures";
	public static final String PATIENT_BASED = "Yes, Patient-based";
	public static final String NOT_PATIENT_BASED = "No, Not Patient-based";

	private Boolean omitCompositeMeasure;

	private String qdmVersion;
	private String measureSetId;

	private List<String> scoringTypes;
	
	private PatientBasedType patientBasedType;
	
	public enum PatientBasedType {ALL, PATIENT, NOT_PATIENT}
	
	public MeasureSearchModel() {
		this.searchTerm = "";
		this.versionType = VersionType.ALL;
		this.scoringTypes = new ArrayList<>();
		this.patientBasedType = PatientBasedType.ALL;
		this.modifiedDate = 0;
		this.modifiedOwner = "";
		this.owner = "";
		this.startIndex = 1;
		this.pageSize = Integer.MAX_VALUE;
		this.isMyMeasureSearch = SearchModel.MY;
	}

	public MeasureSearchModel(int myMeasureSearch, int startIndex, int pageSize, String searchTerm) {
		this();
		
		this.isMyMeasureSearch = myMeasureSearch;
		this.startIndex = startIndex;
		this.pageSize = pageSize;
		this.searchTerm = searchTerm;
	}

	public PatientBasedType isPatientBased() {
		return patientBasedType;
	}
	public void setPatientBased(PatientBasedType patientBased) {
		patientBasedType = patientBased;
	}

	public List<String> getScoringTypes() {
		return scoringTypes;
	}

	public void setScoringTypes(List<String> scoringTypes) {
		this.scoringTypes = scoringTypes;
	}

	public String getQdmVersion() {
		return qdmVersion;
	}

	public void setQdmVersion(String qdmVersion) {
		this.qdmVersion = qdmVersion;
	}
	
	public Boolean isOmitCompositeMeasure() {
		return omitCompositeMeasure;
	}

	public void setOmitCompositeMeasure(Boolean omitCompositeMeasure) {
		this.omitCompositeMeasure = omitCompositeMeasure;
	}
	
	public String getMeasureSetId() {
		return measureSetId;
	}

	public void setMeasureSetId(String measureSetId) {
		this.measureSetId = measureSetId;
	}

	@Override
	public void reset() {
		super.reset();
		scoringTypes = new ArrayList<>();
		patientBasedType = PatientBasedType.ALL;
	}
}
