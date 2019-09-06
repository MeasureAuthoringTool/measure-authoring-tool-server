package mat.dto;

public class AuthorDTO {
	private String id;
	private String authorName;

	public AuthorDTO(){
		
	}
	
	public AuthorDTO(String id, String authorName) {
		super();
		this.id = id;
		this.authorName = authorName;
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
		return authorName;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public int getSortOrder() {
		return 0;
	}
}
