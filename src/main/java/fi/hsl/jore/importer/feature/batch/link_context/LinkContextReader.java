package fi.hsl.jore.importer.feature.batch.link_context;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class LinkContextReader {

    private static final String NAME = "linkContextReader";

    private final DataSource sourceDataSource;

    @Autowired
    public LinkContextReader(@Qualifier("sourceDataSource") final DataSource sourceDataSource) {
        this.sourceDataSource = sourceDataSource;
    }

    public JdbcCursorItemReader<LinkContext> build() {
        // The default fetch size seems to be 128 items
        return new JdbcCursorItemReaderBuilder<LinkContext>()
                .dataSource(sourceDataSource)
                .name(NAME)
                .sql(LinkContextRowMapper.SQL)
                .rowMapper(new LinkContextRowMapper())
                .build();
    }
}
