package fi.hsl.jore.importer.feature.batch.route;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import fi.hsl.jore.importer.feature.network.route.dto.ExportableRoute;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRoute;
import fi.hsl.jore.importer.feature.transmodel.entity.TransmodelRouteDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_END_TIME;
import static fi.hsl.jore.importer.TestConstants.OPERATING_DAY_START_TIME;
import static fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil.createMultilingualString;
import static fi.hsl.jore.importer.feature.transmodel.util.TimestampFactory.offsetDateTimeFromLocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class RouteExportProcessorTest {

    private static final DirectionType IMPORTER_ROUTE_DIRECTION = DirectionType.INBOUND;
    private static final TransmodelRouteDirection JORE4_ROUTE_DIRECTION = TransmodelRouteDirection.INBOUND;
    private static final int JORE4_ROUTE_PRIORITY = 10;
    private static final String ROUTE_EXTERNAL_ID = "1001";
    private static final String FINNISH_NAME = "Keskustori - Etelä-Hervanta";
    private static final String SWEDISH_NAME = "Central torget - Södra Hervanta";
    private static final UUID LINE_TRANSMODEL_ID = UUID.fromString("184b4710-9366-4500-aef3-39d03e95dde2");
    private static final String ROUTE_NUMBER = "30";
    private static final UUID START_SCHEDULED_STOP_POINT_TRANSMODEL_ID = UUID.fromString("19a756fa-0a4b-4cf8-99f9-b2e347dd5568");
    private static final UUID END_SCHEDULED_STOP_POINT_TRANSMODEL_ID = UUID.fromString("33bca2cd-d7ee-494a-844d-8915c4de1893");

    private static final LocalDate VALIDITY_PERIOD_START_DAY = LocalDate.of(2021, 1, 1);
    private static final LocalDate VALIDITY_PERIOD_END_DAY = LocalDate.of(2022, 12, 31);

    private static final LocalDateTime VALIDITY_PERIOD_START_TIME = LocalDateTime.of(
            VALIDITY_PERIOD_START_DAY,
            OPERATING_DAY_START_TIME
    );
    private static final LocalDateTime VALIDITY_PERIOD_END_TIME = LocalDateTime.of(
            VALIDITY_PERIOD_END_DAY.plusDays(1),
            OPERATING_DAY_END_TIME
    );

    private static final ExportableRoute INPUT = ExportableRoute.of(
            IMPORTER_ROUTE_DIRECTION,
            ExternalId.of(ROUTE_EXTERNAL_ID),
            createMultilingualString(FINNISH_NAME, SWEDISH_NAME),
            LINE_TRANSMODEL_ID,
            ROUTE_NUMBER,
            START_SCHEDULED_STOP_POINT_TRANSMODEL_ID,
            END_SCHEDULED_STOP_POINT_TRANSMODEL_ID,
            DateRange.between(VALIDITY_PERIOD_START_DAY, VALIDITY_PERIOD_END_DAY)
    );

    private final RouteExportProcessor processor = new RouteExportProcessor();

    @Test
    @DisplayName("Should return a route with a generated id")
    void shouldReturnRouteWithGeneratedId() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        assertThat(route.routeId()).isNotNull();
    }

    @Test
    @DisplayName("Should return a route with the correct Finnish name")
    void shouldReturnRouteWithCorrectFinnishName() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        final String finnishName = JoreLocaleUtil.getI18nString(route.description(), JoreLocaleUtil.FINNISH);
        assertThat(finnishName).isEqualTo(FINNISH_NAME);
    }

    @Test
    @DisplayName("Should return a route with the correct Swedish name")
    void shouldReturnRouteWithCorrectSwedishName() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        final String swedishName = JoreLocaleUtil.getI18nString(route.description(), JoreLocaleUtil.SWEDISH);
        assertThat(swedishName).isEqualTo(SWEDISH_NAME);
    }

    @Test
    @DisplayName("Should return a route with the correct direction")
    void shouldReturnRouteWithCorrectDirection() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        assertThat(route.direction()).isEqualTo(JORE4_ROUTE_DIRECTION);
    }

    @Test
    @DisplayName("Should return a route wtih the correct external id")
    void shouldReturnRouteWithCorrectExternalId() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        assertThat(route.externalId()).isEqualTo(ROUTE_EXTERNAL_ID);
    }

    @Test
    @DisplayName("Should return a route with the correct label")
    void shouldReturnRouteWithCorrectLabel() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        assertThat(route.label()).isEqualTo(ROUTE_NUMBER);
    }

    @Test
    @DisplayName("Should return a route with the correct line transmodel id")
    void shouldReturnRouteWithCorrectLineTransmodelId() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        assertThat(route.lineId()).isEqualTo(LINE_TRANSMODEL_ID.toString());
    }

    @Test
    @DisplayName("Should return a route with the correct priority")
    void shouldReturnRouteWithCorrectPriority() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        assertThat(route.priority()).isEqualTo(JORE4_ROUTE_PRIORITY);
    }

    @Test
    @DisplayName("Should return a route with the correct start scheduled stop point id")
    void shouldReturnRouteWithCorrectStartScheduledStopPointId() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        assertThat(route.startScheduledStopPointId()).isEqualTo(START_SCHEDULED_STOP_POINT_TRANSMODEL_ID.toString());
    }

    @Test
    @DisplayName("Should return a route with the correct end scheduled stop point id")
    void shouldReturnRouteWithCorrectEndScheduledStopPointId() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        assertThat(route.endScheduledStopPointId()).isEqualTo(END_SCHEDULED_STOP_POINT_TRANSMODEL_ID.toString());
    }

    @Test
    @DisplayName("Should return a route with the correct validity period start time")
    void shouldReturnRouteWithCorrectValidityPeriodStartTime() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        assertThat(route.validityStart()).contains(offsetDateTimeFromLocalDateTime(VALIDITY_PERIOD_START_TIME));
    }

    @Test
    @DisplayName("Should return a route with the correct validity period end time")
    void shouldReturnRouteWithCorrectValidityPeriodEndTime() throws Exception {
        final TransmodelRoute route = processor.process(INPUT);
        assertThat(route.validityEnd()).contains(offsetDateTimeFromLocalDateTime(VALIDITY_PERIOD_END_TIME));
    }
}
