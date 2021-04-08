package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.BatchIntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkRepository;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.util.GeometryUtil;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

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

    private static final LineString GEOM = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(61.4463974, 23.8525307, 0),
            new Coordinate(61.4492857, 23.8561722, 0)
    );

    @Autowired
    private ILinkRepository linkRepository;

    @Test
    public void testWhenDbEmpty() {
        assertThat(linkRepository.empty(),
                   is(true));

        final JobExecution jobExecution = jobLauncherTestUtils.launchStep("importLinksStep");

        assertThat(jobExecution.getExitStatus().getExitCode(),
                   is("COMPLETED"));

        assertThat(linkRepository.count(),
                   is(1));
    }

    @Test
    public void testUpdatingExistingLink() {
        assertThat(linkRepository.empty(),
                   is(true));

        final ExternalId extId = ExternalId.of("c-d");
        final List<LinkPK> linkIds = linkRepository.upsert(List.of(
                PersistableLink.of(extId,
                                   NetworkType.ROAD,
                                   GEOM)
        ));
        final LinkPK targetId = linkIds.get(0);

        final JobExecution jobExecution = jobLauncherTestUtils.launchStep("importLinksStep");

        assertThat(jobExecution.getExitStatus().getExitCode(),
                   is("COMPLETED"));

        assertThat(linkRepository.count(),
                   is(1));

        final Link updated = linkRepository.findById(targetId).orElseThrow();

        assertThat(updated.externalId(),
                   is(extId));
        assertThat("Target link geometry is updated",
                   updated.geometry(),
                   is(GeometryUtil.toLineString(
                           GeometryUtil.SRID_WGS84,
                           new Coordinate(10, 10, 0),
                           new Coordinate(10, 10, 0)
                   )));
    }

}
