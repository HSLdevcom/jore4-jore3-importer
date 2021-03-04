package fi.hsl.jore.importer.feature.batch.link_context;

import fi.hsl.jore.importer.feature.jore.entity.JrLink;
import fi.hsl.jore.importer.feature.jore.entity.JrNode;
import fi.hsl.jore.importer.feature.jore.field.NodeType;
import fi.hsl.jore.importer.feature.jore.field.TransitType;
import fi.hsl.jore.importer.feature.jore.field.generated.NodeId;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class LinkContextRowMapper implements RowMapper<LinkContext> {

    public static final String SQL = "SELECT" +
                                     "    l.lnkverkko," +
                                     "    l.lnkmitpituus," +
                                     "    l.lnkpituus," +
                                     "    sa.soltunnus AS alku_soltunnus," +
                                     "    sa.soltyyppi AS alku_soltyyppi," +
                                     "    sa.solomx AS alku_solomx," +
                                     "    sa.solomy AS alku_solomy," +
                                     "    sa.solstmx AS alku_solstmx," +
                                     "    sa.solstmy AS alku_solstmy," +
                                     "    sb.soltunnus AS loppu_soltunnus," +
                                     "    sb.soltyyppi AS loppu_soltyyppi," +
                                     "    sb.solomx AS loppu_solomx," +
                                     "    sb.solomy AS loppu_solomy," +
                                     "    sb.solstmx AS loppu_solstmx," +
                                     "    sb.solstmy AS loppu_solstmy" +
                                     " FROM" +
                                     "    jr_linkki l" +
                                     "        LEFT JOIN jr_solmu sa ON sa.soltunnus = l.lnkalkusolmu" +
                                     "        LEFT JOIN jr_solmu sb ON sb.soltunnus = l.lnkloppusolmu";

    @Override
    @Nullable
    public LinkContext mapRow(final ResultSet rs,
                              final int rowNum) throws SQLException {
        final JrNode from = JrNode.of(NodeId.of(rs.getString("alku_soltunnus")),
                                      NodeType.of(rs.getString("alku_soltyyppi"))
                                              .orElse(NodeType.UNKNOWN),
                                      rs.getDouble("alku_solomx"),
                                      rs.getDouble("alku_solomy"),
                                      rs.getDouble("alku_solstmx"),
                                      rs.getDouble("alku_solstmy"));
        final JrNode to = JrNode.of(NodeId.of(rs.getString("loppu_soltunnus")),
                                    NodeType.of(rs.getString("loppu_soltyyppi"))
                                            .orElse(NodeType.UNKNOWN),
                                    rs.getDouble("loppu_solomx"),
                                    rs.getDouble("loppu_solomy"),
                                    rs.getDouble("loppu_solstmx"),
                                    rs.getDouble("loppu_solstmy"));
        final JrLink link = JrLink.of(TransitType.of(rs.getString("lnkverkko")).orElse(TransitType.UNKNOWN),
                                      from.nodeId(),
                                      to.nodeId(),
                                      rs.getInt("lnkpituus"),
                                      Optional.ofNullable(rs.getObject("lnkmitpituus", Integer.class)));
        return LinkContext.of(link, from, to);
    }
}
