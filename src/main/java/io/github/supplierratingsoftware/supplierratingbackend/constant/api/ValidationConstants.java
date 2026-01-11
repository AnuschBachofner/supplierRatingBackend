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

    // Enforces that the host is composed of valid labels: alphanumeric, optional interior hyphens, separated by dots.
    // Strictly forbids backslashes '\' anywhere in the URL and disallows spaces in the path to satisfy OpenBIS strictness.
    // Accepts empty string (for clearing/optional fields).
    public static final String URL_REGEX = "^$|^(http|https|ftp)://(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)(?:\\.(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?))*(?::[0-9]+)?(/[^\\s\\\\]*)?$";

    // Message adapted to reflect that spaces are indeed forbidden by the regex now.
    public static final String URL_MESSAGE = "URL must be a valid format (http/https/ftp), have a valid host structure, and must not contain backslashes or spaces";

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