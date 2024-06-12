package fi.hsl.jore.importer.feature.batch.line_header;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getLocalDateTimeOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalString;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getStringOrThrow;

import fi.hsl.jore.importer.feature.jore3.entity.JrLineHeader;
import fi.hsl.jore.importer.feature.jore3.field.LineId;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Nullable;
import org.springframework.jdbc.core.RowMapper;

public class LineHeaderMapper implements RowMapper<JrLineHeader> {

    public static final String SQL_PATH = "classpath:jore3-import/import_line_headers.sql";

    @Override
    @Nullable
    public JrLineHeader mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return JrLineHeader.of(
                LineId.from(getStringOrThrow(rs, "lintunnus")),
                getLocalDateTimeOrThrow(rs, "linalkupvm").toLocalDate(),
                getLocalDateTimeOrThrow(rs, "linloppupvm").toLocalDate(),
                getStringOrThrow(rs, "linnimi"),
                getOptionalString(rs, "linnimilyh"),
                getOptionalString(rs, "linnimir"),
                getOptionalString(rs, "linnimilyhr"),
                getOptionalString(rs, "linlahtop1"),
                getOptionalString(rs, "linlahtop1r"),
                getOptionalString(rs, "linlahtop2"),
                getOptionalString(rs, "linlahtop2r"));
    }
}
