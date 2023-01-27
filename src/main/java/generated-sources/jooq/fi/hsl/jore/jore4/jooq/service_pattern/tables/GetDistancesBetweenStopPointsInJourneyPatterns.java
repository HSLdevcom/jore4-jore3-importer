/*
 * This file is generated by jOOQ.
 */
package fi.hsl.jore.jore4.jooq.service_pattern.tables;


import fi.hsl.jore.jore4.jooq.service_pattern.ServicePattern;

import java.time.LocalDate;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GetDistancesBetweenStopPointsInJourneyPatterns extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns</code>
     */
    public static final GetDistancesBetweenStopPointsInJourneyPatterns GET_DISTANCES_BETWEEN_STOP_POINTS_IN_JOURNEY_PATTERNS = new GetDistancesBetweenStopPointsInJourneyPatterns();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns.journey_pattern_id</code>.
     */
    public final TableField<Record, UUID> JOURNEY_PATTERN_ID = createField(DSL.name("journey_pattern_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns.route_id</code>.
     */
    public final TableField<Record, UUID> ROUTE_ID = createField(DSL.name("route_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns.route_priority</code>.
     */
    public final TableField<Record, Integer> ROUTE_PRIORITY = createField(DSL.name("route_priority"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns.observation_date</code>.
     */
    public final TableField<Record, LocalDate> OBSERVATION_DATE = createField(DSL.name("observation_date"), SQLDataType.LOCALDATE.nullable(false), this, "");

    /**
     * The column
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns.stop_interval_sequence</code>.
     */
    public final TableField<Record, Integer> STOP_INTERVAL_SEQUENCE = createField(DSL.name("stop_interval_sequence"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns.start_stop_label</code>.
     */
    public final TableField<Record, String> START_STOP_LABEL = createField(DSL.name("start_stop_label"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns.end_stop_label</code>.
     */
    public final TableField<Record, String> END_STOP_LABEL = createField(DSL.name("end_stop_label"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns.distance_in_metres</code>.
     */
    public final TableField<Record, Double> DISTANCE_IN_METRES = createField(DSL.name("distance_in_metres"), SQLDataType.DOUBLE.nullable(false), this, "");

    private GetDistancesBetweenStopPointsInJourneyPatterns(Name alias, Table<Record> aliased) {
        this(alias, aliased, new Field[] {
            DSL.val(null, SQLDataType.UUID.getArrayDataType()),
            DSL.val(null, SQLDataType.LOCALDATE),
            DSL.val(null, SQLDataType.BOOLEAN)
        });
    }

    private GetDistancesBetweenStopPointsInJourneyPatterns(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.function());
    }

    /**
     * Create an aliased
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns</code>
     * table reference
     */
    public GetDistancesBetweenStopPointsInJourneyPatterns(String alias) {
        this(DSL.name(alias), GET_DISTANCES_BETWEEN_STOP_POINTS_IN_JOURNEY_PATTERNS);
    }

    /**
     * Create an aliased
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns</code>
     * table reference
     */
    public GetDistancesBetweenStopPointsInJourneyPatterns(Name alias) {
        this(alias, GET_DISTANCES_BETWEEN_STOP_POINTS_IN_JOURNEY_PATTERNS);
    }

    /**
     * Create a
     * <code>service_pattern.get_distances_between_stop_points_in_journey_patterns</code>
     * table reference
     */
    public GetDistancesBetweenStopPointsInJourneyPatterns() {
        this(DSL.name("get_distances_between_stop_points_in_journey_patterns"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : ServicePattern.SERVICE_PATTERN;
    }

    @Override
    public GetDistancesBetweenStopPointsInJourneyPatterns as(String alias) {
        return new GetDistancesBetweenStopPointsInJourneyPatterns(DSL.name(alias), this, parameters);
    }

    @Override
    public GetDistancesBetweenStopPointsInJourneyPatterns as(Name alias) {
        return new GetDistancesBetweenStopPointsInJourneyPatterns(alias, this, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public GetDistancesBetweenStopPointsInJourneyPatterns rename(String name) {
        return new GetDistancesBetweenStopPointsInJourneyPatterns(DSL.name(name), null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public GetDistancesBetweenStopPointsInJourneyPatterns rename(Name name) {
        return new GetDistancesBetweenStopPointsInJourneyPatterns(name, null, parameters);
    }

    /**
     * Call this table-valued function
     */
    public GetDistancesBetweenStopPointsInJourneyPatterns call(
          UUID[] journeyPatternIds
        , LocalDate observationDate
        , Boolean includeDraftStops
    ) {
        GetDistancesBetweenStopPointsInJourneyPatterns result = new GetDistancesBetweenStopPointsInJourneyPatterns(DSL.name("get_distances_between_stop_points_in_journey_patterns"), null, new Field[] {
            DSL.val(journeyPatternIds, SQLDataType.UUID.getArrayDataType()),
            DSL.val(observationDate, SQLDataType.LOCALDATE),
            DSL.val(includeDraftStops, SQLDataType.BOOLEAN)
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }

    /**
     * Call this table-valued function
     */
    public GetDistancesBetweenStopPointsInJourneyPatterns call(
          Field<UUID[]> journeyPatternIds
        , Field<LocalDate> observationDate
        , Field<Boolean> includeDraftStops
    ) {
        GetDistancesBetweenStopPointsInJourneyPatterns result = new GetDistancesBetweenStopPointsInJourneyPatterns(DSL.name("get_distances_between_stop_points_in_journey_patterns"), null, new Field[] {
            journeyPatternIds,
            observationDate,
            includeDraftStops
        });

        return aliased() ? result.as(getUnqualifiedName()) : result;
    }
}