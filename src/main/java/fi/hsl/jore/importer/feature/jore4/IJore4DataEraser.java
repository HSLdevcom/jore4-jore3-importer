package fi.hsl.jore.importer.feature.jore4;

public interface IJore4DataEraser {

    void deleteJourneyPatterns();

    void deleteRoutesAndLines();

    void deleteScheduledStopPointsAndTimingPlaces();
}
