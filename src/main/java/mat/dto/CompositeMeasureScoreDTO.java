package mat.dto;


public class CompositeMeasureScoreDTO {
	private String id;
	private String score;
	public CompositeMeasureScoreDTO(){
		
	}
	
	public CompositeMeasureScoreDTO(String id, String score) {
		super();
		this.id = id;
		this.score = score;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return id;
	}
	public String getItem() {
		return score;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public int getSortOrder() {
		return 0;
	}
	
}
