package fi.hsl.jore.importer.feature.batch.stop_place;

import fi.hsl.jore.importer.feature.batch.util.TransitTypeToNetworkTypeMapper;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.entity.JrStopPlace;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.stops.stop_place.dto.Jore3StopPlace;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class StopPlaceImportProcessor implements ItemProcessor<JrStopPlace, Jore3StopPlace> {

    @Override
    public Jore3StopPlace process(final JrStopPlace input) throws Exception {
        final NetworkType networkType = TransitTypeToNetworkTypeMapper.resolveNetworkType(input.transitType());
        return Jore3StopPlace.of(
                ExternalId.of(input.pk().stopPlaceId().value()),
                MultilingualString.empty()
                        .with(JoreLocaleUtil.FINNISH, input.nameFinnish())
                        .with(JoreLocaleUtil.SWEDISH, input.nameSwedish()),
                MultilingualString.empty()
                        .with(JoreLocaleUtil.FINNISH, input.nameLongFinnish())
                        .with(JoreLocaleUtil.SWEDISH, input.nameLongSwedish()),
                MultilingualString.empty()
                        .with(JoreLocaleUtil.FINNISH, input.locationFinnish())
                        .with(JoreLocaleUtil.SWEDISH, input.locationSwedish()),
                networkType);
    }
}
