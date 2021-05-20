package fi.hsl.jore.importer.feature.batch.link.support;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import io.vavr.collection.HashSet;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class TransitTypeToNetworkTypeMapperTest {

    @Test
    public void testEachTransitTypeSupported() {
        assertThat(TransitTypeToNetworkTypeMapper.TO_NETWORK_TYPE.keySet(),
                   is(HashSet.of(TransitType.values())));
    }

    @Test
    public void testEachNetworkTypeSupported() {
        // Note that at the moment there is a comprehensive 1:1 mapping from TransitType->NetworkType,
        // but in the future we might have additional NetworkTypes with no matching TransitType
        assertThat(TransitTypeToNetworkTypeMapper.TO_NETWORK_TYPE.values().toSet(),
                   is(HashSet.of(NetworkType.values())));
    }
}
