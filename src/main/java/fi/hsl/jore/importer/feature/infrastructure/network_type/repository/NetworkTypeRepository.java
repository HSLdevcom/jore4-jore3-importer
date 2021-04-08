package fi.hsl.jore.importer.feature.infrastructure.network_type.repository;

import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.jooq.infrastructure_network.tables.InfrastructureNetworkTypes;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NetworkTypeRepository
        implements INetworkTypeRepository {

    private static final InfrastructureNetworkTypes TYPES = InfrastructureNetworkTypes.INFRASTRUCTURE_NETWORK_TYPES;

    private final DSLContext db;

    @Autowired
    public NetworkTypeRepository(final DSLContext db) {
        this.db = db;
    }

    @Transactional
    public void createIfMissing(final NetworkType type) {
        db.insertInto(TYPES)
          .columns(TYPES.INFRASTRUCTURE_NETWORK_TYPE)
          .values(type.label())
          .onConflictDoNothing()
          .execute();
    }
}
