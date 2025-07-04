package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.feature.batch.common.GenericCleanupTasklet;
import fi.hsl.jore.importer.feature.batch.common.GenericCommitTasklet;
import fi.hsl.jore.importer.feature.batch.common.GenericImportWriter;
import fi.hsl.jore.importer.feature.batch.common.Jore4SchemaCleanupTasklet;
import fi.hsl.jore.importer.feature.batch.line.LineExportProcessor;
import fi.hsl.jore.importer.feature.batch.line.LineExportReader;
import fi.hsl.jore.importer.feature.batch.line.LineExportWriter;
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
import fi.hsl.jore.importer.feature.batch.place.PlaceImportProcessor;
import fi.hsl.jore.importer.feature.batch.place.PlaceImportRowReader;
import fi.hsl.jore.importer.feature.batch.place.support.IPlaceImportRepository;
import fi.hsl.jore.importer.feature.batch.route.JourneyPatternExportProcessor;
import fi.hsl.jore.importer.feature.batch.route.JourneyPatternExportReader;
import fi.hsl.jore.importer.feature.batch.route.JourneyPatternExportWriter;
import fi.hsl.jore.importer.feature.batch.route.JourneyPatternStopExportProcessor;
import fi.hsl.jore.importer.feature.batch.route.JourneyPatternStopExportReader;
import fi.hsl.jore.importer.feature.batch.route.JourneyPatternStopExportWriter;
import fi.hsl.jore.importer.feature.batch.route.MapMatchingProcessor;
import fi.hsl.jore.importer.feature.batch.route.RouteExportProcessor;
import fi.hsl.jore.importer.feature.batch.route.RouteExportReader;
import fi.hsl.jore.importer.feature.batch.route.RouteExportWriter;
import fi.hsl.jore.importer.feature.batch.route.RouteGeometryExportReader;
import fi.hsl.jore.importer.feature.batch.route.RouteGeometryExportWriter;
import fi.hsl.jore.importer.feature.batch.route.RouteProcessor;
import fi.hsl.jore.importer.feature.batch.route.RouteReader;
import fi.hsl.jore.importer.feature.batch.route.support.IRouteImportRepository;
import fi.hsl.jore.importer.feature.batch.route_direction.RouteDirectionProcessor;
import fi.hsl.jore.importer.feature.batch.route_direction.RouteDirectionReader;
import fi.hsl.jore.importer.feature.batch.route_direction.support.IRouteDirectionImportRepository;
import fi.hsl.jore.importer.feature.batch.route_link.RouteLinkReader;
import fi.hsl.jore.importer.feature.batch.route_link.RouteLinksProcessor;
import fi.hsl.jore.importer.feature.batch.route_link.RouteLinksReader;
import fi.hsl.jore.importer.feature.batch.route_link.RouteLinksWriter;
import fi.hsl.jore.importer.feature.batch.route_link.dto.Jore3RoutePointsAndLinks;
import fi.hsl.jore.importer.feature.batch.route_link.dto.RouteLinksAndAttributes;
import fi.hsl.jore.importer.feature.batch.route_link.support.IRouteLinkImportRepository;
import fi.hsl.jore.importer.feature.batch.route_link.support.IRoutePointImportRepository;
import fi.hsl.jore.importer.feature.batch.route_link.support.IRouteStopPointImportRepository;
import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.ScheduledStopPointExportProcessor;
import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.ScheduledStopPointExportReader;
import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.ScheduledStopPointExportWriter;
import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.ScheduledStopPointImportProcessor;
import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.ScheduledStopPointImportReader;
import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.support.IScheduledStopPointImportRepository;
import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.timing_place.TimingPlaceExportProcessor;
import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.timing_place.TimingPlaceExportReader;
import fi.hsl.jore.importer.feature.batch.scheduled_stop_point.timing_place.TimingPlaceExportWriter;
import fi.hsl.jore.importer.feature.batch.stop_place.StopPlaceImportProcessor;
import fi.hsl.jore.importer.feature.batch.stop_place.StopPlaceImportReader;
import fi.hsl.jore.importer.feature.batch.stop_place.support.IStopPlaceImportRepository;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.Jore3Link;
import fi.hsl.jore.importer.feature.infrastructure.link_shape.dto.Jore3LinkShape;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.Jore3Node;
import fi.hsl.jore.importer.feature.jore3.entity.JrLine;
import fi.hsl.jore.importer.feature.jore3.entity.JrLineHeader;
import fi.hsl.jore.importer.feature.jore3.entity.JrNode;
import fi.hsl.jore.importer.feature.jore3.entity.JrPlace;
import fi.hsl.jore.importer.feature.jore3.entity.JrRoute;
import fi.hsl.jore.importer.feature.jore3.entity.JrRouteDirection;
import fi.hsl.jore.importer.feature.jore3.entity.JrScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore3.entity.JrStopPlace;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPattern;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4JourneyPatternStop;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Line;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Route;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteGeometry;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4ScheduledStopPoint;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4TimingPlace;
import fi.hsl.jore.importer.feature.network.line.dto.ImporterLine;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line_header.dto.Jore3LineHeader;
import fi.hsl.jore.importer.feature.network.place.dto.PersistablePlace;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPattern;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterJourneyPatternStop;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterRoute;
import fi.hsl.jore.importer.feature.network.route.dto.Jore3Route;
import fi.hsl.jore.importer.feature.network.route_direction.dto.Jore3RouteDirection;
import fi.hsl.jore.importer.feature.network.route_point.dto.ImporterRouteGeometry;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.ImporterScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.dto.Jore3ScheduledStopPoint;
import fi.hsl.jore.importer.feature.network.scheduled_stop_point.timing_place.ImporterTimingPlace;
import fi.hsl.jore.importer.feature.stops.stop_place.dto.Jore3StopPlace;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "fi.hsl.jore.importer.feature")
public class JobConfig {
    public static final String JOB_NAME = "importJoreJob";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public JobConfig(
            final JobRepository jobRepository,
            @BatchTransactionManager final PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    /* Steps to import data from Jore 3 to importer staging DB. */
    @Bean
    public Job importJob(
            final Flow importNodesFlow,
            final Flow importLinksFlow,
            final Flow importLinkPointsFlow,
            final Flow importLinesFlow,
            final Flow importLineHeadersFlow,
            final Flow importRoutesFlow,
            final Flow importRouteDirectionsFlow,
            final Flow importRouteLinksFlow,
            final Flow importPlacesFlow,
            final Flow importScheduledStopPointsFlow,
            final Flow importStopPlacesFlow,
            // Export data from the importer staging DB to Jore 4 DB.
            final Flow jore4ExportFlow) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(importNodesFlow)
                .next(importLinksFlow)
                .next(importLinkPointsFlow)
                .next(importLinesFlow)
                .next(importLineHeadersFlow)
                .next(importRoutesFlow)
                .next(importRouteDirectionsFlow)
                .next(importRouteLinksFlow)
                .next(importPlacesFlow)
                .next(importScheduledStopPointsFlow)
                .next(importStopPlacesFlow)
                .next(jore4ExportFlow)
                .end()
                .build();
    }

    @Bean
    public Flow importNodesFlow(final Step prepareNodesStep, final Step importNodesStep, final Step commitNodesStep) {

        return new FlowBuilder<SimpleFlow>("importNodesFlow")
                .start(prepareNodesStep)
                .next(importNodesStep)
                .next(commitNodesStep)
                .build();
    }

    @Bean
    public Step prepareNodesStep(final INodeImportRepository nodeImportRepository) {
        return new StepBuilder("prepareNodesStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(nodeImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step importNodesStep(final NodeReader nodeReader, final INodeImportRepository nodeImportRepository) {
        final int chunkSize = 1000;

        return new StepBuilder("importNodesStep", jobRepository)
                .allowStartIfComplete(true)
                .<JrNode, Jore3Node>chunk(chunkSize, transactionManager)
                .reader(nodeReader.build())
                .processor(new NodeProcessor())
                .writer(new GenericImportWriter<>(nodeImportRepository))
                .build();
    }

    @Bean
    public Step commitNodesStep(final INodeImportRepository nodeImportRepository) {
        return new StepBuilder("commitNodesStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(nodeImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Flow importLinksFlow(final Step prepareLinksStep, final Step importLinksStep, final Step commitLinksStep) {

        return new FlowBuilder<SimpleFlow>("importLinksFlow")
                .start(prepareLinksStep)
                .next(importLinksStep)
                .next(commitLinksStep)
                .build();
    }

    @Bean
    public Step prepareLinksStep(final ILinkImportRepository linkImportRepository) {
        return new StepBuilder("prepareLinksStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(linkImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step importLinksStep(final LinkRowReader linkReader, final ILinkImportRepository linkImportRepository) {
        final int chunkSize = 1000;
        return new StepBuilder("importLinksStep", jobRepository)
                .allowStartIfComplete(true)
                .<LinkRow, Jore3Link>chunk(chunkSize, transactionManager)
                .reader(linkReader.build())
                .processor(new LinkRowProcessor())
                .writer(new GenericImportWriter<>(linkImportRepository))
                .build();
    }

    @Bean
    public Step commitLinksStep(final ILinkImportRepository linkImportRepository) {
        return new StepBuilder("commitLinksStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(linkImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Flow importLinkPointsFlow(
            final Step prepareLinkPointsStep, final Step importLinkPointsStep, final Step commitLinkPointsStep) {

        return new FlowBuilder<SimpleFlow>("importLinkPointsFlow")
                .start(prepareLinkPointsStep)
                .next(importLinkPointsStep)
                .next(commitLinkPointsStep)
                .build();
    }

    @Bean
    public Step prepareLinkPointsStep(final ILinkShapeImportRepository linkPointImportRepository) {
        return new StepBuilder("prepareLinkPointsStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(linkPointImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step importLinkPointsStep(
            final PointReader pointReader, final ILinkShapeImportRepository linkPointImportRepository) {
        final int chunkSize = 100;
        return new StepBuilder("importLinkPointsStep", jobRepository)
                .allowStartIfComplete(true)
                .<LinkPoints, Jore3LinkShape>chunk(chunkSize, transactionManager)
                .reader(new LinkPointReader(pointReader.build()))
                .processor(new LinkPointProcessor())
                .writer(new GenericImportWriter<>(linkPointImportRepository))
                .build();
    }

    @Bean
    public Step commitLinkPointsStep(final ILinkShapeImportRepository linkPointImportRepository) {
        return new StepBuilder("commitLinkPointsStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(linkPointImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Flow importLinesFlow(final Step prepareLinesStep, final Step importLinesStep, final Step commitLinesStep) {

        return new FlowBuilder<SimpleFlow>("importLinesFlow")
                .start(prepareLinesStep)
                .next(importLinesStep)
                .next(commitLinesStep)
                .build();
    }

    @Bean
    public Step prepareLinesStep(final ILineImportRepository lineImportRepository) {
        return new StepBuilder("prepareLinesStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(lineImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step importLinesStep(final LineRowReader lineReader, final ILineImportRepository lineImportRepository) {
        final int chunkSize = 1000;
        return new StepBuilder("importLinesStep", jobRepository)
                .allowStartIfComplete(true)
                .<JrLine, PersistableLine>chunk(chunkSize, transactionManager)
                .reader(lineReader.build())
                .processor(new LineProcessor())
                .writer(new GenericImportWriter<>(lineImportRepository))
                .build();
    }

    @Bean
    public Step commitLinesStep(final ILineImportRepository lineImportRepository) {
        return new StepBuilder("commitLinesStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(lineImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Flow importLineHeadersFlow(
            final Step prepareLineHeadersStep, final Step importLineHeadersStep, final Step commitLineHeadersStep) {

        return new FlowBuilder<SimpleFlow>("importLineHeadersFlow")
                .start(prepareLineHeadersStep)
                .next(importLineHeadersStep)
                .next(commitLineHeadersStep)
                .build();
    }

    @Bean
    public Step prepareLineHeadersStep(final ILineHeaderImportRepository lineHeaderImportRepository) {
        return new StepBuilder("prepareLineHeadersStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(lineHeaderImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step importLineHeadersStep(
            final LineHeaderReader lineHeaderReader, final ILineHeaderImportRepository lineHeaderImportRepository) {
        final int chunkSize = 1;
        return new StepBuilder("importLineHeadersStep", jobRepository)
                .allowStartIfComplete(true)
                .<JrLineHeader, Jore3LineHeader>chunk(chunkSize, transactionManager)
                .reader(lineHeaderReader.build())
                .processor(new LineHeaderProcessor())
                .writer(new GenericImportWriter<>(lineHeaderImportRepository))
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .build();
    }

    @Bean
    public Step commitLineHeadersStep(final ILineHeaderImportRepository lineHeaderImportRepository) {
        return new StepBuilder("commitLineHeadersStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(lineHeaderImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Flow importRoutesFlow(
            final Step prepareRoutesStep, final Step importRoutesStep, final Step commitRoutesStep) {

        return new FlowBuilder<SimpleFlow>("importRoutesFlow")
                .start(prepareRoutesStep)
                .next(importRoutesStep)
                .next(commitRoutesStep)
                .build();
    }

    @Bean
    public Step prepareRoutesStep(final IRouteImportRepository routeImportRepository) {
        return new StepBuilder("prepareRoutesStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(routeImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step importRoutesStep(final RouteReader routeReader, final IRouteImportRepository routeImportRepository) {
        final int chunkSize = 1000;
        return new StepBuilder("importRoutesStep", jobRepository)
                .allowStartIfComplete(true)
                .<JrRoute, Jore3Route>chunk(chunkSize, transactionManager)
                .reader(routeReader.build())
                .processor(new RouteProcessor())
                .writer(new GenericImportWriter<>(routeImportRepository))
                .build();
    }

    @Bean
    public Step commitRoutesStep(final IRouteImportRepository routeImportRepository) {
        return new StepBuilder("commitRoutesStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(routeImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Flow importRouteDirectionsFlow(
            final Step prepareRouteDirectionsStep,
            final Step importRouteDirectionsStep,
            final Step commitRouteDirectionsStep) {

        return new FlowBuilder<SimpleFlow>("importRouteDirectionsFlow")
                .start(prepareRouteDirectionsStep)
                .next(importRouteDirectionsStep)
                .next(commitRouteDirectionsStep)
                .build();
    }

    @Bean
    public Step prepareRouteDirectionsStep(final IRouteDirectionImportRepository routeDirectionImportRepository) {
        return new StepBuilder("prepareRouteDirectionsStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(routeDirectionImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step importRouteDirectionsStep(
            final RouteDirectionReader routeDirectionReader,
            final IRouteDirectionImportRepository routeDirectionImportRepository) {
        final int chunkSize = 1000;
        return new StepBuilder("importRouteDirectionsStep", jobRepository)
                .allowStartIfComplete(true)
                .<JrRouteDirection, Jore3RouteDirection>chunk(chunkSize, transactionManager)
                .reader(routeDirectionReader.build())
                .processor(new RouteDirectionProcessor())
                .writer(new GenericImportWriter<>(routeDirectionImportRepository))
                .build();
    }

    @Bean
    public Step commitRouteDirectionsStep(final IRouteDirectionImportRepository routeDirectionImportRepository) {
        return new StepBuilder("commitRouteDirectionsStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(routeDirectionImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Flow importRouteLinksFlow(
            final Step prepareRoutePointsStep,
            final Step prepareRouteStopPointsStep,
            final Step prepareRouteLinksStep,
            final Step importRouteLinksStep,
            final Step commitRoutePointsStep,
            final Step commitRouteStopPointsStep,
            final Step commitRouteLinksStep) {

        return new FlowBuilder<SimpleFlow>("importRouteLinksFlow")
                .start(prepareRoutePointsStep)
                .next(prepareRouteStopPointsStep)
                .next(prepareRouteLinksStep)
                .next(importRouteLinksStep)
                .next(commitRoutePointsStep)
                .next(commitRouteStopPointsStep)
                .next(commitRouteLinksStep)
                .build();
    }

    @Bean
    public Step prepareRoutePointsStep(final IRoutePointImportRepository routePointImportRepository) {
        return new StepBuilder("prepareRoutePointsStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(routePointImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step prepareRouteStopPointsStep(final IRouteStopPointImportRepository routeStopPointImportRepository) {
        return new StepBuilder("prepareRouteStopPointsStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(routeStopPointImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step prepareRouteLinksStep(final IRouteLinkImportRepository routeLinkImportRepository) {
        return new StepBuilder("prepareRouteLinksStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(routeLinkImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step importRouteLinksStep(
            final RouteLinkReader routeLinkReader,
            final IRoutePointImportRepository routePointImportRepository,
            final IRouteStopPointImportRepository routeStopPointImportRepository,
            final IRouteLinkImportRepository routeLinkImportRepository) {
        final int chunkSize = 100;
        return new StepBuilder("importRouteLinksStep", jobRepository)
                .allowStartIfComplete(true)
                .<RouteLinksAndAttributes, Jore3RoutePointsAndLinks>chunk(chunkSize, transactionManager)
                .reader(new RouteLinksReader(routeLinkReader.build()))
                .processor(new RouteLinksProcessor())
                // Note how we write the route points, stop points, and route links to three different repositories
                .writer(new RouteLinksWriter(
                        routePointImportRepository, routeStopPointImportRepository, routeLinkImportRepository))
                .build();
    }

    @Bean
    public Step commitRoutePointsStep(final IRoutePointImportRepository routePointImportRepository) {
        return new StepBuilder("commitRoutePointsStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(routePointImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step commitRouteStopPointsStep(final IRouteStopPointImportRepository routeStopPointImportRepository) {
        return new StepBuilder("commitRouteStopPointsStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(routeStopPointImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step commitRouteLinksStep(final IRouteLinkImportRepository routeLinkImportRepository) {
        return new StepBuilder("commitRouteLinksStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(routeLinkImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Flow importPlacesFlow(
            final Step preparePlacesStep, final Step importPlacesStep, final Step commitPlacesStep) {

        return new FlowBuilder<SimpleFlow>("importPlacesFlow")
                .start(preparePlacesStep)
                .next(importPlacesStep)
                .next(commitPlacesStep)
                .build();
    }

    @Bean
    public Step preparePlacesStep(final IPlaceImportRepository placeImportRepository) {
        return new StepBuilder("preparePlacesStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(placeImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Step importPlacesStep(
            final PlaceImportRowReader placeReader, final IPlaceImportRepository placeImportRepository) {
        final int chunkSize = 1000;
        return new StepBuilder("importPlacesStep", jobRepository)
                .allowStartIfComplete(true)
                .<JrPlace, PersistablePlace>chunk(chunkSize, transactionManager)
                .reader(placeReader.build())
                .processor(new PlaceImportProcessor())
                .writer(new GenericImportWriter<>(placeImportRepository))
                .build();
    }

    @Bean
    public Step commitPlacesStep(final IPlaceImportRepository placeImportRepository) {
        return new StepBuilder("commitPlacesStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(placeImportRepository), transactionManager)
                .build();
    }

    @Bean
    public Flow importScheduledStopPointsFlow(
            final Step prepareScheduledStopPointsStep,
            final Step importScheduledStopPointsStep,
            final Step commitScheduledStopPointsStep) {
        return new FlowBuilder<SimpleFlow>("importScheduledStopPointsFlow")
                .start(prepareScheduledStopPointsStep)
                .next(importScheduledStopPointsStep)
                .next(commitScheduledStopPointsStep)
                .build();
    }

    @Bean
    public Step prepareScheduledStopPointsStep(final IScheduledStopPointImportRepository repository) {
        return new StepBuilder("prepareScheduledStopPointsStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(repository), transactionManager)
                .build();
    }

    @Bean
    public Step importScheduledStopPointsStep(
            final ScheduledStopPointImportReader reader, final IScheduledStopPointImportRepository repository) {
        return new StepBuilder("importScheduledStopPointsStep", jobRepository)
                .allowStartIfComplete(true)
                .<JrScheduledStopPoint, Jore3ScheduledStopPoint>chunk(1000, transactionManager)
                .reader(reader.build())
                .processor(new ScheduledStopPointImportProcessor())
                .writer(new GenericImportWriter<>(repository))
                .build();
    }

    @Bean
    public Step commitScheduledStopPointsStep(final IScheduledStopPointImportRepository repository) {
        return new StepBuilder("commitScheduledStopPointsStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(repository), transactionManager)
                .build();
    }

    @Bean
    public Flow importStopPlacesFlow(
            final Step prepareStopPlacesStep,
            final Step importStopPlacesStep,
            final Step commitStopPlacesStep) {
        return new FlowBuilder<SimpleFlow>("importStopPlacesFlow")
                .start(prepareStopPlacesStep)
                .next(importStopPlacesStep)
                .next(commitStopPlacesStep)
                .build();
    }

    @Bean
    public Step prepareStopPlacesStep(final IStopPlaceImportRepository repository) {
        return new StepBuilder("prepareStopPlacesStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCleanupTasklet<>(repository), transactionManager)
                .build();
    }

    @Bean
    public Step importStopPlacesStep(
            final StopPlaceImportReader reader,
            final IStopPlaceImportRepository repository) {
        return new StepBuilder("importStopPlacesStep", jobRepository)
                .allowStartIfComplete(true)
                .<JrStopPlace, Jore3StopPlace>chunk(1000, transactionManager)
                .reader(reader.build())
                .processor(new StopPlaceImportProcessor())
                .writer(new GenericImportWriter<>(repository))
                .build();
    }

    @Bean
    public Step commitStopPlacesStep(final IStopPlaceImportRepository repository) {
        return new StepBuilder("commitStopPlacesStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new GenericCommitTasklet<>(repository), transactionManager)
                .build();
    }

    @Bean
    public Flow jore4ExportFlow(
            final Step prepareJore4ExportStep,
            final Step exportTimingPlacesStep,
            final Step exportScheduledStopPointsStep,
            final Step exportLinesStep,
            final Step exportRoutesStep,
            final Step exportRouteGeometriesStep,
            final Step exportJourneyPatternsStep,
            final Step exportJourneyPatternStopsStep) {
        return new FlowBuilder<SimpleFlow>("jore4ExportFlow")
                .start(prepareJore4ExportStep)
                .next(exportTimingPlacesStep)
                .next(exportScheduledStopPointsStep)
                .next(exportLinesStep)
                .next(exportRoutesStep)
                .next(exportRouteGeometriesStep)
                .next(exportJourneyPatternsStep)
                .next(exportJourneyPatternStopsStep)
                .build();
    }

    @Bean
    public Step prepareJore4ExportStep(final Jore4SchemaCleanupTasklet cleanupTasklet) {
        return new StepBuilder("prepareJore4ExportStep", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(cleanupTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step exportTimingPlacesStep(final TimingPlaceExportReader reader, final TimingPlaceExportWriter writer) {
        return new StepBuilder("exportTimingPlacesStep", jobRepository)
                .allowStartIfComplete(true)
                .<ImporterTimingPlace, Jore4TimingPlace>chunk(1, transactionManager)
                .reader(reader.build())
                .processor(new TimingPlaceExportProcessor())
                .writer(writer)
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .listener(new StatisticsLoggingStepExecutionListener())
                .build();
    }

    @Bean
    public Step exportScheduledStopPointsStep(
            final ScheduledStopPointExportReader reader,
            final ScheduledStopPointExportProcessor processor,
            final ScheduledStopPointExportWriter writer) {
        return new StepBuilder("exportScheduledStopPointsStep", jobRepository)
                .allowStartIfComplete(true)
                .<ImporterScheduledStopPoint, Jore4ScheduledStopPoint>chunk(1, transactionManager)
                .reader(reader.build())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .listener(new StatisticsLoggingStepExecutionListener())
                .build();
    }

    @Bean
    public Step exportLinesStep(
            final LineExportReader reader, final LineExportProcessor processor, final LineExportWriter writer) {
        return new StepBuilder("exportLinesStep", jobRepository)
                .allowStartIfComplete(true)
                .<ImporterLine, Jore4Line>chunk(1, transactionManager)
                .reader(reader.build())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .listener(new StatisticsLoggingStepExecutionListener())
                .build();
    }

    @Bean
    public Step exportRoutesStep(
            final RouteExportReader reader, final RouteExportProcessor processor, final RouteExportWriter writer) {
        return new StepBuilder("exportRoutesStep", jobRepository)
                .allowStartIfComplete(true)
                .<ImporterRoute, Jore4Route>chunk(1, transactionManager)
                .reader(reader.build())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .listener(new StatisticsLoggingStepExecutionListener())
                .build();
    }

    @Bean
    public Step exportRouteGeometriesStep(
            final RouteGeometryExportReader reader,
            final MapMatchingProcessor processor,
            final RouteGeometryExportWriter writer) {
        return new StepBuilder("exportRouteGeometriesStep", jobRepository)
                .allowStartIfComplete(true)
                .<ImporterRouteGeometry, Jore4RouteGeometry>chunk(1, transactionManager)
                .reader(reader.build())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .listener(new StatisticsLoggingStepExecutionListener())
                .build();
    }

    @Bean
    public Step exportJourneyPatternsStep(
            final JourneyPatternExportReader reader,
            final JourneyPatternExportProcessor processor,
            final JourneyPatternExportWriter writer) {
        return new StepBuilder("exportJourneyPatternsStep", jobRepository)
                .allowStartIfComplete(true)
                .<ImporterJourneyPattern, Jore4JourneyPattern>chunk(1, transactionManager)
                .reader(reader.build())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .listener(new StatisticsLoggingStepExecutionListener())
                .build();
    }

    @Bean
    public Step exportJourneyPatternStopsStep(
            final JourneyPatternStopExportReader reader,
            final JourneyPatternStopExportProcessor processor,
            final JourneyPatternStopExportWriter writer) {
        return new StepBuilder("exportJourneyPatternStopsStep", jobRepository)
                .allowStartIfComplete(true)
                .<ImporterJourneyPatternStop, Jore4JourneyPatternStop>chunk(1000, transactionManager)
                .reader(reader.build())
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .listener(new StatisticsLoggingStepExecutionListener())
                .build();
    }
}
