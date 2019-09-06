package mat.dto;



public class UnitMatrixDTO {
	private String id;
	private String unitId;
	public UnitMatrixDTO(){
		
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
		return unitId;
	}

	public void setItem(String unitId) {
		this.unitId = unitId;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public int getSortOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
}
