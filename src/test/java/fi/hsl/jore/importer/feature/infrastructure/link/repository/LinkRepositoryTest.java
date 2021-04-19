package fi.hsl.jore.importer.feature.infrastructure.link.repository;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.common.dto.mixin.IHasPK;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.util.GeometryUtil;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinkRepositoryTest extends IntegrationTest {

    private final ILinkRepository linkRepository;

    private static final LineString GEOM = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.168988620, 24.949328727, 0),
            new Coordinate(60.168896355, 24.945549266, 0)
    );

    private static final LineString GEOM2 = GeometryUtil.toLineString(
            GeometryUtil.SRID_WGS84,
            new Coordinate(60.158988620, 24.939328727, 0),
            new Coordinate(60.158896355, 24.935549266, 0)
    );

    public LinkRepositoryTest(@Autowired final ILinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Test
    public void insertSingleLink() {
        final PersistableLink link = PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, GEOM);

        assertThat(linkRepository.empty(),
                   is(true));

        final LinkPK linkId = linkRepository.insertLink(link);

        final Link linkFromDb = linkRepository.findById(linkId).orElseThrow();

        assertThat(linkFromDb.geometry(),
                   is(GEOM));
        assertThat(linkFromDb.geometry().getSRID(),
                   is(GEOM.getSRID()));
        assertThat(linkFromDb.geometry().getSRID(),
                   is(GeometryUtil.SRID_WGS84));
    }

    @Test
    public void insertMultipleLinks() {
        assertThat(linkRepository.empty(),
                   is(true));

        final List<LinkPK> keys = linkRepository.upsert(List.of(
                PersistableLink.of(ExternalId.of("a"), NetworkType.ROAD, GEOM),
                PersistableLink.of(ExternalId.of("b"), NetworkType.ROAD, GEOM2)
        ));

        final List<Link> linksFromDb = linkRepository.findAll();

        assertThat(linksFromDb.get(0).geometry(),
                   is(GEOM));
        assertThat(linksFromDb.get(0).geometry().getSRID(),
                   is(GeometryUtil.SRID_WGS84));
        assertThat(linksFromDb.get(0).geometry().getSRID(),
                   is(GEOM.getSRID()));
        assertThat(linksFromDb.map(IHasPK::pk).toSet(),
                   is(keys.toSet()));
    }
}
