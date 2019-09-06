package mat.model.code;

public class DirectReferenceCode {
	private String codeDescriptor;
	private String codeSystemName;
	private String codeSystemVersion;
	private String codeSystemOid;
	private String code;
	public String getCodeDescriptor() {
		return codeDescriptor;
	}
	public void setCodeDescriptor(String codeDescriptor) {
		this.codeDescriptor = codeDescriptor;
	}
	public String getCodeSystemName() {
		return codeSystemName;
	}
	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}
	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}
	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeSystemOid() {
		return codeSystemOid;
	}
	public void setCodeSystemOid(String codeSystemOid) {
		this.codeSystemOid = codeSystemOid;
	}
	
}
