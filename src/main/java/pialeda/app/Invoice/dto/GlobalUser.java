package pialeda.app.Invoice.dto;

public class GlobalUser {
    private static String userEmail;
    private static String userRole;
    private static String userFirstName;
    private static String userLastName;

    public static String getUserFirstName() {
        return userFirstName;
    }

    public static String setUserFirstName(String userFirstName) {
        GlobalUser.userFirstName = userFirstName;
        return userFirstName;
    }

    public static String getUserLastName() {
        return userLastName;
    }

    public static String setUserLastName(String userLastName) {
        GlobalUser.userLastName = userLastName;
        return userLastName;
    }

    public static String getUserRole() {
        return userRole;
    }

    public static String setUserRole(String userRole) {
        GlobalUser.userRole = userRole;
        return userRole;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static String setUserEmail(String userEmail) {
        GlobalUser.userEmail = userEmail;
        return userEmail;
    }

    public String getFullName(){
        return userFirstName+" "+userLastName;
    }
}
