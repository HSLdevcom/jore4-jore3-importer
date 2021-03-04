package fi.hsl.jore.importer.feature.infrastructure.link.repository;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Link;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.generated.LinkPK;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.generated.NetworkTypePK;
import fi.hsl.jore.importer.feature.infrastructure.network_type.repository.INetworkTypeRepository;
import fi.hsl.jore.importer.feature.util.GeometryUtil;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinkRepositoryTest extends IntegrationTest {

    private final INetworkTypeRepository networkTypeRepository;
    private final ILinkRepository linkRepository;

    private static final LineString GEOM = GeometryUtil.toLineString(
            new Coordinate(60.168988620, 24.949328727, 0),
            new Coordinate(60.168896355, 24.945549266, 0)
    );

    private static final LineString GEOM2 = GeometryUtil.toLineString(
            new Coordinate(60.158988620, 24.939328727, 0),
            new Coordinate(60.158896355, 24.935549266, 0)
    );

    @Autowired
    public LinkRepositoryTest(final INetworkTypeRepository networkTypeRepository,
                              final ILinkRepository linkRepository) {
        this.networkTypeRepository = networkTypeRepository;
        this.linkRepository = linkRepository;
    }

    @Test
    public void insertSingleLink() {
        final NetworkTypePK typeId = networkTypeRepository.findOrCreate(NetworkType.BUS);

        final PersistableLink link = PersistableLink.of(typeId, GEOM);
        final LinkPK linkId = linkRepository.insertLink(link);

        final Link linkFromDb = linkRepository.findById(linkId).orElseThrow();

        assertThat(GEOM,
                   is(linkFromDb.geometry()));
    }

    @Test
    public void insertMultipleLinks() {
        final NetworkTypePK typeId = networkTypeRepository.findOrCreate(NetworkType.BUS);

        linkRepository.insertLinks(List.of(
                PersistableLink.of(typeId, GEOM),
                PersistableLink.of(typeId, GEOM2)
        ));

        final List<Link> linksFromDb = linkRepository.findAll();

        assertThat(GEOM,
                   is(linksFromDb.get(0).geometry()));
    }
}
