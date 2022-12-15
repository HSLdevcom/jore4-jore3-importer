package fi.hsl.jore.importer.feature.batch.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public final class JdbcUtil {

    private JdbcUtil() {
    }

    private static void throwOnNull(final ResultSet rs,
                                    final String column) throws SQLException {
        if (rs.wasNull()) {
            final String msg = String.format("NPE when reading '%s' on row %d",
                                             column,
                                             rs.getRow());
            throw new RuntimeException(msg);
        }
    }

    public static int getIntOrThrow(final ResultSet rs,
                                    final String column) throws SQLException {
        final int val = rs.getInt(column);
        throwOnNull(rs, column);
        return val;
    }

    public static Optional<Integer> getOptionalInt(final ResultSet rs,
                                                   final String column) throws SQLException {
        final int val = rs.getInt(column);
        return rs.wasNull() ? Optional.empty() : Optional.of(val);
    }

    public static double getDoubleOrThrow(final ResultSet rs,
                                          final String column) throws SQLException {
        final double val = rs.getDouble(column);
        throwOnNull(rs, column);
        return val;
    }

    public static Optional<Double> getOptionalDouble(final ResultSet rs,
                                                     final String column) throws SQLException {
        final double val = rs.getDouble(column);
        return rs.wasNull() ? Optional.empty() : Optional.of(val);
    }

    public static String getStringOrThrow(final ResultSet rs,
                                          final String column) throws SQLException {
        final String val = rs.getString(column);
        throwOnNull(rs, column);
        return val.trim();
    }

    public static Optional<Short> getOptionalShort(final ResultSet rs,
                                                   final String column) throws SQLException {
        final String val = rs.getString(column);
        try {
            return StringUtils.isBlank(val) ? Optional.empty() : Optional.of(Short.parseShort(val.trim()));
        }
        catch (final NumberFormatException ex) {
            return Optional.empty();
        }
    }

    public static Optional<Long> getOptionalLong(final ResultSet rs,
                                                 final String column) throws SQLException {
        final String val = rs.getString(column);
        try {
            return StringUtils.isBlank(val) ? Optional.empty() : Optional.of(Long.parseLong(val.trim()));
        }
        catch (final NumberFormatException ex) {
            return Optional.empty();
        }
    }

    public static Optional<String> getOptionalString(final ResultSet rs,
                                                     final String column) throws SQLException {
        final String val = rs.getString(column);
        return rs.wasNull() ? Optional.empty() : Optional.of(val);
    }

    public static LocalDateTime getLocalDateTimeOrThrow(final ResultSet rs,
                                                        final String column) throws SQLException {
        final Timestamp val = rs.getTimestamp(column);
        throwOnNull(rs, column);
        return val.toLocalDateTime();
    }

    public static boolean getBooleanOrThrow(final ResultSet rs,
                                            final String column) throws SQLException {
        final boolean val = rs.getBoolean(column);
        throwOnNull(rs, column);
        return val;
    }
}
