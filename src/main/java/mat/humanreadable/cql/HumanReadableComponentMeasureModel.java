package mat.humanreadable.cql;

public class HumanReadableComponentMeasureModel {

	private String name; 
	private String version; 
	private String measureSetId;
	
	public HumanReadableComponentMeasureModel() {
		
	}
	
	public HumanReadableComponentMeasureModel(String measureName, String versionNumber, String measureSetId) {
		this.name = measureName;
		this.version = versionNumber;
		this.measureSetId = measureSetId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String measureName) {
		this.name = measureName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String versionNumber) {
		this.version = versionNumber;
	}
	public String getMeasureSetId() {
		return measureSetId;
	}
	public void setMeasureSetId(String measureSetId) {
		this.measureSetId = measureSetId;
	} 	
}
