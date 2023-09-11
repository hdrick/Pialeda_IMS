package pialeda.app.Invoice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import pialeda.app.Invoice.config.DateUtils;
import pialeda.app.Invoice.dto.*;
import pialeda.app.Invoice.model.User;
import pialeda.app.Invoice.service.ClientService;
import pialeda.app.Invoice.service.InvoiceService;
import pialeda.app.Invoice.service.SupplierService;
import pialeda.app.Invoice.service.UserService;
import pialeda.app.Invoice.dto.GlobalUser;
import java.time.LocalDate;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private VRController vrController;

    /**
     * Displays the login page.
     *
     * @param model The model to add attributes.
     * @return The view name for the login page.
     */
    @GetMapping("/login")
    public String login(Model model) {
        try {
            String role = GlobalUser.getUserRole();
            String destination = null;

            if (role == null) {
                model.addAttribute("login", new Login());
                return "admin/login";
            } else if (role.equals("marketing")) {
                logger.info("User with role 'marketing' logged in.");
                return destination = "redirect:/marketing-invoice";
            } else if (role.equals("vr-staff")) {
                logger.info("User with role 'vr-staff' logged in.");
                return destination = "redirect:/vr/user/invoices";
            } else if (role.equals("admin")) {
                logger.info("Admin user logged in.");
                return destination = "redirect:/admin-dashboard";
            }

            return destination;
        } catch (Exception e) {
            logger.error("Error occurred during login: {}", e.getMessage(), e);
            return "redirect:/login";
        }
    }

    /**
     * Handles the login process.
     *
     * @param login The login DTO.
     * @param model The model to add attributes.
     * @return The redirection destination after login.
     */
    @PostMapping("/loginUser")
    public String loginUser(@ModelAttribute("login") Login login, Model model) {
        try {
            Boolean user = userService.loadUserByEmail(login.getEmail(), login.getPassword());

            if (user != null && user) {
                String destination = null;
                User userDb = userService.loadUser(login.getEmail());
                String userEmail = GlobalUser.setUserEmail(userDb.getEmail());
                String userRole = GlobalUser.setUserRole(userDb.getRole());
                String userFname = GlobalUser.setUserFirstName(userDb.getFirstName());
                String userLname = GlobalUser.setUserLastName(userDb.getLastName());
                String fullName = userFname + " " + userLname;
                logger.info("User logged in: {}", userEmail);

                if ("admin".equals(userRole)) {
                    return destination = "redirect:/admin-dashboard";
                } else if ("vr-staff".equals(userRole)) {
                    return destination = "redirect:/vr/user/invoices";
                } else if ("marketing".equals(userRole)) {
                    return destination = "redirect:/marketing-invoice";
                }
                return "destination";
            } else {
                boolean hideSpan = true;
                model.addAttribute("hideSpan", hideSpan);
                return "admin/login";
            }
        } catch (Exception e) {
            logger.error("Error occurred during login: {}", e.getMessage(), e);
            boolean hideSpan = true;
            model.addAttribute("hideSpan", hideSpan);
            return "admin/login";
        }
    }

    /**
     * Logs the user out.
     *
     * @return The view name for the login page after logout.
     */
    @GetMapping("logout")
    public String logout() {
        try {
            String role = GlobalUser.getUserRole();
            String newRole = GlobalUser.setUserRole(null);
            logger.info("User logged out");
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Error occurred during logout: {}", e.getMessage(), e);
            return "redirect:/login";
        }
    }

    /**
     * Handles the GET request to display the admin dashboard or redirect to other pages based on the user's role.
     *
     * @param model The model to add attributes for the view.
     * @return The admin dashboard view or a redirect to other pages based on the user's role.
     */
    @GetMapping("admin-dashboard")
    public String dashboard(Model model) {
        String role = GlobalUser.getUserRole();
        String userFname = GlobalUser.getUserFirstName();
        String userLname = GlobalUser.getUserLastName();
        String fullName = userLname + ", " + userFname;
        String destination = null;

        if (role == null) {
            // If the user is not logged in, redirect to the login page
            logger.info("User is not logged in. Redirecting to the login page.");
            return destination = "redirect:/login";
        } else if (role.equals("marketing")) {
            // Redirect to the marketing invoice page if the user is in the marketing role
            return destination = "redirect:/marketing-invoice";
        } else if (role.equals("vr-staff")) {
            // Redirect to the VR user invoices page if the user is in the VR staff role
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals("admin")) {
            // Display the admin dashboard if the user is in the admin role
            model.addAttribute("userCount", userService.getUserCount());
            model.addAttribute("supplierCount", supplierService.getSupplierCount());
            model.addAttribute("clientCount", clientService.getClientCount());
            model.addAttribute("invoiceCount", invoiceService.getInvoiceCunt());
            model.addAttribute("fullName", fullName);
            logger.info("Displaying admin dashboard for user: {}", fullName);
            return destination = "admin/dashboard";
        }

        logger.warn("Invalid user role. Redirecting to the login page.");
        return destination;
    }



    /**
     * Handles the GET request to retrieve VR user invoices with optional filtering and pagination.
     *
     * @param model       The model to add attributes for the view.
     * @param client      The client filter (optional).
     * @param supplier    The supplier filter (optional).
     * @param startDate   The start date for filtering (optional).
     * @param endDate     The end date for filtering (optional).
     * @param currentPage The current page number for pagination (default value is 1).
     * @return The VR user invoices view with filtered and paginated results, or a redirect to another page based on the user's role.
     */
    @GetMapping("vr/user/invoices")
    public String getAllPages(Model model,
                              @RequestParam(name = "client", required = false) String client,
                              @RequestParam(name = "supplier", required = false) String supplier,
                              @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              @RequestParam(name = "page", required = false, defaultValue = "1") int currentPage) {
        String role = GlobalUser.getUserRole();
        String userFname = GlobalUser.getUserFirstName();
        String userLname = GlobalUser.getUserLastName();
        String fullName = userLname + ", " + userFname;
        String destination = null;

        if (role == null) {
            // If the user is not logged in, redirect to the login page
            logger.info("User is not logged in. Redirecting to the login page.");
            return "redirect:/login";
        } else if (role.equals("admin")) {
            // Redirect to the admin dashboard if the user is in the admin role
            return destination = "redirect:/admin-dashboard";
        } else if (role.equals("marketing")) {
            // Redirect to the marketing invoice page if the user is in the marketing role
            return destination = "redirect:/marketing-invoice";
        } else if (role.equals("vr-staff")) {
            // Handle VR staff role and apply filtering and pagination
            if (client != null && supplier != null) {
                if (startDate != null && endDate != null) {
                    String message = null;
                    if (!DateUtils.isValidLocalDate(DateUtils.parseDateToString2(startDate)) ||
                            !DateUtils.isValidLocalDate(DateUtils.parseDateToString2(endDate))) {
                        message = "Invalid start or end date format";
                        logger.warn("Invalid date format provided. Redirecting to the date format error page.");
                        return vrController.invalidDateFormat(model, message);
                    } else if (startDate.isAfter(endDate)) {
                        message = "The start date cannot be later than the finish date.";
                        logger.warn("Start date is later than the finish date. Redirecting to the date format error page.");
                        return vrController.invalidDateFormat(model, message);
                    } else {
                        logger.info("Filtering VR user invoices by client, supplier, and date range.");
                        return vrController.filterClientSupplierSortByDateRange(model, client, supplier, startDate, endDate, currentPage, fullName);
                    }
                } else {
                    logger.info("Filtering VR user invoices by client and supplier.");
                    return vrController.filterSortClientSupplierPage(model, client, supplier, currentPage, fullName);
                }
            } else if (client != null && supplier == null) {
                if (startDate != null && endDate != null) {
                    String message = null;
                    if (!DateUtils.isValidLocalDate(DateUtils.parseDateToString2(startDate)) ||
                            !DateUtils.isValidLocalDate(DateUtils.parseDateToString2(endDate))) {
                        message = "Invalid start or end date format";
                        logger.warn("Invalid date format provided. Redirecting to the date format error page.");
                        return vrController.invalidDateFormat(model, message);
                    } else if (startDate.isAfter(endDate)) {
                        message = "The start date cannot be later than the finish date.";
                        logger.warn("Start date is later than the finish date. Redirecting to the date format error page.");
                        return vrController.invalidDateFormat(model, message);
                    } else {
                        logger.info("Filtering VR user invoices by client and date range.");
                        return vrController.filterClientSortByDateRange(model, client, startDate, endDate, currentPage, fullName);
                    }
                } else {
                    logger.info("Filtering VR user invoices by client.");
                    return vrController.filterPageClient(model, client, currentPage, fullName);
                }
            } else if (client == null && supplier != null) {
                if (startDate != null && endDate != null) {
                    String message = null;
                    if (!DateUtils.isValidLocalDate(DateUtils.parseDateToString2(startDate)) ||
                            !DateUtils.isValidLocalDate(DateUtils.parseDateToString2(endDate))) {
                        message = "Invalid start or end date format";
                        logger.warn("Invalid date format provided. Redirecting to the date format error page.");
                        return vrController.invalidDateFormat(model, message);
                    } else if (startDate.isAfter(endDate)) {
                        message = "The start date cannot be later than the finish date.";
                        logger.warn("Start date is later than the finish date. Redirecting to the date format error page.");
                        return vrController.invalidDateFormat(model, message);
                    } else {
                        logger.info("Filtering VR user invoices by supplier and date range.");
                        return vrController.filterSupplierSortByDateRange(model, supplier, startDate, endDate, currentPage, fullName);
                    }
                } else {
                    logger.info("Filtering VR user invoices by supplier.");
                    return vrController.filterPageSupplier(model, supplier, currentPage, fullName);
                }
            } else {
                logger.info("Displaying default VR user invoices page.");
                return vrController.getDefaultPage(model, currentPage, fullName);
            }
        }

        logger.warn("Invalid user role. Redirecting to the login page.");
        return destination;
    }




    /**
     * Handles the GET request for the marketing invoice page.
     *
     * @param model The model to add attributes for the view.
     * @return The marketing invoice view if the user is in the marketing role, or a redirect to another page based on the user's role.
     */
    @GetMapping("marketing-invoice")
    public String marketingInvoice(Model model) {
        String role = GlobalUser.getUserRole();
        String userFname = GlobalUser.getUserFirstName();
        String userLname = GlobalUser.getUserLastName();
        String fullName = userLname + ", " + userFname;
        String destination = null;

        if (role == null) {
            // If the user is not logged in, redirect to the login page
            logger.info("User is not logged in. Redirecting to the login page.");
            return destination = "redirect:/login";
        } else if (role.equals("marketing")) {
            // If the user is in the marketing role, populate model attributes and display the marketing invoice page
            model.addAttribute("clientList", clientService.getAllClient());
            model.addAttribute("supplierList", supplierService.getAllSupplier());

            model.addAttribute("clientInfo", new ClientInfo());
            model.addAttribute("supplierInfo", new SupplierInfo());

            model.addAttribute("invoiceInfo", new InvoiceInfo());
            model.addAttribute("fullName", fullName);

            // Generate invoice number
            LocalDate currentDate = LocalDate.now();
            int currentDay = currentDate.getDayOfMonth();
            int currentMonth = currentDate.getMonthValue();
            int generateInvoiceNumber = (int) invoiceService.getInvoiceCunt();
            Random random = new Random();

            if (generateInvoiceNumber == 0 || generateInvoiceNumber < 0) {
                generateInvoiceNumber += 1;
            } else {
                generateInvoiceNumber += 1;
            }

            // Generate two random two-digit numbers between 10 and 99
            int randomNum = random.nextInt(90) + 10;
            // Format generateInvoiceNumber with a leading zero
            String generateInvNumberStr = String.format("%02d", generateInvoiceNumber);
            String resultInvNumStr = String.format("%d%d%s", currentMonth, currentDay, generateInvNumberStr);
            String invoiceNumber = resultInvNumStr + randomNum;
            int result = Integer.parseInt(invoiceNumber);
            String resultStr = String.valueOf(result);

            if (invoiceService.checkInvoiceNum(resultStr) == false) {
                model.addAttribute("generateInvNum", result);
                String resultStr2 = String.valueOf(result);
                String poFormat = "PO" + resultStr2;
                model.addAttribute("generatePONum", poFormat);

                logger.info("Marketing invoice page displayed.");
                return destination = "marketing/invoice";
            } else {
                result += 1;
                model.addAttribute("generateInvNum", result);
                String resultStr2 = String.valueOf(result);
                String poFormat = "PO" + resultStr2;
                model.addAttribute("generatePONum", poFormat);

                logger.info("Marketing invoice page displayed.");
                return destination = "marketing/invoice";
            }
        } else if (role.equals("vr-staff")) {
            // Redirect to VR staff page if the user is in the VR staff role
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals("admin")) {
            // Redirect to the admin dashboard if the user is in the admin role
            return destination = "redirect:admin-dashboard";
        }

        logger.warn("Invalid user role. Redirecting to the login page.");
        return destination;
    }


}
