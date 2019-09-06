package mat.api.user;

import mat.api.organizations.OrganizationDTO;

public class UserDTO {

    private String id;
    private String loginId;
    private String role;

    private String firstName;
    private String lastName;
    private String middleInitial;

    private String title;

    private String email;
    private String phoneNumber;

    private OrganizationDTO organization;

    public UserDTO() {
    }


    public UserDTO(String id, String firstName, String lastName, String middleInitial, OrganizationDTO organization) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleInitial = middleInitial;
		this.organization = organization;
	}

    public UserDTO(String id, String loginId, String role, String firstName, String lastName, String middleInitial,
                   String title, String email, String phoneNumber, OrganizationDTO organization) {
        this.id = id;
        this.loginId = loginId;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
        this.title = title;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.organization = organization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

	public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }
}
