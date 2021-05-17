package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.feature.batch.common.GenericCleanupTasklet;
import fi.hsl.jore.importer.feature.batch.common.GenericCommitTasklet;
import fi.hsl.jore.importer.feature.batch.common.GenericImportWriter;
import fi.hsl.jore.importer.feature.batch.link.LinkRowProcessor;
import fi.hsl.jore.importer.feature.batch.link.LinkRowReader;
import fi.hsl.jore.importer.feature.batch.link.dto.LinkRow;
import fi.hsl.jore.importer.feature.batch.link.support.ILinkImportRepository;
import fi.hsl.jore.importer.feature.batch.node.NodeProcessor;
import fi.hsl.jore.importer.feature.batch.node.NodeReader;
import fi.hsl.jore.importer.feature.batch.node.support.INodeImportRepository;
import fi.hsl.jore.importer.feature.batch.point.LinkPointProcessor;
import fi.hsl.jore.importer.feature.batch.point.LinkPointReader;
import fi.hsl.jore.importer.feature.batch.point.PointReader;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkGeometry;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkPoints;
import fi.hsl.jore.importer.feature.batch.point.support.ILinkPointImportRepository;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.ImportableLink;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.ImportableNode;
import fi.hsl.jore.importer.feature.jore3.entity.JrNode;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration
public class JobConfig extends BatchConfig {

    public static final String JOB_NAME = "importJoreJob";

    @Bean
    public Job importJob(final Flow importNodesFlow,
                         final Flow importLinksFlow,
                         final Flow importLinkPointsFlow) {
        return jobs.get(JOB_NAME)
                   .start(importNodesFlow)
                   .next(importLinksFlow)
                   .next(importLinkPointsFlow)
                   .end()
                   .build();
    }

    @Bean
    public Flow importNodesFlow(final Step prepareNodesStep,
                                final Step importNodesStep,
                                final Step commitNodesStep) {

        return new FlowBuilder<SimpleFlow>("importNodesFlow")
                .start(prepareNodesStep)
                .next(importNodesStep)
                .next(commitNodesStep)
                .build();
    }

    @Bean
    public Step prepareNodesStep(final INodeImportRepository nodeImportRepository) {
        return steps.get("prepareNodesStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCleanupTasklet<>(nodeImportRepository))
                    .build();
    }

    @Bean
    public Step importNodesStep(final NodeReader nodeReader,
                                final INodeImportRepository nodeImportRepository) {
        final int chunkSize = 1000;
        return steps.get("importNodesStep")
                    .allowStartIfComplete(true)
                    .<JrNode, ImportableNode>chunk(chunkSize)
                    .reader(nodeReader.build())
                    .processor(new NodeProcessor())
                    .writer(new GenericImportWriter<>(nodeImportRepository))
                    .build();
    }

    @Bean
    public Step commitNodesStep(final INodeImportRepository nodeImportRepository) {
        return steps.get("commitNodesStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCommitTasklet<>(nodeImportRepository))
                    .build();
    }

    @Bean
    public Flow importLinksFlow(final Step prepareLinksStep,
                                final Step importLinksStep,
                                final Step commitLinksStep) {

        return new FlowBuilder<SimpleFlow>("importLinksFlow")
                .start(prepareLinksStep)
                .next(importLinksStep)
                .next(commitLinksStep)
                .build();
    }

    @Bean
    public Step prepareLinksStep(final ILinkImportRepository linkImportRepository) {
        return steps.get("prepareLinksStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCleanupTasklet<>(linkImportRepository))
                    .build();
    }

    @Bean
    public Step importLinksStep(final LinkRowReader linkReader,
                                final ILinkImportRepository linkImportRepository) {
        final int chunkSize = 1000;
        return steps.get("importLinksStep")
                    .allowStartIfComplete(true)
                    .<LinkRow, ImportableLink>chunk(chunkSize)
                    .reader(linkReader.build())
                    .processor(new LinkRowProcessor())
                    .writer(new GenericImportWriter<>(linkImportRepository))
                    .build();
    }

    @Bean
    public Step commitLinksStep(final ILinkImportRepository linkImportRepository) {
        return steps.get("commitLinksStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCommitTasklet<>(linkImportRepository))
                    .build();
    }

    @Bean
    public Flow importLinkPointsFlow(final Step prepareLinkPointsStep,
                                     final Step importLinkPointsStep,
                                     final Step commitLinkPointsStep) {

        return new FlowBuilder<SimpleFlow>("importLinkPointsFlow")
                .start(prepareLinkPointsStep)
                .next(importLinkPointsStep)
                .next(commitLinkPointsStep)
                .build();
    }

    @Bean
    public Step prepareLinkPointsStep(final ILinkPointImportRepository linkPointImportRepository) {
        return steps.get("prepareLinkPointsStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCleanupTasklet<>(linkPointImportRepository))
                    .build();
    }

    @Bean
    public Step importLinkPointsStep(final PointReader pointReader,
                                     final ILinkPointImportRepository linkPointImportRepository) {
        final int chunkSize = 100;
        return steps.get("importLinkPointsStep")
                    .allowStartIfComplete(true)
                    .<LinkPoints, LinkGeometry>chunk(chunkSize)
                    .reader(new LinkPointReader(pointReader.build()))
                    .processor(new LinkPointProcessor())
                    .writer(new GenericImportWriter<>(linkPointImportRepository))
                    .build();
    }

    @Bean
    public Step commitLinkPointsStep(final ILinkPointImportRepository linkPointImportRepository) {
        return steps.get("commitLinkPointsStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCommitTasklet<>(linkPointImportRepository))
                    .build();
    }
}
