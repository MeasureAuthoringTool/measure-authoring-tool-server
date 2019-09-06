package mat.api.measures.dto;

public class ShareMeasureDTO {

	private String userId;
	
	public ShareMeasureDTO(String userId) {
		this.userId = userId;
	}

	public ShareMeasureDTO() {
		
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	} 
}
