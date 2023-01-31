package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkTestRepository;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.util.GeometryUtil;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static fi.hsl.jore.importer.TestGeometryUtil.geometriesMatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = JobConfig.class)
@Sql(scripts = {
        "/sql/jore3/drop_tables.sql",
        "/sql/jore3/populate_nodes.sql",
        "/sql/jore3/populate_links.sql",
        "/sql/jore3/populate_points.sql"
},
     config = @SqlConfig(dataSource = "sourceDataSource"))
@Sql(scripts = "/sql/importer/drop_tables.sql")
public class ImportLinksStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("prepareNodesStep",
                                                      "importNodesStep",
                                                      "commitNodesStep",
                                                      "prepareLinksStep",
                                                      "importLinksStep",
                                                      "commitLinksStep");

    // Note that links always point to the actual location, not the projected location (when dealing with bus stops)
    private static final Coordinate NODE_C = new Coordinate(6, 5);
    private static final Coordinate NODE_D = new Coordinate(17, 16);
    private static final Coordinate NODE_E = new Coordinate(19, 18);
    private static final Coordinate NODE_F = new Coordinate(8, 7);

    // External ID of the link,
    // The line geometry
    // and the link network type
    private static final List<Tuple3<ExternalId, LineString, NetworkType>> LINKS = List.of(
            Tuple.of(ExternalId.of("1-c-d"),
                     GeometryUtil.toLineString(GeometryUtil.SRID_WGS84,
                                               List.of(NODE_C,
                                                       NODE_D)),
                     NetworkType.ROAD),
            Tuple.of(ExternalId.of("1-d-e"),
                     GeometryUtil.toLineString(GeometryUtil.SRID_WGS84,
                                               List.of(NODE_D,
                                                       NODE_E)),
                     NetworkType.ROAD),
            Tuple.of(ExternalId.of("1-e-f"),
                     GeometryUtil.toLineString(GeometryUtil.SRID_WGS84,
                                               List.of(NODE_E,
                                                       NODE_F)),
                     NetworkType.ROAD)
    );

    @Autowired
    private ILinkTestRepository linkRepository;

    @Test
    public void givenSampleDataInSourceDatabase_andAnEmptyDatabase_whenImportLinksStepsAreRun_thenLinksBetweenNodesCAndDAndNodesDAndEAndNodesEAndFAreImported() {
        assertThat(linkRepository.empty(),
                   is(true));

        runSteps(STEPS);

        assertThat(linkRepository.count(),
                   is(LINKS.size()));

        LINKS.forEach(expectedLinkParams -> {
            final ExternalId externalId = expectedLinkParams._1;
            final Link link = linkRepository.findByExternalId(externalId).orElseThrow();

            assertThat(String.format("link %s should have correct geometry", externalId),
                       geometriesMatch(link.geometry(),
                                       expectedLinkParams._2),
                       is(true));
            assertThat(String.format("link %s should have correct network type", externalId),
                       link.networkType(),
                       is(expectedLinkParams._3));
        });
    }
}
