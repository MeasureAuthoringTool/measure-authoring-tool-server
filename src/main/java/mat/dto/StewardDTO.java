package mat.dto;

public class StewardDTO {
	private String id;
	private String orgName;
	public StewardDTO(){
		
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
		return orgName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public int getSortOrder() {
		return 0;
	}
}
