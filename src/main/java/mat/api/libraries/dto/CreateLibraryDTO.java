package mat.api.libraries.dto;


public class CreateLibraryDTO {

	private String name; 
	
	public CreateLibraryDTO(String name) {
		this.name = name;
	}
	
	public CreateLibraryDTO() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
