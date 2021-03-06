package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.feature.batch.common.GenericCleanupTasklet;
import fi.hsl.jore.importer.feature.batch.common.GenericCommitTasklet;
import fi.hsl.jore.importer.feature.batch.common.GenericImportWriter;
import fi.hsl.jore.importer.feature.batch.line.LineProcessor;
import fi.hsl.jore.importer.feature.batch.line.LineRowReader;
import fi.hsl.jore.importer.feature.batch.line.support.ILineImportRepository;
import fi.hsl.jore.importer.feature.batch.line_header.LineHeaderProcessor;
import fi.hsl.jore.importer.feature.batch.line_header.LineHeaderReader;
import fi.hsl.jore.importer.feature.batch.line_header.support.ILineHeaderImportRepository;
import fi.hsl.jore.importer.feature.batch.link.LinkRowProcessor;
import fi.hsl.jore.importer.feature.batch.link.LinkRowReader;
import fi.hsl.jore.importer.feature.batch.link.dto.LinkRow;
import fi.hsl.jore.importer.feature.batch.link.support.ILinkImportRepository;
import fi.hsl.jore.importer.feature.batch.link_shape.LinkPointProcessor;
import fi.hsl.jore.importer.feature.batch.link_shape.LinkPointReader;
import fi.hsl.jore.importer.feature.batch.link_shape.PointReader;
import fi.hsl.jore.importer.feature.batch.link_shape.dto.LinkPoints;
import fi.hsl.jore.importer.feature.batch.link_shape.support.ILinkShapeImportRepository;
import fi.hsl.jore.importer.feature.batch.node.NodeProcessor;
import fi.hsl.jore.importer.feature.batch.node.NodeReader;
import fi.hsl.jore.importer.feature.batch.node.support.INodeImportRepository;
import fi.hsl.jore.importer.feature.batch.route.RouteProcessor;
import fi.hsl.jore.importer.feature.batch.route.RouteReader;
import fi.hsl.jore.importer.feature.batch.route.support.IRouteImportRepository;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.ImportableLink;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.ImportableLinkShape;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.ImportableNode;
import fi.hsl.jore.importer.feature.jore3.entity.JrLine;
import fi.hsl.jore.importer.feature.jore3.entity.JrLineHeader;
import fi.hsl.jore.importer.feature.jore3.entity.JrNode;
import fi.hsl.jore.importer.feature.jore3.entity.JrRoute;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line_header.dto.ImportableLineHeader;
import fi.hsl.jore.importer.feature.network.route.dto.ImportableRoute;
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
                         final Flow importLinkPointsFlow,
                         final Flow importLinesFlow,
                         final Flow importLineHeadersFlow,
                         final Flow importRoutesFlow) {
        return jobs.get(JOB_NAME)
                   .start(importNodesFlow)
                   .next(importLinksFlow)
                   .next(importLinkPointsFlow)
                   .next(importLinesFlow)
                   .next(importLineHeadersFlow)
                   .next(importRoutesFlow)
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
    public Step prepareLinkPointsStep(final ILinkShapeImportRepository linkPointImportRepository) {
        return steps.get("prepareLinkPointsStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCleanupTasklet<>(linkPointImportRepository))
                    .build();
    }

    @Bean
    public Step importLinkPointsStep(final PointReader pointReader,
                                     final ILinkShapeImportRepository linkPointImportRepository) {
        final int chunkSize = 100;
        return steps.get("importLinkPointsStep")
                    .allowStartIfComplete(true)
                    .<LinkPoints, ImportableLinkShape>chunk(chunkSize)
                    .reader(new LinkPointReader(pointReader.build()))
                    .processor(new LinkPointProcessor())
                    .writer(new GenericImportWriter<>(linkPointImportRepository))
                    .build();
    }

    @Bean
    public Step commitLinkPointsStep(final ILinkShapeImportRepository linkPointImportRepository) {
        return steps.get("commitLinkPointsStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCommitTasklet<>(linkPointImportRepository))
                    .build();
    }

    @Bean
    public Flow importLinesFlow(final Step prepareLinesStep,
                                final Step importLinesStep,
                                final Step commitLinesStep) {

        return new FlowBuilder<SimpleFlow>("importLinesFlow")
                .start(prepareLinesStep)
                .next(importLinesStep)
                .next(commitLinesStep)
                .build();
    }

    @Bean
    public Step prepareLinesStep(final ILineImportRepository lineImportRepository) {
        return steps.get("prepareLinesStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCleanupTasklet<>(lineImportRepository))
                    .build();
    }

    @Bean
    public Step importLinesStep(final LineRowReader lineReader,
                                final ILineImportRepository lineImportRepository) {
        final int chunkSize = 1000;
        return steps.get("importLinesStep")
                    .allowStartIfComplete(true)
                    .<JrLine, PersistableLine>chunk(chunkSize)
                    .reader(lineReader.build())
                    .processor(new LineProcessor())
                    .writer(new GenericImportWriter<>(lineImportRepository))
                    .build();
    }

    @Bean
    public Step commitLinesStep(final ILineImportRepository lineImportRepository) {
        return steps.get("commitLinesStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCommitTasklet<>(lineImportRepository))
                    .build();
    }

    @Bean
    public Flow importLineHeadersFlow(final Step prepareLineHeadersStep,
                                      final Step importLineHeadersStep,
                                      final Step commitLineHeadersStep) {

        return new FlowBuilder<SimpleFlow>("importLineHeadersFlow")
                .start(prepareLineHeadersStep)
                .next(importLineHeadersStep)
                .next(commitLineHeadersStep)
                .build();
    }

    @Bean
    public Step prepareLineHeadersStep(final ILineHeaderImportRepository lineHeaderImportRepository) {
        return steps.get("prepareLineHeadersStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCleanupTasklet<>(lineHeaderImportRepository))
                    .build();
    }

    @Bean
    public Step importLineHeadersStep(final LineHeaderReader lineHeaderReader,
                                      final ILineHeaderImportRepository lineHeaderImportRepository) {
        final int chunkSize = 1000;
        return steps.get("importLineHeadersStep")
                    .allowStartIfComplete(true)
                    .<JrLineHeader, ImportableLineHeader>chunk(chunkSize)
                    .reader(lineHeaderReader.build())
                    .processor(new LineHeaderProcessor())
                    .writer(new GenericImportWriter<>(lineHeaderImportRepository))
                    .build();
    }

    @Bean
    public Step commitLineHeadersStep(final ILineHeaderImportRepository lineHeaderImportRepository) {
        return steps.get("commitLineHeadersStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCommitTasklet<>(lineHeaderImportRepository))
                    .build();
    }

    @Bean
    public Flow importRoutesFlow(final Step prepareRoutesStep,
                                 final Step importRoutesStep,
                                 final Step commitRoutesStep) {

        return new FlowBuilder<SimpleFlow>("importRoutesFlow")
                .start(prepareRoutesStep)
                .next(importRoutesStep)
                .next(commitRoutesStep)
                .build();
    }

    @Bean
    public Step prepareRoutesStep(final IRouteImportRepository routeImportRepository) {
        return steps.get("prepareRoutesStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCleanupTasklet<>(routeImportRepository))
                    .build();
    }

    @Bean
    public Step importRoutesStep(final RouteReader routeReader,
                                 final IRouteImportRepository routeImportRepository) {
        final int chunkSize = 1000;
        return steps.get("importRoutesStep")
                    .allowStartIfComplete(true)
                    .<JrRoute, ImportableRoute>chunk(chunkSize)
                    .reader(routeReader.build())
                    .processor(new RouteProcessor())
                    .writer(new GenericImportWriter<>(routeImportRepository))
                    .build();
    }

    @Bean
    public Step commitRoutesStep(final IRouteImportRepository routeImportRepository) {
        return steps.get("commitRoutesStep")
                    .allowStartIfComplete(true)
                    .tasklet(new GenericCommitTasklet<>(routeImportRepository))
                    .build();
    }
}
