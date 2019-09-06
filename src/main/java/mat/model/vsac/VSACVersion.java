package mat.model.vsac;

/**
 * The Class VSACVersion.
 */
public class VSACVersion {

	/** The name. */
	private String name;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public int getSortOrder() {
		return 0;
	}

	public String getValue() {
		return name;
	}

	public String getItem() {
		return name;
	}
	
	
}
