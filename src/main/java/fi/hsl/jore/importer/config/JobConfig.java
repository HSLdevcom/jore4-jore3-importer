package fi.hsl.jore.importer.config;

import fi.hsl.jore.importer.feature.batch.link_context.LinkContext;
import fi.hsl.jore.importer.feature.batch.link_context.LinkContextProcessor;
import fi.hsl.jore.importer.feature.batch.link_context.LinkContextReader;
import fi.hsl.jore.importer.feature.batch.link_context.LinkWriter;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkRepository;
import fi.hsl.jore.importer.feature.infrastructure.network_type.cache.INetworkTypeCache;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {

    private final JobBuilderFactory jobs;

    private final StepBuilderFactory steps;

    @Autowired
    public JobConfig(final JobBuilderFactory jobs,
                     final StepBuilderFactory steps) {
        this.jobs = jobs;
        this.steps = steps;
    }

    @Bean
    public Job importJob(final Step importLinksAndNodesStep) {
        return jobs.get("importJoreJob")
                   .start(importLinksAndNodesStep)
                   .build();
    }

    @Bean
    public Step importLinksAndNodesStep(final LinkContextReader nodeReader,
                                        final INetworkTypeCache networkTypeCache,
                                        final ILinkRepository linkRepository) {
        return steps.get("importLinksAndNodesStep")
                .<LinkContext, PersistableLink>chunk(LinkWriter.BLOCK_SIZE)
                .reader(nodeReader.build())
                .processor(new LinkContextProcessor(networkTypeCache))
                .writer(new LinkWriter(linkRepository))
                .build();
    }
}
