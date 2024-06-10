package fi.hsl.jore.importer.feature.batch.route_link;

import com.google.common.collect.FluentIterable;
import fi.hsl.jore.importer.feature.batch.common.GenericImportWriter;
import fi.hsl.jore.importer.feature.batch.route_link.dto.Jore3RoutePointsAndLinks;
import fi.hsl.jore.importer.feature.batch.route_link.support.IRouteLinkImportRepository;
import fi.hsl.jore.importer.feature.batch.route_link.support.IRoutePointImportRepository;
import fi.hsl.jore.importer.feature.batch.route_link.support.IRouteStopPointImportRepository;
import fi.hsl.jore.importer.feature.network.route_link.dto.Jore3RouteLink;
import fi.hsl.jore.importer.feature.network.route_link.dto.generated.RouteLinkPK;
import fi.hsl.jore.importer.feature.network.route_point.dto.Jore3RoutePoint;
import fi.hsl.jore.importer.feature.network.route_point.dto.generated.RoutePointPK;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.Jore3RouteStopPoint;
import fi.hsl.jore.importer.feature.network.route_stop_point.dto.generated.RouteStopPointPK;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;


public class RouteLinksWriter implements ItemWriter<Jore3RoutePointsAndLinks> {

    private final GenericImportWriter<Jore3RoutePoint, RoutePointPK> pointWriter;
    private final GenericImportWriter<Jore3RouteStopPoint, RouteStopPointPK> stopPointWriter;
    private final GenericImportWriter<Jore3RouteLink, RouteLinkPK> linkWriter;

    public RouteLinksWriter(final IRoutePointImportRepository routePointImportRepository,
                            final IRouteStopPointImportRepository routeStopPointImportRepository,
                            final IRouteLinkImportRepository routeLinkImportRepository) {
        pointWriter = new GenericImportWriter<>(routePointImportRepository);
        stopPointWriter = new GenericImportWriter<>(routeStopPointImportRepository);
        linkWriter = new GenericImportWriter<>(routeLinkImportRepository);
    }

    @Override
    public void write(final Chunk<? extends Jore3RoutePointsAndLinks> items) throws Exception {
        final FluentIterable<? extends Jore3RoutePointsAndLinks> fluentItems = FluentIterable.from(items.getItems());

        pointWriter.write(
            fluentItems
                .transformAndConcat(Jore3RoutePointsAndLinks::routePoints)
        );

        stopPointWriter.write(
            fluentItems
                .transformAndConcat(Jore3RoutePointsAndLinks::stopPoints)
        );

        linkWriter.write(
            fluentItems
                .transformAndConcat(Jore3RoutePointsAndLinks::routeLinks)
        );
    }

    @SuppressWarnings("unused")
    @AfterStep
    public ExitStatus afterStep(final StepExecution stepExecution) {

        pointWriter.afterStep(stepExecution);
        stopPointWriter.afterStep(stepExecution);
        linkWriter.afterStep(stepExecution);

        return stepExecution.getExitStatus();
    }
}
