package io.github.supplierratingsoftware.supplierratingbackend.constant.api;

public final class ValidationConstants {

    /**
     * Private constructor to prevent instantiation.
     */
    private ValidationConstants() {
    }

    // --- Common ---
    // Checks for protocol and at least one character after `://`
    public static final String URL_REGEX = "^(http|https|ftp)://[^\\s]+$";
    public static final String URL_MESSAGE = "URL must be in format http(s)://<domain> or ftp://<domain> and contain no spaces";

    // Checks for a valid date format: YYYY-MM-DD
    public static final String DATE_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
    public static final String DATE_MESSAGE = "Date must be in format YYYY-MM-DD with valid month (01-12) and day (01-31)";

    // --- Supplier ---
    // Allowed country codes
    public static final String COUNTRY_REGEX = "^(CH|D|F|FL|NL)$";
    public static final String COUNTRY_MESSAGE = "Country must be one of the defined values from OpenBIS vocabulary: CH, D, F, FL, NL";

    // --- Order ---
    // Main Categories for orders, based on OpenBIS Vocabulary
    public static final String MAIN_CATEGORY_REGEX = "^(Beschaffung|Dienstleistung)$";
    public static final String MAIN_CATEGORY_MESSAGE = "The Main category must be one of the defined values from OpenBIS vocabulary: Beschaffung, Dienstleistung";

    // Sub Categories for orders, based on OpenBIS Vocabulary
    public static final String SUB_CATEGORY_REGEX = "^(Beratung|Dienstleistung|Gerät/Werkzeug|Maschine|Messgeräte|Messmittel|PC Hardware|PC Software|Prüfmaschine)$";
    public static final String SUB_CATEGORY_MESSAGE = "The Subcategory must be one of the defined values from OpenBIS vocabulary: Beratung, Dienstleistung, Gerät/Werkzeug, Maschine, Messgeräte, Messmittel, PC Hardware, PC Software, Prüfmaschine";

    // --- Rating ---
    // Score range
    public static final int RATING_SCORE_MIN = 1;
    public static final String RATING_SCORE_MIN_MESSAGE = "Rating score must be at least " + RATING_SCORE_MIN;
    public static final int RATING_SCORE_MAX = 5;
    public static final String RATING_SCORE_MAX_MESSAGE = "Rating score must be at most " + RATING_SCORE_MAX;
}