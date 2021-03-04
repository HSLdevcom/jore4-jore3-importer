package fi.hsl.jore.importer.feature.batch.link;

import fi.hsl.jore.importer.feature.batch.link.dto.LinkRow;
import fi.hsl.jore.importer.feature.jore.entity.JrLink;
import fi.hsl.jore.importer.feature.jore.entity.JrNode;
import fi.hsl.jore.importer.feature.jore.field.NodeType;
import fi.hsl.jore.importer.feature.jore.field.TransitType;
import fi.hsl.jore.importer.feature.jore.field.generated.NodeId;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getDoubleOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getIntOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalInt;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getStringOrThrow;

public class LinkRowMapper implements RowMapper<LinkRow> {

    public static final String SQL_PATH = "classpath:import/import_links.sql";

    @Override
    @Nullable
    public LinkRow mapRow(final ResultSet rs,
                          final int rowNum) throws SQLException {
        final JrNode from = JrNode.of(NodeId.of(getStringOrThrow(rs, "alku_soltunnus")),
                                      NodeType.of(getStringOrThrow(rs, "alku_soltyyppi")).orElse(NodeType.UNKNOWN),
                                      getDoubleOrThrow(rs, "alku_solomx"),
                                      getDoubleOrThrow(rs, "alku_solomy"),
                                      getDoubleOrThrow(rs, "alku_solstmx"),
                                      getDoubleOrThrow(rs, "alku_solstmy"));
        final JrNode to = JrNode.of(NodeId.of(getStringOrThrow(rs, "loppu_soltunnus")),
                                    NodeType.of(getStringOrThrow(rs, "loppu_soltyyppi")).orElse(NodeType.UNKNOWN),
                                    getDoubleOrThrow(rs, "loppu_solomx"),
                                    getDoubleOrThrow(rs, "loppu_solomy"),
                                    getDoubleOrThrow(rs, "loppu_solstmx"),
                                    getDoubleOrThrow(rs, "loppu_solstmy"));
        final JrLink link = JrLink.of(TransitType.of(getStringOrThrow(rs, "lnkverkko")).orElse(TransitType.UNKNOWN),
                                      from.nodeId(),
                                      to.nodeId(),
                                      getIntOrThrow(rs, "lnkpituus"),
                                      getOptionalInt(rs, "lnkmitpituus"));
        return LinkRow.of(link, from, to);
    }
}
