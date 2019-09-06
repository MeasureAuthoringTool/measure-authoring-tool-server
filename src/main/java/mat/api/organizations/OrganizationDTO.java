package mat.api.organizations;

public class OrganizationDTO {

    private String id;
    private String name;
    private String oid;

    public OrganizationDTO() {
    }

    public OrganizationDTO(String id, String name, String oid) {
        this.id = id;
        this.name = name;
        this.oid = oid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
