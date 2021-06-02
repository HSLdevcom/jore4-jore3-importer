package fi.hsl.jore.importer.feature.batch.point;

import fi.hsl.jore.importer.feature.batch.point.dto.LinkEndpoints;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkPoints;
import fi.hsl.jore.importer.feature.batch.point.dto.PointRow;
import fi.hsl.jore.importer.feature.jore3.entity.ImmutableJrPoint;
import fi.hsl.jore.importer.feature.jore3.entity.JrPoint;
import fi.hsl.jore.importer.feature.jore3.field.TransitType;
import fi.hsl.jore.importer.feature.jore3.field.generated.NodeId;
import fi.hsl.jore.importer.feature.jore3.key.JrLinkPk;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReaderStateTest {

    private static final JrPoint POINT_1 = JrPoint.of(TransitType.BUS,
                                                      NodeId.of("1"),
                                                      NodeId.of("2"),
                                                      1,
                                                      1,
                                                      60.1,
                                                      24.1);
    private static final JrPoint POINT_2 = ImmutableJrPoint.copyOf(POINT_1)
                                                           .withPointId(2)
                                                           .withOrderNumber(2)
                                                           .withLatitude(60.2)
                                                           .withLongitude(24.2);
    private static final LinkEndpoints ENDPOINTS = LinkEndpoints.of(60.0,
                                                                    24.0,
                                                                    61.0,
                                                                    25.0);

    @Test
    public void whenGivenInitialState_thenFieldsAreEmpty() {
        final LinkPointReader.ReaderState state =
                LinkPointReader.ReaderState
                        .init();

        assertThat(state.link().isEmpty(),
                   is(true));
        assertThat(state.endpoints().isEmpty(),
                   is(true));
        assertThat(state.accumulator().contents().isEmpty(),
                   is(true));
        assertThat(state.pendingResult().isEmpty(),
                   is(true));
        assertThat(state.result().isEmpty(),
                   is(true));
        assertThat(state.exhausted(),
                   is(false));
    }

    @Test
    public void whenNoRows_thenFieldsAreEmptyAndExhausted() {
        final LinkPointReader.ReaderState state =
                LinkPointReader.ReaderState
                        .init()
                        .onItem(null);

        assertThat(state.link().isEmpty(),
                   is(true));
        assertThat(state.endpoints().isEmpty(),
                   is(true));
        assertThat(state.accumulator().contents().isEmpty(),
                   is(true));
        assertThat(state.pendingResult().isEmpty(),
                   is(true));
        assertThat(state.result().isEmpty(),
                   is(true));
        assertThat(state.exhausted(),
                   is(true));
    }

    @Test
    public void whenSingleRow_thenAValidResultIsPending() {
        final LinkPointReader.ReaderState state =
                LinkPointReader.ReaderState
                        .init()
                        .onItem(PointRow.of(POINT_1,
                                            ENDPOINTS));

        final JrLinkPk expectedLinkPk =
                JrLinkPk.of(TransitType.BUS,
                            NodeId.of("1"),
                            NodeId.of("2"));

        final LinkPoints expectedLinkPoints =
                LinkPoints.of(expectedLinkPk,
                              ENDPOINTS,
                              List.of(POINT_1));

        assertThat(state.link().isEmpty(),
                   is(false));
        assertThat(state.link().get(),
                   is(expectedLinkPk));
        assertThat(state.endpoints().isEmpty(),
                   is(false));
        assertThat(state.endpoints().get(),
                   is(ENDPOINTS));
        assertThat(state.accumulator().contents().isEmpty(),
                   is(false));
        assertThat(state.accumulator().asList(),
                   is(List.of(POINT_1)));
        assertThat(state.pendingResult().isEmpty(),
                   is(false));
        assertThat(state.pendingResult().get(),
                   is(expectedLinkPoints));
        assertThat(state.result().isEmpty(),
                   is(true));
        assertThat(state.exhausted(),
                   is(false));
    }

    @Test
    public void whenSingleRow_andNoOtherResults_thenAValidResultIsAvailableAndExhausted() {
        final LinkPointReader.ReaderState state =
                LinkPointReader.ReaderState
                        .init()
                        .onItem(PointRow.of(POINT_1,
                                            ENDPOINTS))
                        .onItem(null);

        final JrLinkPk expectedLinkPk =
                JrLinkPk.of(TransitType.BUS,
                            NodeId.of("1"),
                            NodeId.of("2"));

        final LinkPoints expectedLinkPoints =
                LinkPoints.of(expectedLinkPk,
                              ENDPOINTS,
                              List.of(POINT_1));

        assertThat(state.link().isEmpty(),
                   is(false));
        assertThat(state.link().get(),
                   is(expectedLinkPk));
        assertThat(state.endpoints().isEmpty(),
                   is(false));
        assertThat(state.endpoints().get(),
                   is(ENDPOINTS));
        assertThat(state.accumulator().contents().isEmpty(),
                   is(false));
        assertThat(state.accumulator().asList(),
                   is(List.of(POINT_1)));
        assertThat(state.pendingResult().isEmpty(),
                   is(false));
        assertThat(state.pendingResult().get(),
                   is(expectedLinkPoints));
        assertThat(state.result().isEmpty(),
                   is(false));
        assertThat(state.result().get(),
                   is(expectedLinkPoints));
        assertThat(state.exhausted(),
                   is(true));
    }

    @Test
    public void whenTwoRows_andNoOtherResults_thenAValidResultIsAvailableAndExhausted() {
        final LinkPointReader.ReaderState state =
                LinkPointReader.ReaderState
                        .init()
                        .onItem(PointRow.of(POINT_1,
                                            ENDPOINTS))
                        .onItem(PointRow.of(POINT_2,
                                            ENDPOINTS))
                        .onItem(null);

        final JrLinkPk expectedLinkPk =
                JrLinkPk.of(TransitType.BUS,
                            NodeId.of("1"),
                            NodeId.of("2"));

        final LinkPoints expectedLinkPoints =
                LinkPoints.of(expectedLinkPk,
                              ENDPOINTS,
                              List.of(POINT_1,
                                      POINT_2));

        assertThat(state.link().isEmpty(),
                   is(false));
        assertThat(state.link().get(),
                   is(expectedLinkPk));
        assertThat(state.endpoints().isEmpty(),
                   is(false));
        assertThat(state.endpoints().get(),
                   is(ENDPOINTS));
        assertThat(state.accumulator().contents().isEmpty(),
                   is(false));
        assertThat(state.accumulator().asList(),
                   is(List.of(POINT_1, POINT_2)));
        assertThat(state.pendingResult().isEmpty(),
                   is(false));
        assertThat(state.pendingResult().get(),
                   is(expectedLinkPoints));
        assertThat(state.result().isEmpty(),
                   is(false));
        assertThat(state.result().get(),
                   is(expectedLinkPoints));
        assertThat(state.exhausted(),
                   is(true));
    }
}
