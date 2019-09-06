package mat.api.libraries.dto;

import mat.api.OwnerDTO;
import mat.model.cql.CQLLibraryDataSetObject;

public class LibraryDTO {
	private String id;
	private String familyId;
	
	private String name;
	
	private String version;
	private String releaseVersion;
	
	private OwnerDTO owner;
	
	public LibraryDTO(String id, String familyId, String name, String version, String releaseVersion, OwnerDTO owner) {
		this.id = id;
		this.familyId = familyId;
		this.name = name;
		this.version = version;
		this.releaseVersion = releaseVersion;
		this.owner = owner;
	}
	
	public LibraryDTO(CQLLibraryDataSetObject cqlLibraryDataSetObject) {		
		this(cqlLibraryDataSetObject.getId(), cqlLibraryDataSetObject.getCqlSetId(),
			cqlLibraryDataSetObject.getCqlName(), cqlLibraryDataSetObject.getVersion().replace("Draft v", ""), 
			cqlLibraryDataSetObject.getReleaseVersion().replace("v", ""),
			new OwnerDTO(cqlLibraryDataSetObject.getId(), cqlLibraryDataSetObject.getOwnerFirstName() + " " + cqlLibraryDataSetObject.getOwnerLastName()));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getReleaseVersion() {
		return releaseVersion;
	}

	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

	public OwnerDTO getOwner() {
		return owner;
	}

	public void setOwner(OwnerDTO owner) {
		this.owner = owner;
	}
	
	
}
