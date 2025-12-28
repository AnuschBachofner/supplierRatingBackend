package io.github.supplierratingsoftware.supplierratingbackend.constant.openbis;

/**
 * Contains business schema constants specific to the Supplier Rating configuration in OpenBIS.
 * Includes property codes and identifiers defined in the OpenBIS instance.
 */
public final class OpenBisSchemaConstants {

    // Property Codes
    // - Supplier
    public static final String NAME_SUPPLIER_PROPERTY = "NAME";
    public static final String CUSTOMER_NUMBER_SUPPLIER_PROPERTY = "KUNDENNUMMER";
    public static final String ADDITION_SUPPLIER_PROPERTY = "LIEFERANTEN_ZUSATZ";
    public static final String STREET_SUPPLIER_PROPERTY = "LIEFERANTEN_STRASSE";
    public static final String PO_BOX_SUPPLIER_PROPERTY = "LIEFERANTEN_POSTFACH";
    public static final String COUNTRY_SUPPLIER_PROPERTY = "LIEFERANTEN_LAND";
    public static final String ZIP_CODE_SUPPLIER_PROPERTY = "LIEFERANTEN_POSTLEITZAHL";
    public static final String CITY_SUPPLIER_PROPERTY = "LIEFERANTEN_ORT";
    public static final String WEBSITE_SUPPLIER_PROPERTY = "LIEFERANTEN_WEBLINK";
    public static final String EMAIL_SUPPLIER_PROPERTY = "LIEFERANTEN_EMAIL";
    public static final String PHONE_NUMBER_SUPPLIER_PROPERTY = "LIEFERANTEN_TELEFON";
    public static final String VAT_ID_SUPPLIER_PROPERTY = "MWST";
    public static final String CONDITIONS_SUPPLIER_PROPERTY = "KONDITIONEN";
    public static final String CUSTOMER_INFO_SUPPLIER_PROPERTY = "KUNDENINFORMATION";

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private OpenBisSchemaConstants() {
    }
}
