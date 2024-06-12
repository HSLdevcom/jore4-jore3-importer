package fi.hsl.jore.importer.feature.mapmatching.dto.response;

/**
 * Identifies the source system of an infrastructure link and specifies the link's id in the source system. This
 * information is returned by the map matching API.
 */
public class ExternalLinkRefDTO {

    private String infrastructureSource;
    private String externalLinkId;

    public String getInfrastructureSource() {
        return infrastructureSource;
    }

    public String getExternalLinkId() {
        return externalLinkId;
    }

    public void setInfrastructureSource(final String infrastructureSource) {
        this.infrastructureSource = infrastructureSource;
    }

    public void setExternalLinkId(final String externalLinkId) {
        this.externalLinkId = externalLinkId;
    }
}
