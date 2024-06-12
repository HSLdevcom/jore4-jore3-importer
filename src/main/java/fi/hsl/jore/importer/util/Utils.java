package fi.hsl.jore.importer.util;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Utils {
    public static <T> Stream<T> streamIterable(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
