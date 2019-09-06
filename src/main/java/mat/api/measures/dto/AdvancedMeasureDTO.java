package mat.api.measures.dto;

import mat.api.OwnerDTO;
import mat.model.measure.Measure;

public class AdvancedMeasureDTO extends MeasureDTO {
    private Integer eMeasureId;

    private String qdmVersion;

    private Boolean isPatientBased;
    private String measureScoring;

    private Boolean isDraft;
    private Boolean isEditable;
    private Boolean isCloneable;
    private Boolean isExportable;

	private boolean isDraftable;

    public AdvancedMeasureDTO() {
    }

    public AdvancedMeasureDTO(Measure measure, boolean isEditable, boolean isDraftable, boolean isCloneable, boolean isExportable) {
        super(measure);
        this.eMeasureId = measure.geteMeasureId();
        this.qdmVersion = measure.getQdmVersion();
        this.isPatientBased = measure.getPatientBased();
        this.measureScoring = measure.getMeasureScoring();
        this.isDraft = measure.isDraft();
        this.isDraftable = isDraftable;
        this.isEditable = isEditable;
        this.isCloneable = isCloneable;
        this.isExportable = isExportable;
    }

    public AdvancedMeasureDTO(String id, String familyId, String name, String eCQMAbbreviatedTitle, Integer eMeasureId,
                              String version, String revisionNumber, String releaseVersion, String qdmVersion, Boolean isPatientBased,
                              String measureScoring, Boolean isDraft, Boolean isEditable, Boolean isDraftable, Boolean isCloneable,
                              Boolean isExportable, OwnerDTO owner) {

        super(id, familyId, name, eCQMAbbreviatedTitle, version, revisionNumber, releaseVersion, owner, isDraft);
        this.eMeasureId = eMeasureId;
        this.qdmVersion = qdmVersion;
        this.isDraftable = isDraftable;
        this.isPatientBased = isPatientBased;
        this.measureScoring = measureScoring;
        this.isDraft = isDraft;
        this.isEditable = isEditable;
        this.isCloneable = isCloneable;
        this.isExportable = isExportable;
    }

    public Integer geteMeasureId() {
        return eMeasureId;
    }

    public void seteMeasureId(int eMeasureId) {
        this.eMeasureId = eMeasureId;
    }

    public String getQdmVersion() {
        return qdmVersion;
    }

    public void setQdmVersion(String qdmVersion) {
        this.qdmVersion = qdmVersion;
    }

    public Boolean isPatientBased() {
        return isPatientBased;
    }

    public void setPatientBased(boolean patientBased) {
        isPatientBased = patientBased;
    }

    public String getMeasureScoring() {
        return measureScoring;
    }

    public void setMeasureScoring(String measureScoring) {
        this.measureScoring = measureScoring;
    }

    public Boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }

    public Boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public Boolean isCloneable() {
        return isCloneable;
    }

    public void setCloneable(boolean cloneable) {
        isCloneable = cloneable;
    }

    public Boolean isExportable() {
        return isExportable;
    }

    public void setExportable(boolean exportable) {
        isExportable = exportable;
    }

	public Boolean getIsPatientBased() {
		return isPatientBased;
	}

	public void setIsPatientBased(Boolean isPatientBased) {
		this.isPatientBased = isPatientBased;
	}

	public boolean isDraftable() {
		return isDraftable;
	}

	public void setDraftable(boolean isDraftable) {
		this.isDraftable = isDraftable;
	}
}
