package fi.hsl.jore.importer.feature.mapmatching.service;

import org.springframework.web.util.UriComponentsBuilder;

/** A factory class which allows us to get the full url of the map matching API. */
class MapMatchingApiUrlFactory {

    /** Prevents instantiation. */
    private MapMatchingApiUrlFactory() {}

    /**
     * Returns the url of the map matching API.
     *
     * @param baseUrl The base url of the map matching API.
     */
    static String buildMapMatchingApiUrl(final String baseUrl) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/match/public-transport-route/v1/bus.json")
                .toUriString();
    }
}
