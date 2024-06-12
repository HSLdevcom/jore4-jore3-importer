package fi.hsl.jore.importer.config.jooq.converter.util;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import java.util.Optional;
import java.util.function.Function;

public final class RangeUtil {

    private static <T extends Comparable<? super T>> Range<T> between(
            final T from, final T to, final boolean startInclusive, final boolean endInclusive) {
        return Range.range(
                from,
                startInclusive ? BoundType.CLOSED : BoundType.OPEN,
                to,
                endInclusive ? BoundType.CLOSED : BoundType.OPEN);
    }

    private static <T extends Comparable<? super T>> Range<T> beginningFrom(
            final T from, final boolean startInclusive) {
        return startInclusive ? Range.atLeast(from) : Range.greaterThan(from);
    }

    private static <T extends Comparable<? super T>> Range<T> endingTo(final T to, final boolean endInclusive) {
        return endInclusive ? Range.atMost(to) : Range.lessThan(to);
    }

    public static <T extends Comparable<? super T>> Range<T> asRange(
            final Optional<T> maybeFrom,
            final Optional<T> maybeTo,
            final boolean startInclusive,
            final boolean endInclusive) {
        return maybeFrom
                .map(from -> maybeTo.map(to -> between(from, to, startInclusive, endInclusive))
                        .orElse(beginningFrom(from, startInclusive)))
                .orElse(maybeTo.map(to -> endingTo(to, endInclusive)).orElse(Range.all()));
    }

    public static <T extends Comparable<? super T>> String render(
            final Range<T> range, final Function<T, String> toString) {
        final StringBuilder sb = new StringBuilder(64);
        if (range.hasLowerBound()) {
            switch (range.lowerBoundType()) {
                case OPEN:
                    sb.append('(');
                    break;
                case CLOSED:
                    sb.append('[');
                    break;
            }
            sb.append(toString.apply(range.lowerEndpoint()));
        } else {
            sb.append('(');
        }
        sb.append(',');
        if (range.hasUpperBound()) {
            sb.append(toString.apply(range.upperEndpoint()));
            switch (range.upperBoundType()) {
                case OPEN:
                    sb.append(')');
                    break;
                case CLOSED:
                    sb.append(']');
                    break;
            }
        } else {
            sb.append(')');
        }
        return sb.toString();
    }

    private RangeUtil() {}
}
