package fi.hsl.jore.importer.feature.jore4;

public interface IDataEraser {

    void deleteJourneyPatterns();

    void deleteRoutesAndLines();

    void deleteScheduledStopPointsAndTimingPlaces();
}
