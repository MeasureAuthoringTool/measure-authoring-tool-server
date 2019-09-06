package mat.api.measures.dto;

import mat.api.OwnerDTO;
import mat.model.measure.Measure;

public class MeasureDTO {
    private String id;
    private String familyId;

    private String name;
    private String eCQMAbbreviatedTitle;

    private String version;
    private String releaseVersion;

    private OwnerDTO owner;

    public MeasureDTO() {
    }

    public MeasureDTO(Measure measure) {
       this(measure.getId(), measure.getMeasureSet().getId(), measure.getDescription(), measure.getaBBRName(),
               measure.getMajorVersionInt() + "." + measure.getMinorVersionInt(), measure.getRevisionNumber(), measure.getReleaseVersion(),
               new OwnerDTO(measure.getOwner().getId(), measure.getOwner().getFullName()), measure.isDraft());
    }

    public MeasureDTO(String id, String familyId, String name, String eCQMAbbreviatedTitle, String version,
                      String revisionNumber, String releaseVersion,  OwnerDTO owner, boolean isDraft) {
        this.id = id;
        this.familyId = familyId;
        this.name = name;
        this.eCQMAbbreviatedTitle = eCQMAbbreviatedTitle;
        this.releaseVersion = releaseVersion.replace("v", "");
        this.owner = owner;

        
        
        version = Double.parseDouble(version) + "";
        if(isDraft) {
            version = version + "." + revisionNumber;
        }

        this.version = version;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public OwnerDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }
}
