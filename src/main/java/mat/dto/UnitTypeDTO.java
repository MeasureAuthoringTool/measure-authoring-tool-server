package mat.dto;

public class UnitTypeDTO {
	private String id;
	private String unitType;
	public UnitTypeDTO(){
		
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
		// TODO Auto-generated method stub
		return unitType;
	}
	public void setItem(String unitType) {
		this.unitType = unitType;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
}
