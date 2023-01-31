package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.LinkShape;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.repository.ILinkShapeTestRepository;
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
        "/sql/jore3/drop_tables.sql",
        "/sql/jore3/populate_nodes.sql",
        "/sql/jore3/populate_links.sql",
        "/sql/jore3/populate_points.sql"
},
     config = @SqlConfig(dataSource = "sourceDataSource"))
@Sql(scripts = "/sql/importer/drop_tables.sql")
public class ImportLinkPointsStepTest extends BatchIntegrationTest {

    private static final List<String> STEPS = List.of("prepareNodesStep",
                                                      "importNodesStep",
                                                      "commitNodesStep",
                                                      "prepareLinksStep",
                                                      "importLinksStep",
                                                      "commitLinksStep",
                                                      "prepareLinkPointsStep",
                                                      "importLinkPointsStep",
                                                      "commitLinkPointsStep");

    @Autowired
    private ILinkShapeTestRepository shapeRepository;

    @Test
    public void givenSampleDataInSourceDatabase_andAnEmptyDatabase_whenImportLinkPointsStepsAreRun_thenLinkShapeForLinkIsImported() {
        assertThat(shapeRepository.empty(),
                   is(true));

        runSteps(STEPS);

        assertThat(shapeRepository.count(),
                   is(1));

        final LinkShape shape = shapeRepository.findByExternalId(ExternalId.of("1-c-d")).orElseThrow();

        final Coordinate nodeC = new Coordinate(13, 12);
        final Coordinate poCD1 = new Coordinate(14.5, 14.1);
        final Coordinate poCD2 = new Coordinate(16.5, 15.1);
        final Coordinate nodeD = new Coordinate(17, 16);

        final LineString expectedPoints = GeometryUtil.toLineString(GeometryUtil.SRID_WGS84,
                                                                    List.of(nodeC,
                                                                            poCD1,
                                                                            poCD2,
                                                                            nodeD));

        assertThat(geometriesMatch(shape.geometry(), expectedPoints),
                   is(true));
    }
}
