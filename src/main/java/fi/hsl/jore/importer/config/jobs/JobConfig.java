package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.feature.batch.link.LinkRowProcessor;
import fi.hsl.jore.importer.feature.batch.link.LinkRowReader;
import fi.hsl.jore.importer.feature.batch.link.LinkWriter;
import fi.hsl.jore.importer.feature.batch.link.dto.LinkRow;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkRepository;
import fi.hsl.jore.importer.feature.infrastructure.network_type.cache.INetworkTypeCache;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class JobConfig extends BatchConfig {

    @Bean
    public Job importJob(final Step importLinksAndNodesStep) {
        return jobs.get("importJoreJob")
                   .start(importLinksAndNodesStep)
                   .build();
    }

    @Bean
    public Step importLinksAndNodesStep(final LinkRowReader nodeReader,
                                        final INetworkTypeCache networkTypeCache,
                                        final ILinkRepository linkRepository) {
        return steps.get("importLinksAndNodesStep")
                .<LinkRow, PersistableLink>chunk(LinkWriter.BLOCK_SIZE)
                .reader(nodeReader.build())
                .processor(new LinkRowProcessor(networkTypeCache))
                .writer(new LinkWriter(linkRepository))
                .build();
    }
}
