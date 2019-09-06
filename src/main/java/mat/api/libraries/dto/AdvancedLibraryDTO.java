package mat.api.libraries.dto;

import mat.api.OwnerDTO;
import mat.model.cql.CQLLibraryDataSetObject;

public class AdvancedLibraryDTO extends LibraryDTO {
	
	private String qdmVersion;
		
	public AdvancedLibraryDTO(CQLLibraryDataSetObject cqlLibraryDataSetObject) {		
		this(cqlLibraryDataSetObject.getId(), cqlLibraryDataSetObject.getCqlSetId(),
			cqlLibraryDataSetObject.getCqlName(), cqlLibraryDataSetObject.getVersion().replace("Draft v", ""), 
			cqlLibraryDataSetObject.getQdmVersion(), cqlLibraryDataSetObject.getReleaseVersion().replace("v", ""),
			new OwnerDTO(cqlLibraryDataSetObject.getId(), cqlLibraryDataSetObject.getOwnerFirstName() + " " + cqlLibraryDataSetObject.getOwnerLastName()));
	}

	public AdvancedLibraryDTO(String id, String familyId, String name, String version, 
			String qdmVersion, String releaseVersion,
			OwnerDTO owner) {
		super(id, familyId, name, version, releaseVersion, owner);
		this.qdmVersion = qdmVersion;
	}

	public String getQdmVersion() {
		return qdmVersion;
	}

	public void setQdmVersion(String qdmVersion) {
		this.qdmVersion = qdmVersion;
	}
}
