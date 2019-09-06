package mat.dto;

public class DataTypeDTO {
	private String id;
	private String description;
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
		return description;
	}
	public int getSortOrder() {
		return 0;
	}
	public DataTypeDTO(String id, String description) {
		super();
		this.id = id;
		this.description = description;
	}

}
