package mat.dto;


public class CategoryDTO {
	private String id;
	private String description;
	public CategoryDTO(){
		
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getValue() {
		return id;
	}

	public String getItem() {
		// TODO Auto-generated method stub
		return description;
	}

	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
}
