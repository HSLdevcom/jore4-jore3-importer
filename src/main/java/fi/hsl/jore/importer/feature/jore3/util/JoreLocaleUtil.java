package fi.hsl.jore.importer.feature.jore3.util;

import fi.hsl.jore.importer.feature.common.dto.field.MultilingualString;
import java.util.Locale;
import java.util.Map;

public final class JoreLocaleUtil {

    public static final Locale FINNISH = Locale.forLanguageTag("fi-FI");

    public static final Locale SWEDISH = Locale.forLanguageTag("sv-SE");

    private JoreLocaleUtil() {}

    /**
     * Creates a new multilingual string.
     *
     * @param finnish The Finnish translation.
     * @param swedish The Swedish translation.
     * @return The created multilingual string.
     */
    public static MultilingualString createMultilingualString(
            final String finnish, final String swedish) {
        return MultilingualString.of(
                Map.of(FINNISH.toString(), finnish, SWEDISH.toString(), swedish));
    }

    /**
     * Finds the requested translation.
     *
     * @param multilingualString All available translations.
     * @param locale The locale of the requested translation.
     * @return The found translation. If no translation is found, this method returns an empty
     *     string.
     */
    public static String getI18nString(
            final MultilingualString multilingualString, final Locale locale) {
        return multilingualString.values().getOrElse(locale.toString(), "");
    }
}
