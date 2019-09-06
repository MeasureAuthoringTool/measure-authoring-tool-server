package mat.api.measures.dto;

public class CreateMeasureDTO {

    private String name;
    private String eCQMAbbreviatedTitle;
    private String measureScoring;
    private Boolean isPatientBased;

    public CreateMeasureDTO() {
    }

    public CreateMeasureDTO(String name, String eCQMAbbreviatedTitle, String measureScoring, Boolean isPatientBased) {
        this.name = name;
        this.eCQMAbbreviatedTitle = eCQMAbbreviatedTitle;
        this.measureScoring = measureScoring;
        this.isPatientBased = isPatientBased;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteCQMAbbreviatedTitle() {
        return eCQMAbbreviatedTitle;
    }

    public void seteCQMAbbreviatedTitle(String eCQMAbbreviatedTitle) {
        this.eCQMAbbreviatedTitle = eCQMAbbreviatedTitle;
    }

    public String getMeasureScoring() {
        return measureScoring;
    }

    public void setMeasureScoring(String measureScoring) {
        this.measureScoring = measureScoring;
    }

    public Boolean getIsPatientBased() {
        return isPatientBased;
    }

    public void setIsPatientBased(Boolean patientBased) {
        isPatientBased = patientBased;
    }

    @Override
    public String toString() {
        return "CreateMeasureDTO{" +
                "name='" + name + '\'' +
                ", eCQMAbbreviatedTitle='" + eCQMAbbreviatedTitle + '\'' +
                ", measureScoring='" + measureScoring + '\'' +
                ", isPatientBased=" + isPatientBased +
                '}';
    }
}
