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

    // - Order
    public static final String NAME_ORDER_PROPERTY = "NAME";
    public static final String MAIN_CATEGORY_ORDER_PROPERTY = "BESTELLUNG_HK";
    public static final String SUB_CATEGORY_ORDER_PROPERTY = "BESTELLUNG_UK";
    public static final String DESCRIPTION_ORDER_PROPERTY = "BEZEICHNUNG";
    public static final String FREQUENCY_ORDER_PROPERTY = "RYTHMUS"; // Mapped to misspelled "RYTHMUS" [sic!] in openBIS
    public static final String CONTACT_NAME_ORDER_PROPERTY = "ANSPRECHPERSON";
    public static final String CONTACT_EMAIL_ORDER_PROPERTY = "ANSPRECHPERSON_EMAIL";
    public static final String CONTACT_PHONE_ORDER_PROPERTY = "ANSPRECHPERSON_TELEFON";
    public static final String ORDER_REASON_ORDER_PROPERTY = "BESCHAFFUNGSGRUND";
    public static final String ORDER_METHOD_ORDER_PROPERTY = "BESTELLART";
    public static final String PURCHASER_ORDER_PROPERTY = "BESTELLER";
    public static final String ORDER_DATE_ORDER_PROPERTY = "BESTELLDATUM";
    public static final String DELIVERY_DATE_ORDER_PROPERTY = "LIEFERDATUM";
    public static final String ORDER_COMMENT_ORDER_PROPERTY = "KOMMENTAR";

    // - Rating Status of Orders
    public static final String RATING_STATUS_RATED_ORDER_PROPERTY = "RATED";
    public static final String RATING_STATUS_PENDING_ORDER_PROPERTY = "PENDING";

    // - Rating
    public static final String QUALITY_RATING_PROPERTY = "QUALITAET";
    public static final String QUALITY_REASON_RATING_PROPERTY = "GRUND_QUALITAET";
    public static final String COST_RATING_PROPERTY = "KOSTEN";
    public static final String COST_REASON_RATING_PROPERTY = "KOSTEN_BEGRUENDUNG";
    public static final String RELIABILITY_RATING_PROPERTY = "EINHALTUNG_TERMINE";
    public static final String RELIABILITY_REASON_RATING_PROPERTY = "EINHALTUNG_TERMINE_GRUND";
    public static final String AVAILABILITY_RATING_PROPERTY = "VERFUEGBARKEIT";
    public static final String AVAILABILITY_REASON_RATING_PROPERTY = "VERFUEGBARKEIT_BEGRUENDUNG";
    public static final String TOTAL_SCORE_RATING_PROPERTY = "GESAMTBEWERTUNG";
    public static final String RATING_COMMENT_RATING_PROPERTY = "KOMMENTAR";

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private OpenBisSchemaConstants() {
    }
}
