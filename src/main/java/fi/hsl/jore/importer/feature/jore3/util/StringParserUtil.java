package fi.hsl.jore.importer.feature.jore3.util;

/** Contains utility methods which help us to parse different values from {@link String} objects. */
public final class StringParserUtil {

    /**
     * Trims the source value and ensures that the source is not null and blank.
     *
     * @param alias The alias of the returned value.
     * @param source The source value.
     * @return A value that doesn't contain any white space.
     * @throws NullPointerException if the source value is {@code null}.
     * @throws IllegalArgumentException if the source value is blank.
     */
    public static String parseRequiredValue(final String alias, final String source) {
        if (source == null) {
            throw new NullPointerException(String.format("%s cannot be null", alias));
        }

        final String value = source.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    String.format("%s cannot be empty or contain only white space", alias));
        }

        return value;
    }

    /**
     * Parses an {@code long} value from the source value.
     *
     * @param alias The alias of the returned value.
     * @param source The source value.
     * @return The parsed int value.
     * @throws NullPointerException if the source value is {@code null}.
     * @throws IllegalArgumentException if the source value is blank or it cannot be parsed.
     */
    public static long parseRequiredLong(final String alias, final String source) {
        final String value = parseRequiredValue(alias, source);
        try {
            return Long.parseLong(value);
        } catch (final NumberFormatException ex) {
            throw new IllegalArgumentException(
                    String.format(
                            "Cannot parse long value for field: %s from source value: %s",
                            alias, value));
        }
    }

    /**
     * Parses an {@code int} value from the source value.
     *
     * @param alias The alias of the returned value.
     * @param source The source value.
     * @return The parsed int value.
     * @throws NullPointerException if the source value is {@code null}.
     * @throws IllegalArgumentException if the source value is blank or it cannot be parsed.
     */
    public static int parseRequiredInteger(final String alias, final String source) {
        final String value = parseRequiredValue(alias, source);
        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException ex) {
            throw new IllegalArgumentException(
                    String.format(
                            "Cannot parse integer value for field: %s from source value: %s",
                            alias, value));
        }
    }
}
