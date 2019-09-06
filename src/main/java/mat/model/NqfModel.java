package mat.model;


public class NqfModel {
	
	/** The root. */
	private String root;
	
	/** The extension. */
	private String extension;

	/**
	 * Gets the root.
	 * 
	 * @return the root
	 */
	public String getRoot() {
		return root;
	}

	/**
	 * Sets the root.
	 * 
	 * @param root
	 *            the new root
	 */
	public void setRoot(String root) {
		this.root = root;
	}

	/**
	 * Gets the extension.
	 * 
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Sets the extension.
	 * 
	 * @param extension
	 *            the new extension
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NqfModel [root=" + root + ", extension=" + extension + "]";
	}

}
