package mat.cql;

import mat.model.cql.CQLIncludeLibrary;

public class LibHolderObject {
	String measureXML = "";
	
	CQLIncludeLibrary cqlLibrary;
	
	public LibHolderObject(){
		
	}
	
	public LibHolderObject(String includeCqlXMLString,
			CQLIncludeLibrary cqlIncludeLibrary) {
		this.measureXML = includeCqlXMLString;
		this.cqlLibrary = cqlIncludeLibrary;
	}
	
	public CQLIncludeLibrary getCqlLibrary() {
		return cqlLibrary;
	}
	public void setCqlLibrary(CQLIncludeLibrary cqlLibrary) {
		this.cqlLibrary = cqlLibrary;
	}
	public String getMeasureXML() {
		return measureXML;
	}
	public void setMeasureXML(String measureXML) {
		this.measureXML = measureXML;
	}
	 
}
