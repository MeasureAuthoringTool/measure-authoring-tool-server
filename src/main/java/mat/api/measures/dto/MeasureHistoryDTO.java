package mat.api.measures.dto;

public class MeasureHistoryDTO {

	private String action;
	private String by;
	private String date;
	private String additionalInformation;
	
	public MeasureHistoryDTO(String action, String by, String date, String additionalInformation) {
		this.action = action;
		this.by = by;
		this.date = date;
		this.additionalInformation = additionalInformation;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
}
