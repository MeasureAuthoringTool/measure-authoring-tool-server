package mat.model.valueset;

import java.util.List;

import mat.dto.CodeListSearchDTO;
import mat.model.BaseModel;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.cql.CQLValueSetTransferObjectValidator;
import mat.model.valueset.MatValueSet;

/**
 * The Class CQLValueSetTransferObject.
 */
public class CQLValueSetTransferObject implements BaseModel {
	
	/** The applied qdm list. */
	List<CQLQualityDataSetDTO> appliedQDMList;
	
	/** The code list search dto. */
	CodeListSearchDTO codeListName;
	
	/** The mat value set. */
	MatValueSet matValueSet;
	
	/** The measure id. */
	String measureId;
	
	String cqlLibraryId;
	
	/** The quality data set dto. */
	CQLQualityDataSetDTO cqlQualityDataSetDTO;
	
	/** The user defined text. */
	String userDefinedText;
	
	/** The is version. */
	boolean isVersion;

	/**
	 * Gets the applied QDM list.
	 *
	 * @return the applied QDM list
	 */
	public List<CQLQualityDataSetDTO> getAppliedQDMList() {
		return appliedQDMList;
	}

	/**
	 * Sets the applied QDM list.
	 *
	 * @param appliedQDMList the new applied QDM list
	 */
	public void setAppliedQDMList(List<CQLQualityDataSetDTO> appliedQDMList) {
		this.appliedQDMList = appliedQDMList;
	}

	/**
	 * Gets the code list name.
	 *
	 * @return the code list name
	 */
	public CodeListSearchDTO getCodeListSearchDTO() {
		return codeListName;
	}

	/**
	 * Sets the code list name.
	 *
	 * @param codeListName the new code list name
	 */
	public void setCodeListSearchDTO(CodeListSearchDTO codeListName) {
		this.codeListName = codeListName;
	}

	/**
	 * Gets the mat value set.
	 *
	 * @return the mat value set
	 */
	public MatValueSet getMatValueSet() {
		return matValueSet;
	}

	/**
	 * Sets the mat value set.
	 *
	 * @param matValueSet the new mat value set
	 */
	public void setMatValueSet(MatValueSet matValueSet) {
		this.matValueSet = matValueSet;
	}

	/**
	 * Gets the measure id.
	 *
	 * @return the measure id
	 */
	public String getMeasureId() {
		return measureId;
	}

	/**
	 * Sets the measure id.
	 *
	 * @param measureId the new measure id
	 */
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	/**
	 * Gets the cql quality data set dto.
	 *
	 * @return the cql quality data set dto
	 */
	public CQLQualityDataSetDTO getCqlQualityDataSetDTO() {
		return cqlQualityDataSetDTO;
	}

	/**
	 * Sets the cql quality data set dto.
	 *
	 * @param cqlQualityDataSetDTO the new cql quality data set dto
	 */
	public void setCqlQualityDataSetDTO(CQLQualityDataSetDTO cqlQualityDataSetDTO) {
		this.cqlQualityDataSetDTO = cqlQualityDataSetDTO;
	}

	/**
	 * Gets the user defined text.
	 *
	 * @return the user defined text
	 */
	public String getUserDefinedText() {
		return userDefinedText;
	}

	/**
	 * Sets the user defined text.
	 *
	 * @param userDefinedText the new user defined text
	 */
	public void setUserDefinedText(String userDefinedText) {
		this.userDefinedText = userDefinedText;
	}

	/**
	 * Checks if is version.
	 *
	 * @return true, if is version
	 */
	public boolean isVersion() {
		return isVersion;
	}

	/**
	 * Sets the version.
	 *
	 * @param isVersion the new version
	 */
	public void setVersion(boolean isVersion) {
		this.isVersion = isVersion;
	}
	
	
	@Override
	public void scrubForMarkUp() {
		String markupRegExp = "<[^>]+>";
		if ((this.getUserDefinedText() != null) && !this.getUserDefinedText().isEmpty()) {
			String noMarkupText = this.getUserDefinedText().trim().replaceAll(markupRegExp, "");
			//System.out.println("QDM User defined name:" + noMarkupText);
			if (this.getUserDefinedText().trim().length() > noMarkupText.length()) {
				this.setUserDefinedText(noMarkupText);
			}
		}
		if (this.getMatValueSet() != null && this.getMatValueSet().getDisplayName()!= null) {
			String noMarkupText = this.getMatValueSet().getDisplayName().trim().replaceAll(markupRegExp, "");
			//System.out.println("QDM VSAC Value set name:" + noMarkupText);
			if (this.getMatValueSet().getDisplayName().trim().length() > noMarkupText.length()) {
				this.getMatValueSet().setDisplayName(noMarkupText);
			}
		}
		if (this.getCqlQualityDataSetDTO() != null) {
			String noMarkupText = this.getCqlQualityDataSetDTO().getName().trim().replaceAll(markupRegExp, "");
			//System.out.println("QDM To Be Modified VSAC Value set name:" + noMarkupText);
			if (this.getCqlQualityDataSetDTO().getName().trim().length() > noMarkupText.length()) {
				this.getCqlQualityDataSetDTO().setName(noMarkupText);
			}
		}
		if (this.getCodeListSearchDTO() != null) {
			String noMarkupText = this.getCodeListSearchDTO().getName().trim().replaceAll(markupRegExp, "");
			//System.out.println("QDM To Be Modified USD Value set name:" + noMarkupText);
			if (this.getCodeListSearchDTO().getName().trim().length() > noMarkupText.length()) {
				this.getCodeListSearchDTO().setName(noMarkupText);
			}
		}
	}
	
	public boolean validateModel(){		
		CQLValueSetTransferObjectValidator validator = new CQLValueSetTransferObjectValidator();
		
		return validator.isValid(this);
	}

	public String getCqlLibraryId() {
		return cqlLibraryId;
	}

	public void setCqlLibraryId(String cqlLibraryId) {
		this.cqlLibraryId = cqlLibraryId;
	}
	

}
