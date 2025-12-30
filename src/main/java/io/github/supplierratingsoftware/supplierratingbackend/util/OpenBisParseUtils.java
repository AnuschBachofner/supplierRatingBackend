package io.github.supplierratingsoftware.supplierratingbackend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class OpenBisParseUtils {

    private static final Logger log = LoggerFactory.getLogger(OpenBisParseUtils.class);

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private OpenBisParseUtils() {
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
}