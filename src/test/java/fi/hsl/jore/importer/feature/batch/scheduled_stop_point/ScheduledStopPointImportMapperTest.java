package fi.hsl.jore.importer.feature.batch.scheduled_stop_point;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Types;
import java.util.Optional;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.RowSetProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ScheduledStopPointImportMapperTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"01", "07", "02", "99"})
    void shouldReadStopType(final String stopType) throws Exception {
        final String[] columns = {
            "soltunnus",
            "elynumero",
            "pysnimi",
            "pysnimir",
            "sollistunnus",
            "solkirjain",
            "paitunnus",
            "usage_in_routes",
            "pystyyppi"
        };
        final var metadata = new RowSetMetaDataImpl();
        metadata.setColumnCount(columns.length);
        for (int index = 0; index < columns.length; index++) {
            metadata.setColumnName(index + 1, columns[index]);
            metadata.setColumnType(index + 1, index == 7 ? Types.INTEGER : Types.VARCHAR);
        }
        try (final var resultSet = RowSetProvider.newFactory().createCachedRowSet()) {
            resultSet.setMetaData(metadata);
            resultSet.moveToInsertRow();
            for (int index = 1; index <= columns.length; index++) {
                resultSet.updateNull(index);
            }
            resultSet.updateString("soltunnus", "c");
            resultSet.updateInt("usage_in_routes", 0);
            resultSet.updateString("pystyyppi", stopType);
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
            resultSet.next();

            final var stop = new ScheduledStopPointImportMapper().mapRow(resultSet, 0);

            assertThat(stop.stopType()).isEqualTo(Optional.ofNullable(stopType));
        }
    }
}
