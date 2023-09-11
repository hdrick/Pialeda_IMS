package pialeda.app.Invoice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Utility class for date-related operations.
 */
public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     * Get the current date.
     *
     * @return The current date.
     */
    public static LocalDate getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        return currentDate;
    }

    /**
     * Parse a {@link LocalDate} object to a string in "dd-MM-yyyy" format.
     *
     * @param localDateObject The {@link LocalDate} object to format.
     * @return The formatted date string.
     */
    public static String parseDateToString2(LocalDate localDateObject) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDateObject.format(formatter);
        return formattedDate;
    }

    /**
     * Parse a date string in "dd-MM-yyyy" format to a {@link LocalDate} object.
     *
     * @param dateString The date string to parse.
     * @return The parsed {@link LocalDate} object.
     */
    public static LocalDate parseStringToDateLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDateObject = LocalDate.parse(dateString, formatter);
        return localDateObject;
    }

    /**
     * Parse a {@link LocalDate} object to a string in "yyyy-MM-dd" format.
     *
     * @param localDateObject The {@link LocalDate} object to format.
     * @return The formatted date string.
     */
    public static String parseDateToString(LocalDate localDateObject) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = localDateObject.format(formatter);
        return formattedDate;
    }

    /**
     * Parse a {@link LocalDate} object to a string in "MMMM d, yyyy" format (English locale).
     *
     * @param localDateObject The {@link LocalDate} object to format.
     * @return The formatted date string.
     */
    public static String parseDateToStringEnglish(LocalDate localDateObject) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        String formattedDate = localDateObject.format(formatter);
        return formattedDate;
    }

    /**
     * Check if a given string is a valid date in "dd-MM-yyyy" format.
     *
     * @param dateStr The date string to validate.
     * @return {@code true} if the date string is valid, {@code false} otherwise.
     */
    public static boolean isValidLocalDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            logger.debug("Valid date: {}", dateStr);
            return true;
        } catch (DateTimeParseException e) {
            logger.debug("Invalid date: {}", dateStr);
            return false;
        }
    }
}
