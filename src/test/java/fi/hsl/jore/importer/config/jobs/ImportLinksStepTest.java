package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkTestRepository;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.util.GeometryUtil;
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
        "/sql/source/drop_tables.sql",
        "/sql/source/populate_nodes.sql",
        "/sql/source/populate_links.sql",
        "/sql/source/populate_points.sql"
},
     config = @SqlConfig(dataSource = "sourceDataSource"))
@Sql(scripts = "/sql/destination/drop_tables.sql")
public class ImportLinksStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("prepareNodesStep",
                                                      "importNodesStep",
                                                      "commitNodesStep",
                                                      "prepareLinksStep",
                                                      "importLinksStep",
                                                      "commitLinksStep");

    @Autowired
    private ILinkTestRepository linkRepository;

    @Test
    public void givenSampleDataInSourceDatabase_andAnEmptyDatabase_whenImportLinksStepsAreRun_thenLinkBetweenNodeCAndNodeDIsImported() {
        assertThat(linkRepository.empty(),
                   is(true));

        runSteps(STEPS);

        assertThat(linkRepository.count(),
                   is(1));

        final Link link = linkRepository.findByExternalId(ExternalId.of("1-c-d")).orElseThrow();

        final Coordinate nodeC = new Coordinate(13, 12);
        final Coordinate nodeD = new Coordinate(17, 16);
        final LineString expectedGeometry = GeometryUtil.toLineString(GeometryUtil.SRID_WGS84,
                                                                      List.of(nodeC,
                                                                              nodeD));
        assertThat(geometriesMatch(link.geometry(),
                                   expectedGeometry),
                   is(true));
        assertThat(link.networkType(),
                   is(NetworkType.ROAD));
    }
}
