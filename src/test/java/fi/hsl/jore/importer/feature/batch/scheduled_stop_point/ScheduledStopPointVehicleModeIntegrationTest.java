package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import static org.assertj.core.api.Assertions.assertThat;

import fi.hsl.jore.importer.IntTest;
import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support.IScheduledStopPointImportRepository;
import fi.hsl.jore.importer.feature.batch.util.RowStatus;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore4.entity.VehicleMode;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImporterScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.Jore3ScheduledStopPoint;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@IntTest
@Sql(
        scripts = {
            "/sql/importer/drop_tables.sql",
            "/sql/importer/populate_infrastructure_nodes.sql",
            "/sql/importer/populate_places.sql"
        })
class ScheduledStopPointVehicleModeIntegrationTest {

    private final IScheduledStopPointImportRepository repository;
    private final ScheduledStopPointExportReader exportReader;
    private final JdbcTemplate jdbc;

    @Autowired
    ScheduledStopPointVehicleModeIntegrationTest(
            final IScheduledStopPointImportRepository repository,
            final ScheduledStopPointExportReader exportReader,
            @Qualifier("importerDataSource") final DataSource dataSource) {
        this.repository = repository;
        this.exportReader = exportReader;
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @ParameterizedTest
    @EnumSource(
            value = VehicleMode.class,
            names = {"BUS", "TRAM", "UNKNOWN"})
    void shouldPersistAndExportVehicleMode(final VehicleMode mode) throws Exception {
        repository.submitToStaging(List.of(stop(mode)));
        assertThat(repository.commitStagingToTarget()).containsKey(RowStatus.INSERTED);
        assertThat(readMode("scheduled_stop_points")).isEqualTo(mode.getValue());
        assertThat(readStop().vehicleMode()).isEqualTo(mode);
    }

    @ParameterizedTest
    @EnumSource(
            value = VehicleMode.class,
            names = {"TRAM", "UNKNOWN"})
    void shouldUpdateWhenOnlyVehicleModeChanges(final VehicleMode mode) throws Exception {
        repository.submitToStaging(List.of(stop(VehicleMode.BUS)));
        repository.commitStagingToTarget();
        repository.clearStagingTable();
        repository.submitToStaging(List.of(stop(mode)));

        assertThat(repository.commitStagingToTarget()).containsOnlyKeys(RowStatus.UPDATED);
        assertThat(readMode("scheduled_stop_points")).isEqualTo(mode.getValue());
        assertThat(readStop().vehicleMode()).isEqualTo(mode);
        assertThat(readMode("scheduled_stop_points_history")).isEqualTo("bus");
    }

    @Test
    void shouldUseRepresentativeRowsModeForDuplicateStops() throws Exception {
        repository.submitToStaging(List.of(
                stop(VehicleMode.TRAM),
                Jore3ScheduledStopPoint.of(
                        ExternalId.of("d"),
                        Optional.of(9876543211L),
                        MultilingualString.empty(),
                        Optional.of("H1234"),
                        Optional.empty(),
                        2,
                        VehicleMode.BUS)));
        repository.commitStagingToTarget();

        final ImporterScheduledStopPoint exported = readStop();
        assertThat(exported.externalIds()).containsExactly(ExternalId.of("d"), ExternalId.of("c"));
        assertThat(exported.vehicleMode()).isEqualTo(VehicleMode.TRAM);
    }

    private static Jore3ScheduledStopPoint stop(final VehicleMode mode) {
        return Jore3ScheduledStopPoint.of(
                ExternalId.of("c"),
                Optional.of(1234567890L),
                MultilingualString.empty(),
                Optional.of("H1234"),
                Optional.empty(),
                1,
                mode);
    }

    private String readMode(final String table) {
        return jdbc.queryForObject(
                "SELECT vehicle_mode FROM network." + table + " WHERE scheduled_stop_point_ext_id = 'c'", String.class);
    }

    private ImporterScheduledStopPoint readStop() throws Exception {
        final var reader = exportReader.build();
        try {
            reader.open(new ExecutionContext());
            return reader.read();
        } finally {
            reader.close();
        }
    }
}
