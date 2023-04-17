package pialeda.app.Invoice.config;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;


public class DateUtils {

    public static LocalDate getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        return currentDate;
    }
    public static String parseDateToString2(LocalDate localDateObject)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDateObject.format(formatter);
        return formattedDate;
    }
    public static LocalDate parseStringToDateLocalDate(String dateString)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDateObject = LocalDate.parse(dateString, formatter);
        return localDateObject;
    }
    public static String parseDateToString(LocalDate localDateObject)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = localDateObject.format(formatter);
        return formattedDate;
    }
    public static String parseDateToStringEnglish(LocalDate localDateObject)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        String formattedDate = localDateObject.format(formatter);
        return formattedDate;
    }
    public static boolean isValidLocalDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            System.out.println(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
