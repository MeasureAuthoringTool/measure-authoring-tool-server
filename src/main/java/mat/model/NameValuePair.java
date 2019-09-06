package mat.model;


public class NameValuePair {

	public static class Comparator implements java.util.Comparator<NameValuePair> {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(NameValuePair o1, NameValuePair o2) {
			return o1.getName().compareTo(o2.getName());
		}
		
	}
	
	/** The name. */
	private String name;
	
	/** The value. */
	private String value;
	
	/**
	 * Instantiates a new name value pair.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public NameValuePair(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Instantiates a new name value pair.
	 */
	public NameValuePair() {
	}
	
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
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
