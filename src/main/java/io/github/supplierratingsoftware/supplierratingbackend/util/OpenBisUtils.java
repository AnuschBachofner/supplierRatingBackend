package io.github.supplierratingsoftware.supplierratingbackend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class OpenBisUtils {

    private static final Logger log = LoggerFactory.getLogger(OpenBisUtils.class);

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private OpenBisUtils() {
    }

    /**
     * Safely parses a string property to a Double.
     *
     * @param value        The string value to parse.
     * @param contextKey   The key or property name (for logging context).
     * @param contextValue The sample code or ID (for logging context).
     * @return The parsed Double, or null if the value is null/blank or invalid.
     */
    public static Double parseDoubleOrNull(String value, String contextKey, String contextValue) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            log.warn("Data Quality Warning: Failed to parse property '{}' with value '{}' for sample '{}'. Returning null.",
                    contextKey, value, contextValue);
            return null;
        }
    }

    /**
     * Helper method to build an identifier path from parts, joining them with '/'.
     * Example: buildIdentifier("SPACE", "PROJ", "EXP") -> "/SPACE/PROJ/EXP"
     * Null or blank parts are ignored.
     *
     * @param parts The parts to join.
     * @return The joined identifier string.
     */
    public static String buildIdentifier(String... parts) {
        if (parts == null || parts.length == 0) {
            return "";
        }
        List<String> validParts = Arrays.stream(parts).filter(part -> part != null && !part.isBlank()).toList();
        if (validParts.isEmpty()) {return "";}
        return "/" + String.join("/", validParts);
    }

    /**
     * Helper method to put a key-value pair into a map only if the value is not null and not blank.
     *
     * @param map   The map to put into.
     * @param key   The key to put.
     * @param value The value to put.
     */
    public static void putIfNotNull(Map<String, String> map, String key, String value) {
        if (map != null && value != null && !value.isBlank()) {
            map.put(key, value);
        }
    }
}