package fi.hsl.jore.importer.feature.system.repository;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public class SystemRepository implements ISystemRepository {

    private final DSLContext db;

    @Autowired
    public SystemRepository(final DSLContext db) {
        this.db = db;
    }

    @Override
    public Instant currentTimestamp() {
        return db.select(DSL.currentInstant())
                 .fetchOne()
                 .value1();
    }

    @Override
    public LocalDate currentDate() {
        return db.select(DSL.currentLocalDate())
                 .fetchOne()
                 .value1();
    }

    @Override
    public LocalTime currentTime() {
        // Prefer LOCALTIME over CURRENT_TIME,
        // See https://wiki.postgresql.org/wiki/Don%27t_Do_This#Don.27t_use_CURRENT_TIME
        return db.select(DSL.field("LOCALTIME", Time.class))
                 .fetchOne()
                 .value1()
                 .toLocalTime();
    }
}
