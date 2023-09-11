package pialeda.app.Invoice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An enumeration representing months with corresponding month values.
 */
public enum MonthEnum {
    JANUARY("01"),
    FEBRUARY("02"),
    MARCH("03"),
    APRIL("04"),
    MAY("05"),
    JUNE("06"),
    JULY("07"),
    AUGUST("08"),
    SEPTEMBER("09"),
    OCTOBER("10"),
    NOVEMBER("11"),
    DECEMBER("12");

    private static final Logger logger = LoggerFactory.getLogger(MonthEnum.class);

    private final String monthValue;

    private MonthEnum(String monthValue) {
        this.monthValue = monthValue;
    }

    /**
     * Get the month's numeric value.
     *
     * @return The month's numeric value (e.g., "01" for January).
     */
    public String getMonthValue() {
        return monthValue;
    }

    /**
     * Get the numeric value of a month by its name (case-insensitive).
     * If the input does not match any month name, it returns the input as is.
     *
     * @param monthName The name of the month (full or abbreviated).
     * @return The corresponding numeric value (e.g., "01" for January) or the input string if not recognized.
     */
    public static String getMonthValueByName(String monthName) {
        for (MonthEnum month : MonthEnum.values()) {
            if (month.name().equalsIgnoreCase(monthName)
                    || month.name().substring(0, Math.min(3, month.name().length())).equalsIgnoreCase(monthName)) {
                return month.getMonthValue();
            }
        }
        logger.warn("Month name not recognized: {}", monthName);
        return monthName;
    }
}