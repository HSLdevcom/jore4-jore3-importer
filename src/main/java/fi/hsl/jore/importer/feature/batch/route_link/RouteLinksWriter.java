package fi.hsl.jore.importer.feature.batch.route_link;

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
import org.springframework.batch.item.ItemWriter;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
    public void write(final List<? extends Jore3RoutePointsAndLinks> items) throws Exception {
        final List<Jore3RoutePoint> points = items.stream()
                                                       .flatMap(item -> item.routePoints().toJavaStream())
                                                       .collect(toList());
        if (!points.isEmpty()) {
            pointWriter.write(points);
        }
        final List<Jore3RouteStopPoint> stopPoints = items.stream()
                                                               .flatMap(item -> item.stopPoints().toJavaStream())
                                                               .collect(toList());
        if (!stopPoints.isEmpty()) {
            stopPointWriter.write(stopPoints);
        }
        final List<Jore3RouteLink> links = items.stream()
                                                     .flatMap(item -> item.routeLinks().toJavaStream())
                                                     .collect(toList());
        if (!links.isEmpty()) {
            linkWriter.write(links);
        }
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
