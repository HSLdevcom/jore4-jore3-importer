package fi.hsl.jore.importer.feature.jore3.field;

import org.immutables.value.Value;

import java.util.Optional;
import java.util.regex.Pattern;

import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.TESTING;
import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.TRAIN_OR_METRO;
import static fi.hsl.jore.importer.feature.jore3.field.LegacyPublicTransportDestination.UNKNOWN;

/**
 * Line and route identifiers are strings in the form ABBBCD, where
 * <ul>
 *     <li>A: Public transport destination
 *     <ul>
 *         <li>Describes the type of the line
 *         <li>Deprecated in favor of {@link fi.hsl.jore.importer.feature.jore3.enumerated.PublicTransportDestination}
 *         -enum in the line/route tables
 *     </ul>
 *     <li>B: Line number
 *     <ul>
 *         <li>Typically a number between 1-999 (for buses)
 *         <li>Interpreted differently for train or metro lines
 *     </ul>
 *     <li>C: First variant field (optional)
 *     <li>D: Second variant field (optional)
 * </ul>
 * <p>
 * The actual line number shown to the passenger is constructed from the B, C and D fields.
 * This passenger-facing line number is <i>not</i> unique!
 *
 * <p>
 * See: <a href="https://github.com/HSLdevcom/jore4/blob/main/wiki/jore3_route_ids.md">this</a>.
 *
 * <p>
 * This class uses the {@link org.immutables.value.Value.Lazy Lazy} annotation to evaluate (and cache) the individual
 * fields only when requested instead of eagerly evaluating them. This improves performance in situations where we
 * are dealing with large amounts of identifiers but only care about the {@link EncodedIdentifier#originalValue() original value}.
 */
public abstract class EncodedIdentifier {

    private static final Pattern LEADING_ZEROS = Pattern.compile("^0*");

    public abstract String originalValue();

    protected String value() {
        // Pad right upto 6 characters
        // "123" -> "123   "
        return String.format("%-6s", originalValue());
    }

    // The raw fields
    private Character fieldA() {
        return value().charAt(0);
    }

    private String fieldB() {
        return value().substring(1, 4);
    }

    private Character fieldC() {
        return value().charAt(4);
    }

    private Character fieldD() {
        return value().charAt(5);
    }

    // Aliases for the raw fields

    @Value.Lazy
    public LegacyPublicTransportDestination destination() {
        return LegacyPublicTransportDestination.of(fieldA())
                                               .orElse(UNKNOWN);
    }

    private String lineNumber() {
        return fieldB();
    }

    private Optional<Character> variantA() {
        return ' ' == fieldC() ?
                Optional.empty() :
                Optional.of(fieldC());
    }

    private Optional<Character> variantB() {
        return ' ' == fieldD() ?
                Optional.empty() :
                Optional.of(fieldD());
    }

    // Other derived fields

    /**
     * The line number field, but all leading zeros are dropped
     *
     * @return The line number without any leading zeros
     */
    private String sanitizedLineNumber() {
        return LEADING_ZEROS.matcher(lineNumber())
                            .replaceFirst("");
    }

    /**
     * For train, second and third are zeroes and fourth is 1 or 2.
     * Two is for trains operating west from Pasila and one for ones operating north/east.
     *
     * @return True, if this identifier matches a train
     */
    private boolean train() {
        return TRAIN_OR_METRO == destination() &&
               ("001".equals(lineNumber()) || "002".equals(lineNumber()));
    }

    /**
     * For metro, second is one and third and fourth are the displayed line number,
     * e.g. "31M2" -> line number is "1M2" -> displayed identifier is "M2".
     *
     * @return True, if this identifier matches a metro
     */
    private boolean metro() {
        return TRAIN_OR_METRO == destination() &&
               !train() &&
               (lineNumber().charAt(0) == '1');
    }

    /**
     * Check if this identifier represents a hidden variant. Hidden variants are used for routes which
     * differ only very slightly from the main route and we don't need to indicate to the passenger that
     * this route is a variant.
     */
    private boolean hiddenVariantB() {
        return variantB().map(Character::isDigit)
                         .orElse(false);
    }

    // Public API

    /**
     * Check if this identifier represents the main route or a variant of the main route
     *
     * @return True, if this is the main route of a line. False, if this is a variant.
     */
    @Value.Lazy
    public boolean primary() {
        if (train()) {
            // For trains variant A is the line letter, e.g. "train F"
            return variantB().isEmpty();
        }
        // No variant A defined
        return variantA().isEmpty()
               // and variant B is either empty or not-hidden
               // (e.g. it's 'X' to indicate a bus replacing a tram)
               && !hiddenVariantB();
    }

    /**
     * Some lines/routes are not (or never were) actually operational. Most of the time they are identified
     * with "x000", for example "1000", "1000 X". These lines/routes don't always produce sane
     * visible lane numbers for the passenger, e.g. "1000" -> "", but that's not a problem as these are not
     * operational.
     *
     * @return True, if this line is most likely only used for testing/planning.
     */
    @Value.Lazy
    public boolean test() {
        return TESTING == destination() ||
               sanitizedLineNumber().isEmpty();
    }


    /**
     * The actual line/route number shown to the passengers.
     *
     * @return The visible line/route number
     */
    @Value.Lazy
    public String displayId() {
        if (train()) {
            // Trains are identified as "A train", "C train" etc using the variant
            return variantA()
                    .map(Object::toString)
                    .orElse("");
        }
        final StringBuilder result = new StringBuilder(5);

        if (metro()) {
            // Drop the '1' prefix from the line number for metros, e.g. "1M2" -> "M2"
            result.append(lineNumber().substring(1));
        } else {
            result.append(sanitizedLineNumber());
        }

        variantA().ifPresent(result::append);

        // Only append variant B if a) it's present and b) it's not hidden
        if (!hiddenVariantB()) {
            variantB().ifPresent(result::append);
        }

        return result.toString();
    }
}
