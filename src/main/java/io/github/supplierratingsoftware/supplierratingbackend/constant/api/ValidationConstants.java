package io.github.supplierratingsoftware.supplierratingbackend.constant.api;

/**
 * Constants for validation purposes.
 * It provides common regular expressions and messages for validation.
 */
public final class ValidationConstants {

    /**
     * Private constructor to prevent instantiation.
     */
    private ValidationConstants() {
    }

    // --- Common ---
    // Checks for protocol and at least one character after `://`
    // Accepts empty string (for clearing)
    public static final String URL_REGEX = "^$|^(http|https|ftp)://[^\\s]+$";
    public static final String URL_MESSAGE = "URL must be in format http(s)://<domain> or ftp://<domain> and contain no spaces";

    // Checks for a valid date format: YYYY-MM-DD
    // Accepts empty string (for clearing)
    public static final String DATE_REGEX = "^$|^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
    public static final String DATE_MESSAGE = "Date must be in format YYYY-MM-DD with valid month (01-12) and day (01-31)";

    // --- Rating ---
    // Score range
    public static final int RATING_SCORE_MIN = 1;
    public static final String RATING_SCORE_MIN_MESSAGE = "Rating score must be at least " + RATING_SCORE_MIN;
    public static final int RATING_SCORE_MAX = 5;
    public static final String RATING_SCORE_MAX_MESSAGE = "Rating score must be at most " + RATING_SCORE_MAX;
}