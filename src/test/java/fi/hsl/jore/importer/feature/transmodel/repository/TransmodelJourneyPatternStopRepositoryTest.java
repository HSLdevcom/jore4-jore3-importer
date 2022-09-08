package fi.hsl.jore.importer.feature.transmodel.repository;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.common.converter.JsonbConverter;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelJourneyPatternStop;
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
class TransmodelJourneyPatternStopRepositoryTest {

    private final TransmodelJourneyPatternStopRepository repository;
    private final Table targetTable;

    private final JsonbConverter jsonbConverter;

    @Autowired
    TransmodelJourneyPatternStopRepositoryTest(@Qualifier("jore4DataSource") final DataSource targetDataSource,
                                           final TransmodelJourneyPatternStopRepository repository,
                                           final JsonbConverter jsonbConverter) {
        this.repository = repository;
        this.targetTable = new Table(targetDataSource, "journey_pattern.scheduled_stop_point_in_journey_pattern");
        this.jsonbConverter = jsonbConverter;
    }

    @Nested
    @DisplayName("Insert a journey pattern stop into the database")
    @Sql(
            scripts = {
                    "/sql/transmodel/drop_tables.sql",
                    "/sql/transmodel/populate_infrastructure_links.sql",
                    "/sql/transmodel/populate_lines.sql",
                    "/sql/transmodel/populate_scheduled_stop_points.sql",
                    "/sql/transmodel/populate_routes.sql",
                    "/sql/transmodel/populate_journey_patterns.sql"
            },
            config = @SqlConfig(dataSource = "jore4DataSource")
    )
    class InsertJourneyPatternStopIntoDatabase {

        private static final boolean IS_TIMING_POINT = true;
        private static final boolean IS_VIA_POINT = true;
        private final UUID JOURNEY_PATTERN_ID = UUID.fromString("ec564137-f30c-4689-9322-4ef650768af3");
        private final String SCHEDULED_STOP_POINT_LABEL  = "H1234";
        private static final int SCHEDULED_STOP_POINT_SEQUENCE = 1;
        private final Optional<MultilingualString> VIA_POINT_NAMES = Optional.of(MultilingualString.of(Map.of("fi-FI", "Helsinki", "sv-FI", "Helsingfors")));

        private final TransmodelJourneyPatternStop INPUT = TransmodelJourneyPatternStop.of(
                IS_TIMING_POINT,
                IS_VIA_POINT,
                VIA_POINT_NAMES,
                JOURNEY_PATTERN_ID,
                SCHEDULED_STOP_POINT_LABEL,
                SCHEDULED_STOP_POINT_SEQUENCE
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
        @DisplayName("Should save a new journey pattern stop with the correct scheduled stop point id")
        void shouldSaveNewJourneyPatternStopWithCorrectScheduledStopPointId() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.SCHEDULED_STOP_POINT_LABEL.getName())
                    .isEqualTo(SCHEDULED_STOP_POINT_LABEL);
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
        @DisplayName("Should save a new journey pattern with the correct timing point information")
        void shouldSaveNewJourneyPatternWithCorrectTimingPointInformation() {
            repository.insert(List.of(INPUT));

            assertThat(targetTable)
                    .row()
                    .value(SCHEDULED_STOP_POINT_IN_JOURNEY_PATTERN.IS_TIMING_POINT.getName())
                    .isEqualTo(IS_TIMING_POINT);
        }

        @Test
        @DisplayName("Should save a new journey pattern with the correct via point information")
        void shouldSaveNewJourneyPatternWithCorrectViaPointInformation() {
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