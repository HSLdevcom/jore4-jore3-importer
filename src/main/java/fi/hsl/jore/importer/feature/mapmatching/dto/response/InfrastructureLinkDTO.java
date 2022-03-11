package fi.hsl.jore.importer.feature.mapmatching.dto.response;

import org.geojson.LineString;

import java.util.Map;

/**
 * Contains the information of a route path item which is returned
 * by the map matching API.
 */
public class InfrastructureLinkDTO {

    private double distance;
    private ExternalLinkRefDTO externalLinkRef;
    private LineString geometry;
    private Long infrastructureLinkId;
    private Map<String, String> infrastructureLinkName;
    private boolean isTraversalForwards;
    private double weight;

    public double getDistance() {
        return distance;
    }

    public ExternalLinkRefDTO getExternalLinkRef() {
        return externalLinkRef;
    }

    public LineString getGeometry() {
        return geometry;
    }

    public Long getInfrastructureLinkId() {
        return infrastructureLinkId;
    }

    public Map<String, String> getInfrastructureLinkName() {
        return infrastructureLinkName;
    }

    public boolean isTraversalForwards() {
        return isTraversalForwards;
    }

    public double getWeight() {
        return weight;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }

    public void setExternalLinkRef(final ExternalLinkRefDTO externalLinkRef) {
        this.externalLinkRef = externalLinkRef;
    }

    public void setGeometry(final LineString geometry) {
        this.geometry = geometry;
    }

    public void setInfrastructureLinkId(final Long infrastructureLinkId) {
        this.infrastructureLinkId = infrastructureLinkId;
    }

    public void setInfrastructureLinkName(final Map<String, String> infrastructureLinkName) {
        this.infrastructureLinkName = infrastructureLinkName;
    }

    public void setIsTraversalForwards(final boolean isTraversalForwards) {
        this.isTraversalForwards = isTraversalForwards;
    }

    public void setWeight(final double weight) {
        this.weight = weight;
    }
}
