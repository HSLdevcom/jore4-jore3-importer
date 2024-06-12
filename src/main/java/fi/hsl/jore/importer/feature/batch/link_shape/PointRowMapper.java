package fi.hsl.jore.importer.feature.batch.link_shape;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getDoubleOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getIntOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getStringOrThrow;

import fi.hsl.jore.importer.feature.batch.link_shape.dto.LinkEndpoints;
import fi.hsl.jore.importer.feature.batch.link_shape.dto.PointRow;
import fi.hsl.jore.importer.feature.jore3.entity.JrPoint;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Nullable;
import org.springframework.jdbc.core.RowMapper;

public class PointRowMapper implements RowMapper<PointRow> {

    public static final String SQL_PATH = "classpath:jore3-import/import_points.sql";

    @Override
    @Nullable
    public PointRow mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return PointRow.of(
                JrPoint.of(
                        TransitType.of(getStringOrThrow(rs, "lnkverkko"))
                                .orElse(TransitType.UNKNOWN),
                        NodeId.of(getStringOrThrow(rs, "lnkalkusolmu")),
                        NodeId.of(getStringOrThrow(rs, "lnkloppusolmu")),
                        getIntOrThrow(rs, "pisid"),
                        getIntOrThrow(rs, "pisjarjnro"),
                        getDoubleOrThrow(rs, "pismx"),
                        getDoubleOrThrow(rs, "pismy")),
                LinkEndpoints.of(
                        getDoubleOrThrow(rs, "alkusolmux"),
                        getDoubleOrThrow(rs, "alkusolmuy"),
                        getDoubleOrThrow(rs, "loppusolmux"),
                        getDoubleOrThrow(rs, "loppusolmuy")));
    }
}
