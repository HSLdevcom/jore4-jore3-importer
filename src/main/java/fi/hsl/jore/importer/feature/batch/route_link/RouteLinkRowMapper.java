package fi.hsl.jore.importer.feature.batch.route_link;

import fi.hsl.jore.importer.feature.batch.route_link.dto.LastLinkAttributes;
import fi.hsl.jore.importer.feature.batch.route_link.dto.SingleRouteLinkAndParent;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteLink;
import fi.hsl.jore.importer.feature.jore3.enumerated.Direction;
import fi.hsl.jore.importer.feature.jore3.enumerated.NodeType;
import fi.hsl.jore.importer.feature.jore3.enumerated.RegulatedTimingPointStatus;
import fi.hsl.jore.importer.feature.jore3.enumerated.StopPointPurpose;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.RouteId;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.field.generated.RouteLinkId;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getBooleanOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getIntOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getLocalDateTimeOrThrow;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalInt;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getOptionalString;
import static fi.hsl.jore.importer.feature.batch.util.JdbcUtil.getStringOrThrow;

public class RouteLinkRowMapper implements RowMapper<SingleRouteLinkAndParent> {

    public static final String SQL_PATH = "classpath:jore3-import/import_route_links.sql";

    @Override
    @Nullable
    public SingleRouteLinkAndParent mapRow(final ResultSet rs,
                                           final int rowNum) throws SQLException {
        return SingleRouteLinkAndParent.of(
                JrRouteLink.of(
                        RouteLinkId.of(getIntOrThrow(rs, "relid")),
                        getIntOrThrow(rs, "reljarjnro"),
                        RouteId.from(getStringOrThrow(rs, "reitunnus")),
                        Direction.of(getIntOrThrow(rs, "suusuunta"))
                                 .orElse(Direction.UNKNOWN),
                        getLocalDateTimeOrThrow(rs, "suuvoimast").toLocalDate(),
                        TransitType.of(getStringOrThrow(rs, "lnkverkko"))
                                   .orElse(TransitType.UNKNOWN),
                        NodeId.of(getStringOrThrow(rs, "lnkalkusolmu")),
                        NodeId.of(getStringOrThrow(rs, "lnkloppusolmu")),
                        NodeType.of(getStringOrThrow(rs, "relpysakki"))
                                .orElse(NodeType.UNKNOWN),
                        getOptionalString(rs, "ajantaspys")
                                .flatMap(RegulatedTimingPointStatus::of)
                                .orElse(RegulatedTimingPointStatus.UNKNOWN),
                        getOptionalString(rs, "relohaikpys")
                                .flatMap(StopPointPurpose::of)
                                .orElse(StopPointPurpose.UNKNOWN),
                        getBooleanOrThrow(rs, "rl_paikka"),
                        getBooleanOrThrow(rs, "rl_kirjaan"),
                        getBooleanOrThrow(rs, "rl_via"),
                        getOptionalString(rs, "rl_maaranpaa2"),
                        getOptionalString(rs, "rl_maaranpaa2r"),
                        getOptionalInt(rs, "kirjasarake")
                ),

                LastLinkAttributes.of(
                        // Note that this field is different for each row, as each row refers to a different "loppusolmu"!
                        NodeType.of(getStringOrThrow(rs, "loppusolmu_tyyppi"))
                                .orElse(NodeType.UNKNOWN),
                        // Note that these fields are duplicated for each route link as they refer to the same parent route direction!
                        getBooleanOrThrow(rs, "rs_kirjaan"),
                        getOptionalInt(rs, "rs_kirjasarake")
                )
        );
    }
}
