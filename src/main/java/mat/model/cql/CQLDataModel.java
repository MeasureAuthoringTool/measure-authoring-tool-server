package mat.model.cql;


public class CQLDataModel {
	private String name;
	private String qdmVersion;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the qdmVersion
	 */
	public String getQdmVersion() {
		return qdmVersion;
	}

	/**
	 * @param qdmVersion the qdmVersion to set
	 */
	public void setQdmVersion(String qdmVersion) {
		this.qdmVersion = qdmVersion;
	}

}
