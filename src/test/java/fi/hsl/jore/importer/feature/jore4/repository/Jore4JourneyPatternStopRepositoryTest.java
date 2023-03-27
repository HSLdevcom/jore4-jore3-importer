package fi.hsl.jore.importer.feature.jore4.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.common.converter.JsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPatternStop;
import org.assertj.core.api.Condition;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static fi.hsl.jore.jore4.jooq.journey_pattern.Tables.SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN;
import static org.assertj.db.api.Assertions.assertThat;

@IntTest
class Jore4JourneyPatternStopRepositoryTest {

    private final Jore4JourneyPatternStopRepository repository;
    private final Table targetTable;

    private final JsonbConverter jsonbConverter;

    @Autowired
    Jore4JourneyPatternStopRepositoryTest(@Qualifier("jore4DataSource") final DataSource targetDataSource,
                                          final Jore4JourneyPatternStopRepository repository,
                                          final JsonbConverter jsonbConverter) {
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "journey_pattern.scheduled_stop_point_in_journey_pattern");
        this.jsonbConverter = jsonbConverter;
    }

    @Nested
    @DisplayName("Insert a journey pattern stop into the database")
    @Sql(
            scripts = {
                    "/sql/jore4/drop_tables.sql",
                    "/sql/jore4/populate_infrastructure_links.sql",
                    "/sql/jore4/populate_lines.sql",
                    "/sql/jore4/populate_scheduled_stop_points.sql",
                    "/sql/jore4/populate_routes.sql",
                    "/sql/jore4/populate_journey_patterns.sql",
                    "/sql/jore4/populate_timing_places.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource")
    )
    class InsertJourneyPatternStopIntoDatabase {

        private final UUID JOURNEY_PATTERN_ID = UUID.fromString("ec564137-f30c-4689-9322-4ef650768af3");
        private static final int SCHEDULED_STOP_POINT_SEQUENCE = 1;
        private final String SCHEDULED_STOP_POINT_LABEL  = "H1234";
        private static final boolean IS_USED_AS_TIMING_POINT = true;
        private static final boolean IS_REGULATED_TIMING_POINT = true;
        private static final boolean IS_LOADING_TIME_ALLOWED = true;
        private static final boolean IS_VIA_POINT = true;
        private final Optional<MultilingualString> VIA_POINT_NAMES = Optional.of(MultilingualString.of(Map.of(
                "fi-FI", "Helsinki",
                "sv-FI", "Helsingfors"
        )));

        private final Jore4JourneyPatternStop INPUT = Jore4JourneyPatternStop.of(
                JOURNEY_PATTERN_ID,
                SCHEDULED_STOP_POINT_SEQUENCE,
                SCHEDULED_STOP_POINT_LABEL,
                IS_USED_AS_TIMING_POINT,
                IS_REGULATED_TIMING_POINT,
                IS_LOADING_TIME_ALLOWED,
                IS_VIA_POINT,
                VIA_POINT_NAMES
        );

        @Test
        @DisplayName("Should insert a new journey pattern stop into the database")
        void shouldInsertNewJourneyPatternStopIntoDatabase() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable).hasNumberOfRows(1);
        }

        @Test
        @DisplayName("Should save a new journey pattern stop with the correct journey pattern id")
        void shouldSaveNewJourneyPatternStopWithCorrectJourneyPatternId() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.JOURNEY_PATTERN_ID.getName())
                    .isEqualTo(JOURNEY_PATTERN_ID);
        }

        @Test
        @DisplayName("Should save a new journey pattern with the correct scheduled stop point sequence")
        void shouldSaveNewJourneyPatternWithCorrectScheduledStopPointSequence() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_SEQUENCE.getName())
                    .isEqualTo(SCHEDULED_STOP_POINT_SEQUENCE);
        }

        @Test
        @DisplayName("Should save a new journey pattern stop with the correct scheduled stop point id")
        void shouldSaveNewJourneyPatternStopWithCorrectScheduledStopPointId() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_LABEL.getName())
                    .isEqualTo(SCHEDULED_STOP_POINT_LABEL);
        }

        @Test
        @DisplayName("Should save a new journey pattern with the correct is-used-as-timing-point information")
        void shouldSaveNewJourneyPatternWithCorrectIsUsedAsTimingPointInformation() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_USED_AS_TIMING_POINT.getName())
                    .isEqualTo(IS_USED_AS_TIMING_POINT);
        }

        @Test
        @DisplayName("Should save a new journey pattern with the correct is-regulated-timing-point information")
        void shouldSaveNewJourneyPatternWithCorrectIsRegulatedTimingPointInformation() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_REGULATED_TIMING_POINT.getName())
                    .isEqualTo(IS_REGULATED_TIMING_POINT);
        }

        @Test
        @DisplayName("Should save a new journey pattern with the correct is-loading-time-allowed information")
        void shouldSaveNewJourneyPatternWithCorrectIsLoadingTimeAllowedInformation() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_LOADING_TIME_ALLOWED.getName())
                    .isEqualTo(IS_LOADING_TIME_ALLOWED);
        }

        @Test
        @DisplayName("Should save a new journey pattern with the correct is-via-point information")
        void shouldSaveNewJourneyPatternWithCorrectIsViaPointInformation() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_VIA_POINT.getName())
                    .isEqualTo(IS_VIA_POINT);
        }

        @Test
        @DisplayName("Should save a new journey pattern with the correct via point names")
        void shouldSaveNewJourneyPatternWithCorrectViaPointNames() {
            repository.insert(List.of(INPUT));

            final String expectedJsonString = jsonbConverter.asJson(VIA_POINT_NAMES.get()).data();

            var testJsonValueCondition = new Condition<PGobject>(fieldValue ->
                    fieldValue.getValue().replace(" ", "").equals(expectedJsonString),
                    expectedJsonString);

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.VIA_POINT_NAME_I18N.getName())
                    .is(testJsonValueCondition);

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.VIA_POINT_SHORT_NAME_I18N.getName())
                    .is(testJsonValueCondition);
        }
    }
}
