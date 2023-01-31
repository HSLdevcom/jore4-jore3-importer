package fi.hsl.jore.importer.feature.batch.line;

import fi.hsl.jore.importer.feature.jore3.entity.JrLine;
import fi.hsl.jore.importer.feature.jore3.enumerated.ClientOrganization;
import fi.hsl.jore.importer.feature.jore3.enumerated.PublicTransportDestination;
import fi.hsl.jore.importer.feature.jore3.enumerated.PublicTransportType;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.LineId;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getBooleanOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalString;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getStringOrThrow;

public class LineRowMapper implements RowMapper<JrLine> {

    public static final String SQL_PATH = "classpath:jore3-import/import_lines.sql";

    @Override
    @Nullable
    public JrLine mapRow(final ResultSet rs,
                         final int rowNum) throws SQLException {
        return JrLine.of(LineId.from(getStringOrThrow(rs, "lintunnus")),
                         getOptionalString(rs, "linverkko")
                                 .flatMap(TransitType::of)
                                 .orElse(TransitType.UNKNOWN),
                         getOptionalString(rs, "lintilorg")
                                 .flatMap(ClientOrganization::of)
                                 .orElse(ClientOrganization.UNKNOWN),
                         getOptionalString(rs, "linjoukkollaji")
                                 .flatMap(PublicTransportType::of)
                                 .orElse(PublicTransportType.UNKNOWN),
                         getOptionalString(rs, "linjlkohde")
                                 .flatMap(PublicTransportDestination::of)
                                 .orElse(PublicTransportDestination.UNKNOWN),
                         getBooleanOrThrow(rs, "linrunkolinja")
            );
    }
}
