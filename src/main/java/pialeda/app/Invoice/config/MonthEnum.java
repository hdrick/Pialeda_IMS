package pialeda.app.Invoice.config;

public enum MonthEnum
{
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

    private final String monthValue;

    private MonthEnum(String monthValue) {
        this.monthValue = monthValue;
    }

    public String getMonthValue() {
        return monthValue;
    }

    public static String getMonthValueByName(String monthName) {
        for (MonthEnum month : MonthEnum.values()) {
            if (month.name().equalsIgnoreCase(monthName)
                    || month.name().substring(0, Math.min(3, month.name().length())).equalsIgnoreCase(monthName)) {
                return month.getMonthValue();
            }
        }
        return monthName;
    }
}
