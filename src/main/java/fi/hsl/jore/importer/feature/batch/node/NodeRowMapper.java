package fi.hsl.jore.importer.feature.batch.node;

import fi.hsl.jore.importer.feature.jore3.entity.JrNode;
import fi.hsl.jore.importer.feature.jore3.field.NodeType;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getDoubleOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getStringOrThrow;

public class NodeRowMapper implements RowMapper<JrNode> {

    public static final String SQL_PATH = "classpath:import/import_nodes.sql";

    @Override
    @Nullable
    public JrNode mapRow(final ResultSet rs,
                         final int rowNum) throws SQLException {
        return JrNode.of(NodeId.of(getStringOrThrow(rs, "soltunnus")),
                         NodeType.of(getStringOrThrow(rs, "soltyyppi")).orElse(NodeType.UNKNOWN),
                         getDoubleOrThrow(rs, "solomx"),
                         getDoubleOrThrow(rs, "solomy"),
                         getDoubleOrThrow(rs, "solstmx"),
                         getDoubleOrThrow(rs, "solstmy"));
    }
}
