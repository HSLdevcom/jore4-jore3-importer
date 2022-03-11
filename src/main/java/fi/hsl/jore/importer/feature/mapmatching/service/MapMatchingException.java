package fi.hsl.jore.importer.feature.mapmatching.service;

/**
 * This exception is thrown when map matching fails.
 */
public class MapMatchingException extends RuntimeException {

    public MapMatchingException(final String message) {
        super(message);
    }
}
