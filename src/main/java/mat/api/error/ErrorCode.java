package mat.api.error;

public enum ErrorCode {
	MISSING("missing"),
    MISSING_FIELD("missing_field"),
    INVALID("invalid");

    private String value;

    ErrorCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
