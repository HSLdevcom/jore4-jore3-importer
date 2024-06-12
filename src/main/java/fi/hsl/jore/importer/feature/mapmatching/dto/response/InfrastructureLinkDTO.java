package fi.hsl.jore.importer.feature.mapmatching.dto.response;

import java.util.Map;
import org.geojson.LineString;

/** Contains the information of a route path item which is returned by the map matching API. */
public class InfrastructureLinkDTO {

    private ExternalLinkRefDTO externalLinkRef;
    private LineString geometry;
    private Long mapMatchingInfrastructureLinkId;
    private Map<String, String> infrastructureLinkName;
    private boolean isTraversalForwards;
    private double weight;
    private double distance;

    public double getDistance() {
        return distance;
    }

    public ExternalLinkRefDTO getExternalLinkRef() {
        return externalLinkRef;
    }

    public LineString getGeometry() {
        return geometry;
    }

    public Long getMapMatchingInfrastructureLinkId() {
        return mapMatchingInfrastructureLinkId;
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

    public void setMapMatchingInfrastructureLinkId(final Long mapMatchingInfrastructureLinkId) {
        this.mapMatchingInfrastructureLinkId = mapMatchingInfrastructureLinkId;
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
