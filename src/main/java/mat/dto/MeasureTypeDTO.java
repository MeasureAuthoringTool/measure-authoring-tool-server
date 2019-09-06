package mat.dto;


public class MeasureTypeDTO {
	private String id;
	private String Name;
	private String abbrName;

	public MeasureTypeDTO(){
		
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
		return Name;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getAbbrName() {
		return abbrName;
	}

	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}

	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
