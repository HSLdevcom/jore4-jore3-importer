package fi.hsl.jore.importer.feature.network.place.repository;

import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.network.place.dto.PersistablePlace;
import fi.hsl.jore.importer.feature.network.place.dto.Place;
import fi.hsl.jore.importer.feature.network.place.dto.generated.PlacePK;
import fi.hsl.jore.importer.jooq.network.tables.NetworkPlaces;
import fi.hsl.jore.importer.jooq.network.tables.NetworkPlacesWithHistory;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkPlacesRecord;
import fi.hsl.jore.importer.jooq.network.tables.records.NetworkPlacesWithHistoryRecord;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PlaceRepository implements IPlaceTestRepository {

    private static final NetworkPlaces PLACE = NetworkPlaces.NETWORK_PLACES;
    private static final NetworkPlacesWithHistory HISTORY_VIEW = NetworkPlacesWithHistory.NETWORK_PLACES_WITH_HISTORY;
    private static final TableField<NetworkPlacesRecord, UUID> PRIMARY_KEY = PLACE.NETWORK_PLACE_ID;

    private final DSLContext db;

    @Autowired
    public PlaceRepository(@Qualifier("importerDsl") final DSLContext db) {
        this.db = db;
    }

    @Override
    @Transactional
    public PlacePK insert(final PersistablePlace place) {
        final NetworkPlacesRecord r = db.newRecord(PLACE);

        r.setNetworkPlaceExtId(place.externalId().value());
        r.setNetworkPlaceName(place.name());

        r.store();

        return PlacePK.of(r.getNetworkPlaceId());
    }

    @Override
    @Transactional
    public List<PlacePK> insert(final List<PersistablePlace> entities) {
        return entities.stream().map(this::insert).toList();
    }

    @Override
    @Transactional
    public List<PlacePK> insert(final PersistablePlace... entities) {
        return insert(List.of(entities));
    }

    @Override
    @Transactional
    public PlacePK update(final Place place) {
        final NetworkPlacesRecord r = Optional.ofNullable(db.selectFrom(PLACE)
                        .where(PRIMARY_KEY.eq(place.pk().value()))
                        .fetchAny())
                .orElseThrow();

        r.setNetworkPlaceName(place.name());

        r.store();

        return PlacePK.of(r.getNetworkPlaceId());
    }

    @Override
    @Transactional
    public List<PlacePK> update(final List<Place> entities) {
        return entities.stream().map(this::update).toList();
    }

    @Override
    @Transactional
    public List<PlacePK> update(final Place... entities) {
        return update(List.of(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Place> findById(final PlacePK id) {
        return db.selectFrom(PLACE)
                .where(PRIMARY_KEY.eq(id.value()))
                .fetchStream()
                .map(PlaceRepository::from)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Place> findByExternalId(final ExternalId externalId) {
        return db.selectFrom(PLACE)
                .where(PLACE.NETWORK_PLACE_EXT_ID.eq(externalId.value()))
                .fetchStream()
                .map(PlaceRepository::from)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Place> findAll() {
        return db.selectFrom(PLACE).fetchStream().map(PlaceRepository::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<PlacePK> findAllIds() {
        return db.select(PRIMARY_KEY)
                .from(PLACE)
                .fetchStream()
                .map(row -> PlacePK.of(row.value1()))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        //noinspection ConstantConditions
        return db.selectCount().from(PLACE).fetchOne(0, int.class);
    }

    @Override
    @Transactional(readOnly = true)
    public int countHistory() {
        //noinspection ConstantConditions
        return db.selectCount().from(HISTORY_VIEW).fetchOne(0, int.class);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean empty() {
        return count() == 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emptyHistory() {
        return countHistory() == 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Place> findFromHistory() {
        return db.selectFrom(HISTORY_VIEW)
                .orderBy(HISTORY_VIEW.NETWORK_PLACE_SYS_PERIOD.asc())
                .fetchStream()
                .map(PlaceRepository::from)
                .toList();
    }

    private static Place from(final NetworkPlacesRecord record) {
        return Place.of(
                PlacePK.of(record.getNetworkPlaceId()),
                ExternalId.of(record.getNetworkPlaceExtId()),
                record.getNetworkPlaceName(),
                record.getNetworkPlaceSysPeriod());
    }

    private static Place from(final NetworkPlacesWithHistoryRecord record) {
        return Place.of(
                PlacePK.of(record.getNetworkPlaceId()),
                ExternalId.of(record.getNetworkPlaceExtId()),
                record.getNetworkPlaceName(),
                record.getNetworkPlaceSysPeriod());
    }
}
