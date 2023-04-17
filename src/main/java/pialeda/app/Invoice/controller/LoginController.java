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



@Controller
public class LoginController {

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

    @GetMapping("/login")
    public String login(Model model){
        String role = GlobalUser.getUserRole();
        String destination=null;
        if (role == null){
            model.addAttribute("login", new Login());
            return "admin/login";
        }else if(role.equals("marketing")){
            return destination = "redirect:/marketing-invoice";
        } else if (role.equals("vr-staff")) {
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals("admin")) {
            return destination = "redirect:/admin-dashboard";
        }
        return destination;
    }

    @PostMapping("/loginUser")
    public String loginUser(@ModelAttribute("login") Login login, Model model) {
        Boolean user = userService.loadUserByEmail(login.getEmail(),login.getPassword());

        if(user == true){
            String destination=null;
            User userDb = userService.loadUser(login.getEmail());
            String userEmail = GlobalUser.setUserEmail(userDb.getEmail());
            String userROle = GlobalUser.setUserRole(userDb.getRole());
            String userFname = GlobalUser.setUserFirstName(userDb.getFirstName());
            String userLname = GlobalUser.setUserLastName(userDb.getLastName());
            String fullName = userFname+" "+userLname;
            System.out.println(userEmail);
            System.out.println(userROle);
            System.out.println(fullName);

            if(userROle.equals("admin")){
//            redirectAttrs.addAttribute("fullName", fullName);
                return destination ="redirect:/admin-dashboard";
            } else if (userROle.equals("vr-staff")) {

                return destination = "redirect:/vr/user/invoices";
            } else if (userROle.equals("marketing")) {

                return destination ="redirect:/marketing-invoice";
            }
            return "destination";
//            return "login";
        }else{
            boolean hideSpan = true;
            model.addAttribute("hideSpan", hideSpan);
            return "admin/login";
        }
    }


    //Admin CONTROLLER
    @GetMapping("admin-dashboard")
    public String dashboard(Model model){
        String role = GlobalUser.getUserRole();
        String userFname = GlobalUser.getUserFirstName();
        String userLname = GlobalUser.getUserLastName();
        String fullName = userLname+", "+userFname;
        String destination=null;

        if(role == null){
            return destination = "redirect:/login";
        }
        else if (role.equals("marketing")) {
            return destination = "redirect:/marketing-invoice";
        }
        else if (role.equals("vr-staff")) {

            return destination = "redirect:/vr/user/invoices";
        }
        else if (role.equals("admin")) {
            model.addAttribute("userCount", userService.getUserCount());
            model.addAttribute("supplierCount", supplierService.getSupplierCount());
            model.addAttribute("clientCount", clientService.getClientCount());
            model.addAttribute("invoiceCount", invoiceService.getInvoiceCunt());
            model.addAttribute("fullName",fullName);
            return destination = "admin/dashboard";
        }
        return destination;
    }


    //VR CONTROLLER
    @GetMapping("vr/user/invoices")
    public String getAllPages(Model model, @RequestParam(name="client", required = false) String client,
                              @RequestParam(name="supplier", required = false) String supplier,
                              @RequestParam(name="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam(name="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              @RequestParam(name="page", required = false, defaultValue = "1") int currentPage) {
        String role = GlobalUser.getUserRole();
        String userFname = GlobalUser.getUserFirstName();
        String userLname = GlobalUser.getUserLastName();
        String fullName = userLname+", "+userFname;
        String destination=null;

        if(role == null){
            return "redirect:/login";
        } else if (role.equals("admin")) {
            return destination = "redirect:/admin-dashboard";
        }
        else if (role.equals("marketing")) {
            return destination = "redirect:/marketing-invoice";
        } else if (role.equals("vr-staff")) {

            if (client != null && supplier != null)
            {
                if (startDate != null && endDate != null)
                {
                    String message = null;
                    if (!DateUtils.isValidLocalDate(DateUtils.parseDateToString2(startDate)) || !DateUtils.isValidLocalDate(DateUtils.parseDateToString2(endDate)))
                    {
                        message = "Invalid start or end date format";
                        return vrController.invalidDateFormat(model, message);
                    }
                    else if (startDate.isAfter(endDate))
                    {
                        message = "The start date cannot be later than the finish date.";
                        return vrController.invalidDateFormat(model, message);
                    }
                    else
                    {
                        return vrController.filterClientSupplierSortByDateRange(model, client, supplier, startDate, endDate, currentPage, fullName);
                    }

                }
                else
                {
                    return vrController.filterSortClientSupplierPage(model, client, supplier, currentPage, fullName);
                }
            }
            else if (client != null && supplier == null)
            {
                if (startDate != null && endDate != null)
                {
                    String message = null;
                    if (!DateUtils.isValidLocalDate(DateUtils.parseDateToString2(startDate)) || !DateUtils.isValidLocalDate(DateUtils.parseDateToString2(endDate)))
                    {
                        message = "Invalid start or end date format";
                        return vrController.invalidDateFormat(model, message);
                    }
                    else if (startDate.isAfter(endDate))
                    {
                        message = "The start date cannot be later than the finish date.";
                        return vrController.invalidDateFormat(model, message);
                    }
                    else
                    {
                        return vrController.filterClientSortByDateRange(model, client, startDate, endDate, currentPage, fullName);
                    }

                }
                else
                {
                    return vrController.filterPageClient(model, client, currentPage, fullName);
                }
            }
            else if (client == null && supplier != null)
            {
                if (startDate != null && endDate != null)
                {
                    String message = null;
                    if (!DateUtils.isValidLocalDate(DateUtils.parseDateToString2(startDate)) || !DateUtils.isValidLocalDate(DateUtils.parseDateToString2(endDate)))
                    {
                        message = "Invalid start or end date format";
                        return vrController.invalidDateFormat(model, message);
                    }
                    else if (startDate.isAfter(endDate))
                    {
                        message = "The start date cannot be later than the finish date.";
                        return vrController.invalidDateFormat(model, message);
                    }
                    else
                    {
                        return vrController.filterSupplierSortByDateRange(model, supplier, startDate, endDate, currentPage, fullName);
                    }
                }
                else
                {
                    return vrController.filterPageSupplier(model, supplier, currentPage, fullName);
                }
            }
            else
            {
                return vrController.getDefaultPage(model, currentPage, fullName);
            }
        }
        return destination;
    }



    //MARKETING CONTROLLER
    @GetMapping("marketing-invoice")
    public String users(Model model){
        String role = GlobalUser.getUserRole();
        String userFname = GlobalUser.getUserFirstName();
        String userLname = GlobalUser.getUserLastName();
        String fullName = userLname+", "+userFname;
        String destination=null;

        if(role == null){
            return destination = "redirect:/login";
        }
        else if (role.equals("marketing")) {
            model.addAttribute("clientList", clientService.getAllClient());
            model.addAttribute("supplierList", supplierService.getAllSupplier());

            model.addAttribute("clientInfo", new ClientInfo());
            model.addAttribute("supplierInfo", new SupplierInfo());

            model.addAttribute("invoiceInfo", new InvoiceInfo());
            model.addAttribute("fullName",fullName);
            // get the current date
            LocalDate currentDate = LocalDate.now();

            // get the current day and month
            int currentDay = currentDate.getDayOfMonth();
            int  currentMonth = currentDate.getMonthValue();
            int generateInvoiceNumber = (int) invoiceService.getInvoiceCunt();
            Random random = new Random();
            if(generateInvoiceNumber == 0 || generateInvoiceNumber < 0){
                generateInvoiceNumber +=1;
            }else{
                generateInvoiceNumber +=1;
            }
            // generate two random two-digit numbers between 10 and 99
            int randomNum = random.nextInt(90) + 10;
            // format generateInvoiceNumber with a leading zero
            String generateInvNumberStr = String.format("%02d",generateInvoiceNumber);
            String resulInvNumStr = String.format("%d%d%s", currentMonth, currentDay, generateInvNumberStr);
            String invoiceNumber = resulInvNumStr + randomNum;
            int result = Integer.parseInt(invoiceNumber);
            String resultStr = String.valueOf(result);
            if(invoiceService.checkInvoiceNum(resultStr) == false){
                model.addAttribute("generateInvNum", result);
                String resultStr2 = String.valueOf(result);
                String poFormat = "PO"+resultStr2;
                model.addAttribute("generatePONum", poFormat);
                return destination = "marketing/invoice";
            }else{
                result +=1;
                model.addAttribute("generateInvNum", result);
                String resultStr2 = String.valueOf(result);
                String poFormat = "PO"+resultStr2;
                model.addAttribute("generatePONum", poFormat);
                return destination = "marketing/invoice";
            }
        }
        else if (role.equals("vr-staff")) {
            return destination = "redirect:/vr/user/invoices";
        }
        else if (role.equals("admin")) {
            return destination = "redirect:admin-dashboard";
        }
        return destination;
    }

    @GetMapping("logout")
    public String logout(){
        String role = GlobalUser.getUserRole();
        String newRole = GlobalUser.setUserRole(null);
        return "redirect:/login";
    }
}
