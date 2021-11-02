package fi.hsl.jore.importer.feature.transmodel.entity;

/**
 * Specifies the direction of the scheduled stop point
 * on an infrastructure link. Note that the values
 * of this enum are lowercase because the Jore4 destination
 * database uses lowercase values.
 */
public enum TransmodelScheduledStopPointDirection {
    backward,
    forward
}
