package fi.hsl.jore.importer.feature.mapmatching.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Contains the information of a route point.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoutePointRequestDTO {

    private RoutePointLocationRequestDTO location;
    private String nationalId;
    private String passengerId;
    private RoutePointLocationRequestDTO projectedLocation;
    private RoutePointType type;

    public RoutePointRequestDTO() {}

    public RoutePointLocationRequestDTO getLocation() {
        return location;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public RoutePointLocationRequestDTO getProjectedLocation() {
        return projectedLocation;
    }

    public RoutePointType getType() {
        return type;
    }

    public void setLocation(final RoutePointLocationRequestDTO location) {
        this.location = location;
    }

    public void setNationalId(final String nationalId) {
        this.nationalId = nationalId;
    }

    public void setPassengerId(final String passengerId) {
        this.passengerId = passengerId;
    }

    public void setProjectedLocation(final RoutePointLocationRequestDTO projectedLocation) {
        this.projectedLocation = projectedLocation;
    }

    public void setType(final RoutePointType type) {
        this.type = type;
    }
}
