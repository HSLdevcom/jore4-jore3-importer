package fi.hsl.jore.importer.feature.mapmatching.dto.response;

/** Contains the information that's returned by the map matching API when the map matching request failed. */
public class MapMatchingErrorResponseDTO {

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
