package fi.hsl.jore.importer.feature.batch.link.support;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import fi.hsl.jore.importer.feature.batch.util.TransitTypeToNetworkTypeMapper;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.enumerated.TransitType;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class TransitTypeToNetworkTypeMapperTest {

    @Test
    public void testEachTransitTypeSupported() {
        assertThat(TransitTypeToNetworkTypeMapper.TO_NETWORK_TYPE.keySet(), is(Set.of(TransitType.values())));
    }

    @Test
    public void testEachNetworkTypeSupported() {
        // Note that at the moment there is a comprehensive 1:1 mapping from TransitType->NetworkType,
        // but in the future we might have additional NetworkTypes with no matching TransitType
        assertThat(TransitTypeToNetworkTypeMapper.TO_NETWORK_TYPE.values(), containsInAnyOrder(NetworkType.values()));
    }
}
