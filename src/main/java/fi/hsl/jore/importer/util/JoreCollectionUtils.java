package fi.hsl.jore.importer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JoreCollectionUtils {
    public static <T> Stream<T> streamIterable(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T, U> Stream<U> mapWithIndex(final Iterable<T> iterable, final BiFunction<Integer, T, U> mapper) {
        final Iterator<T> iterator = iterable.iterator();
        AtomicInteger nextIndex = new AtomicInteger(0);
        return IntStream.generate(nextIndex::getAndIncrement)
                .takeWhile((_i) -> iterator.hasNext())
                .mapToObj(index -> mapper.apply(index, iterator.next()));
    }

    @SafeVarargs
    public static <T> List<T> concatToList(final Iterable<T>... iterables) {
        return Arrays.stream(iterables)
                .flatMap(iterable -> StreamSupport.stream(iterable.spliterator(), false))
                .toList();
    }

    public static <T> List<T> updateList(final List<T> list, final int index, final T newValue) {
        if (list.size() <= index) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + list.size());
        }
        final List<T> newList = new ArrayList<>(list);
        newList.set(index, newValue);

        return Collections.unmodifiableList(newList);
    }

    public static <T> T getFirst(final Iterable<T> iterable) {
        return iterable.iterator().next();
    }
}
