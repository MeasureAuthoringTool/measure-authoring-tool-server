package mat.dto;

public class UnitDTO {
	private String id;
	private String unit;
	private String cqlunit;
	private int sortOrder;

	public UnitDTO(){
		
	}
	
	public UnitDTO(String id, String unit, String cqlunit, int sortOrder) {
		super();
		this.id = id;
		this.unit = unit;
		this.cqlunit = cqlunit;
		this.sortOrder = sortOrder;
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
		return unit;
	}
	public void setItem(String unit) {
		this.unit = unit;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCqlunit() {
		return cqlunit;
	}
	public void setCqlunit(String cqlunit) {
		this.cqlunit = cqlunit;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	
}
