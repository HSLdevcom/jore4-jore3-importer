package fi.hsl.jore.importer.feature.network.line_header.repository;

import fi.hsl.jore.importer.IntegrationTest;
import fi.hsl.jore.importer.config.jooq.converter.date_range.DateRange;
import fi.hsl.jore.importer.feature.batch.util.ExternalIdUtil;
import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import fi.hsl.jore.importer.feature.common.dto.field.generated.ExternalId;
import fi.hsl.jore.importer.feature.infrastructure.network_type.dto.NetworkType;
import fi.hsl.jore.importer.feature.jore3.field.LineId;
import fi.hsl.jore.importer.feature.network.line.dto.PersistableLine;
import fi.hsl.jore.importer.feature.network.line.dto.generated.LinePK;
import fi.hsl.jore.importer.feature.network.line.repository.ILineTestRepository;
import fi.hsl.jore.importer.feature.network.line_header.dto.LineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.PersistableLineHeader;
import fi.hsl.jore.importer.feature.network.line_header.dto.generated.LineHeaderPK;
import fi.hsl.jore.importer.feature.jore4.entity.TypeOfLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LineHeaderRepositoryTest extends IntegrationTest {

    private static final String LINE_NUMBER = "1005";
    private static final String DISPLAY_LINE_NUMBER = "5";
    private static final ExternalId LINE_EXT_ID = ExternalIdUtil.forLine(LineId.from(LINE_NUMBER));
    private static final NetworkType NETWORK = NetworkType.ROAD;
    private static final MultilingualString NAME = MultilingualString.empty()
                                                                     .with(Locale.ENGLISH, "name");
    private static final MultilingualString NAME_SHORT = MultilingualString.empty()
                                                                           .with(Locale.ENGLISH, "name-short");
    private static final MultilingualString ORIGIN_1 = MultilingualString.empty()
                                                                         .with(Locale.ENGLISH, "origin-1");
    private static final MultilingualString ORIGIN_2 = MultilingualString.empty()
                                                                         .with(Locale.ENGLISH, "origin-2");
    private static final DateRange VALID_TIME = DateRange.between(LocalDate.of(2020, 5, 6),
                                                                  LocalDate.of(2021, 6, 25));
    private static final ExternalId HEADER_EXT_ID = ExternalIdUtil.forLineHeader(LineId.from(LINE_NUMBER),
                                                                                 VALID_TIME.range().lowerEndpoint());
    private final TypeOfLine TYPE_OF_LINE = TypeOfLine.STOPPING_BUS_SERVICE;

    private final ILineTestRepository lineRepository;
    private final ILineHeaderTestRepository lineHeaderRepository;

    public LineHeaderRepositoryTest(@Autowired final ILineTestRepository lineRepository,
                                    @Autowired final ILineHeaderTestRepository lineHeaderRepository) {
        this.lineRepository = lineRepository;
        this.lineHeaderRepository = lineHeaderRepository;
    }

    @BeforeEach
    public void beforeEach() {
        assertThat("Line repository should be empty at the start of the test",
                   lineRepository.empty(),
                   is(true));
        assertThat("Line header repository should be empty at the start of the test",
                   lineHeaderRepository.empty(),
                   is(true));
    }

    @Test
    public void whenPersistingLine_thenPersistedLineIsEqual() {
        final LinePK lineId = lineRepository.insert(
                PersistableLine.of(LINE_EXT_ID,
                                   DISPLAY_LINE_NUMBER,
                                   NETWORK,
                                   TYPE_OF_LINE
                )
        );

        final LineHeaderPK id = lineHeaderRepository.insert(
                PersistableLineHeader.of(HEADER_EXT_ID,
                                         lineId,
                                         NAME,
                                         NAME_SHORT,
                                         ORIGIN_1,
                                         ORIGIN_2,
                                         VALID_TIME)
        );

        assertThat(lineHeaderRepository.count(),
                   is(1));

        final Optional<LineHeader> maybeLineHeader = lineHeaderRepository.findById(id);

        assertThat(maybeLineHeader.isPresent(),
                   is(true));

        final LineHeader lineHeader = maybeLineHeader.orElseThrow();

        assertThat(lineHeader.alive(),
                   is(true));
        assertThat(lineHeader.externalId(),
                   is(HEADER_EXT_ID));
        assertThat(lineHeader.name(),
                   is(NAME));
        assertThat(lineHeader.nameShort(),
                   is(NAME_SHORT));
        assertThat(lineHeader.origin1(),
                   is(ORIGIN_1));
        assertThat(lineHeader.origin2(),
                   is(ORIGIN_2));
        assertThat(lineHeader.validTime(),
                   is(VALID_TIME));
    }
}
