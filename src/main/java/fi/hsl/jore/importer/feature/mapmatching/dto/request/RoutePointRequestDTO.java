package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.geojson.Point;

/**
 * Contains the information of a route point.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoutePointRequestDTO {

    private Point location;
    private String nationalId;
    private String passengerId;
    private Point projectedLocation;
    private RoutePointType type;

    public RoutePointRequestDTO() {}

    public Point getLocation() {
        return location;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public Point getProjectedLocation() {
        return projectedLocation;
    }

    public RoutePointType getType() {
        return type;
    }

    public void setLocation(final Point location) {
        this.location = location;
    }

    public void setNationalId(final String nationalId) {
        this.nationalId = nationalId;
    }

    public void setPassengerId(final String passengerId) {
        this.passengerId = passengerId;
    }

    public void setProjectedLocation(final Point projectedLocation) {
        this.projectedLocation = projectedLocation;
    }

    public void setType(final RoutePointType type) {
        this.type = type;
    }
}
