package fi.hsl.jore.importer.feature.batch.route;

import static fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil.createMultilingualString;
import static org.assertj.core.api.Assertions.assertThat;

import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.jore3.util.JoreLocaleUtil;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4Route;
import fi.hsl.jore.importer.feature.jore4.entity.Jore4RouteDirection;
import fi.hsl.jore.importer.feature.jore4.entity.LegacyHslMunicipalityCode;
import fi.hsl.jore.importer.feature.network.direction_type.field.DirectionType;
import fi.hsl.jore.importer.feature.network.route.dto.ImporterRoute;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RouteExportProcessorTest {

    private static final UUID IMPORTER_ROUTE_DIRECTION_ID = UUID.fromString("2d28fda6-b497-436e-8093-c049a9bd7769");
    private static final DirectionType IMPORTER_ROUTE_DIRECTION = DirectionType.INBOUND;
    private static final Jore4RouteDirection JORE4_ROUTE_DIRECTION = Jore4RouteDirection.INBOUND;
    private static final int JORE4_ROUTE_PRIORITY = 10;
    private static final String FINNISH_NAME = "Keskustori - Etelä-Hervanta";
    private static final String SWEDISH_NAME = "Central torget - Södra Hervanta";
    private static final UUID JORE4_ID_OF_LINE = UUID.fromString("184b4710-9366-4500-aef3-39d03e95dde2");
    private static final String ROUTE_NUMBER = "30";
    private static final Optional<Short> ROUTE_HIDDEN_VARIANT = Optional.of((short) 1);
    private static final LocalDate VALIDITY_PERIOD_START_DAY = LocalDate.of(2021, 1, 1);
    private static final LocalDate VALIDITY_PERIOD_END_DAY = LocalDate.of(2022, 12, 31);
    private static final LegacyHslMunicipalityCode ROUTE_LEGACY_HSL_MUNICIPALITY_CODE =
            LegacyHslMunicipalityCode.HELSINKI;

    private static final ImporterRoute INPUT = ImporterRoute.of(
            ROUTE_NUMBER,
            ROUTE_HIDDEN_VARIANT,
            IMPORTER_ROUTE_DIRECTION_ID,
            IMPORTER_ROUTE_DIRECTION,
            createMultilingualString(FINNISH_NAME, SWEDISH_NAME),
            JORE4_ID_OF_LINE,
            DateRange.between(VALIDITY_PERIOD_START_DAY, VALIDITY_PERIOD_END_DAY),
            ROUTE_LEGACY_HSL_MUNICIPALITY_CODE);

    private final RouteExportProcessor processor = new RouteExportProcessor();

    @Test
    @DisplayName("Should return a route with a generated id")
    void shouldReturnRouteWithGeneratedId() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        assertThat(route.routeId()).isNotNull();
    }

    @Test
    @DisplayName("Should return a route with the correct Finnish name")
    void shouldReturnRouteWithCorrectFinnishName() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        final String finnishName = JoreLocaleUtil.getI18nString(route.description(), JoreLocaleUtil.FINNISH);
        assertThat(finnishName).isEqualTo(FINNISH_NAME);
    }

    @Test
    @DisplayName("Should return a route with the correct Swedish name")
    void shouldReturnRouteWithCorrectSwedishName() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        final String swedishName = JoreLocaleUtil.getI18nString(route.description(), JoreLocaleUtil.SWEDISH);
        assertThat(swedishName).isEqualTo(SWEDISH_NAME);
    }

    @Test
    @DisplayName("Should return a route with the correct direction")
    void shouldReturnRouteWithCorrectDirection() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        assertThat(route.direction()).isEqualTo(JORE4_ROUTE_DIRECTION);
    }

    @Test
    @DisplayName("Should return a route with the correct direction ext id")
    void shouldReturnRouteWithCorrectDirectionExtId() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        assertThat(route.directionExtId()).isEqualTo(IMPORTER_ROUTE_DIRECTION_ID);
    }

    @Test
    @DisplayName("Should return a route with the correct label")
    void shouldReturnRouteWithCorrectLabel() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        assertThat(route.label()).isEqualTo(ROUTE_NUMBER);
    }

    @Test
    @DisplayName("Should return a route with the correct hidden variant when there is none")
    void shouldReturnRouteWithCorrectHiddenVariantNull() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        assertThat(route.hiddenVariant()).isEqualTo(ROUTE_HIDDEN_VARIANT);
    }

    @Test
    @DisplayName("Should return a route with the correct Jore 4 ID of line")
    void shouldReturnRouteWithCorrectJore4IdOfLine() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        assertThat(route.lineId()).isEqualTo(JORE4_ID_OF_LINE);
    }

    @Test
    @DisplayName("Should return a route with the correct priority")
    void shouldReturnRouteWithCorrectPriority() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        assertThat(route.priority()).isEqualTo(JORE4_ROUTE_PRIORITY);
    }

    @Test
    @DisplayName("Should return a route with the correct validity period start time")
    void shouldReturnRouteWithCorrectValidityPeriodStartTime() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        assertThat(route.validityStart()).contains(VALIDITY_PERIOD_START_DAY);
    }

    @Test
    @DisplayName("Should return a route with the correct validity period end time")
    void shouldReturnRouteWithCorrectValidityPeriodEndTime() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        assertThat(route.validityEnd()).contains(VALIDITY_PERIOD_END_DAY);
    }

    @Test
    @DisplayName("Should return a route with the correct legacy HSL municipality code")
    void shouldReturnRouteWithCorrectLegacyHslMunicipalityCode() throws Exception {
        final Jore4Route route = processor.process(INPUT);
        assertThat(route.legacyHslMunicipalityCode()).isEqualTo(ROUTE_LEGACY_HSL_MUNICIPALITY_CODE);
    }
}
