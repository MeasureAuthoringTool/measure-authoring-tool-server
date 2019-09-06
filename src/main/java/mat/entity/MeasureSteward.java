package mat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STEWARD_ORG")
public class MeasureSteward {
	
	private String id;
	
	private String orgName;
	
	private String orgOid;

    public MeasureSteward(String valueOf, String organizationName, String organizationOID) {
    	//TODO implement this?
    }

    public MeasureSteward() {
        //TODO implement this?
    }


	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "ORG_NAME", nullable = false, length = 200)
	public String getOrgName() {
		return orgName;
	}
	
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	@Column(name = "ORG_OID", length = 100)
	public String getOrgOid() {
		return orgOid;
	}
	
	public void setOrgOid(String orgOid) {
		this.orgOid = orgOid;
	}
	

}
