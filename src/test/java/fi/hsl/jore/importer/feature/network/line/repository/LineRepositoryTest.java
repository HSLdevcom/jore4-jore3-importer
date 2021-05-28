package fi.hsl.jore.importer.feature.network.line.repository;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.network.line.dto.Line;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LineRepositoryTest extends IntegrationTest {

    private static final String LINE_NUMBER = "1005";
    private static final String DISPLAY_LINE_NUMBER = "5";
    private static final ExternalId EXT_ID = ExternalIdUtil.forLine(LineId.from(LINE_NUMBER));
    private static final NetworkType NETWORK = NetworkType.ROAD;

    private final ILineTestRepository lineRepository;

    public LineRepositoryTest(@Autowired final ILineTestRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @BeforeEach
    public void beforeEach() {
        assertThat("Line repository should be empty at the start of the test",
                   lineRepository.empty(),
                   is(true));
    }

    @Test
    public void whenPersistingLine_thenPersistedLineIsEqual() {
        final LinePK id = lineRepository.insert(
                PersistableLine.of(EXT_ID,
                                   DISPLAY_LINE_NUMBER,
                                   NETWORK)
        );

        assertThat(lineRepository.count(),
                   is(1));

        final Optional<Line> maybeLine = lineRepository.findById(id);

        assertThat(maybeLine.isPresent(),
                   is(true));

        final Line line = maybeLine.orElseThrow();

        assertThat(line.alive(),
                   is(true));
        assertThat(line.externalId(),
                   is(EXT_ID));
        assertThat(line.lineNumber(),
                   is(DISPLAY_LINE_NUMBER));
        assertThat(line.networkType(),
                   is(NETWORK));
    }
}
