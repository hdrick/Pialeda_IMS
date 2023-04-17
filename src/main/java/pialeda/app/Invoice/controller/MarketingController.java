package pialeda.app.Invoice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pialeda.app.Invoice.dto.*;
import pialeda.app.Invoice.model.*;
import pialeda.app.Invoice.service.ClientService;
import pialeda.app.Invoice.service.CollectionService;
import pialeda.app.Invoice.service.OfficialRecptService;
import pialeda.app.Invoice.service.SupplierService;
import pialeda.app.Invoice.service.InvoiceService;
import pialeda.app.Invoice.dto.GlobalUser;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class MarketingController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private OfficialRecptService officialRecptService;
    @Autowired
    private CollectionService collectionService;

    @GetMapping("marketing-view/invoices")
    public String viewInvoices(Model model){
        return "marketing/invoice-view";
    }


    @PostMapping("/createInvoice")
    public String createInvoice(@ModelAttribute("invoiceInfo") InvoiceInfo invoiceInfo,
                                @RequestParam("invDate") String date,
                                @RequestParam("qty") List<String> qtyList,
                                @RequestParam("unit") List<String> unitList,
                                @RequestParam("article") List<String> articlesList,
                                @RequestParam("unitPrice") List<String> unitPriceList,
                                @RequestParam("amount") List<String> amountList,
                                RedirectAttributes redirectAttributes
                                 ){
        double sumOfGrandTotal = invoiceService.getSuppTotalLimit(invoiceInfo.getSupplierName());
        double supplierLimit = supplierService.findLimitByName(invoiceInfo.getSupplierName());
        double remainingLimit = supplierLimit - sumOfGrandTotal;

        if(invoiceInfo.getGrandTotal() <= remainingLimit){
            invoiceService.createInvoice(invoiceInfo, date, qtyList, unitList,articlesList,unitPriceList,amountList);
            boolean hideDivSuccess = true;
            redirectAttributes.addFlashAttribute("hideDivSuccess", hideDivSuccess);
            return "redirect:/marketing-invoice";
        }else{
            boolean hideDivError = true;
            redirectAttributes.addFlashAttribute("hideDivError", hideDivError);
            return "redirect:/marketing-invoice";
        }
    }

    @PostMapping("/createClient")
    public String createClient(@ModelAttribute("clientInfo") ClientInfo clientInfo, Model model) {
        clientService.createClient(clientInfo);
        return "redirect:/marketing-invoice";
    }

    @PostMapping("/createSupplier")
    public String createSupplier(@ModelAttribute("supplierInfo") SupplierInfo supplierInfo, Model model) {
        supplierService.createSupplier(supplierInfo);
        return "redirect:/marketing-invoice";
    }

    @PostMapping("/createOfficialReceipt")
    public String createOfficialReceipt(@RequestParam("orNumber") int orNumber,
                                        @RequestParam("totalSales") String totalSales,
                                        @RequestParam("addVat") String addVat,
                                        @RequestParam("lwTax") String lwTax,
                                        @RequestParam("amtDue") String amtDue,
                                        @RequestParam("ewt") String ewt,
                                        @RequestParam("total") String total,
                                        @RequestParam("cash") String cash,
                                        @RequestParam("chckNo") String chckNo,
                                        @RequestParam("orAmount") String orAmount,
                                        @RequestParam("cashierName") String cashierName,
                                        @RequestParam Map<String, String> requestParams,
                                        @ModelAttribute("officialReceiptInfo") OfficialReceiptInfo officialReceiptInfo,
                                        RedirectAttributes redirectAttributes
        ) {
      officialRecptService.createOR(orNumber, totalSales, addVat, lwTax, amtDue, ewt, total, cash, chckNo, orAmount, cashierName, requestParams, officialReceiptInfo);
      boolean hideDivSuccessOR = true;
      redirectAttributes.addFlashAttribute("hideDivSuccessOR", hideDivSuccessOR);
      return "redirect:/marketing-officialreceipt";
    }



    @PostMapping("/createCollectionReceipt")
    public String createCollectionReceipt(@RequestParam("orNumber") int orNumber,
                                        @RequestParam("amtDue") String amtDue,
                                        @RequestParam("ewt") String ewt,
                                        @RequestParam("total") String total,
                                        @RequestParam("cash") String cash,
                                        @RequestParam("chckNo") String chckNo,
                                        @RequestParam("crAmount") String crAmount,
                                        @RequestParam("cashierName") String cashierName,
                                        @RequestParam Map<String, String> requestParams,
                                        @ModelAttribute("collectionReceiptInfo") CollectionReceiptInfo collectionReceiptInfo,
                                        RedirectAttributes redirectAttributes
        ) {
      collectionService.createCR(orNumber, amtDue, ewt, total, cash, chckNo, crAmount, cashierName, requestParams, collectionReceiptInfo);
      boolean hideDivSuccessOR = true;
      redirectAttributes.addFlashAttribute("hideDivSuccessOR", hideDivSuccessOR);
      return "redirect:/marketing-collectionreceipt";
    }
      



    @GetMapping("/getClientInfo")
    @ResponseBody
    public Client getClientInfo(@RequestParam("name") String name) {
        Client client = clientService.findByName(name);
        Client clientInfo = new Client(client.getName(), client.getAddress(), 
                                       client.getCityAddress(), client.getTin(), 
                                       client.getAgent(),client.getBusStyle(),
                                       client.getTerms());
        return clientInfo;
    }

    @GetMapping("/getSupplierInfo")
    @ResponseBody
    public Supplier getSupplierInfo(@RequestParam("name") String name) {
        Supplier supplier = supplierService.findByName(name);
        Supplier supplierInfo = new Supplier(supplier.getName(), supplier.getAddress(), supplier.getCityAddress(), supplier.getTin());
        return supplierInfo;
    }



    @GetMapping("/invoice-view")
    public String invoiceView(Model model){
        String role = GlobalUser.getUserRole();
        String destination=null;
        if(role == null){
            return destination = "redirect:/login";
        } else if (role.equals("vr-staff")) {
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals(("marketing"))) {
            return destination = findPaginated(0, model);
        } else if (role.equals("admin")) {
            return destination = "redirect:/admin-dashboard";
        }
        return destination;

    }

    @GetMapping("/page/{pageno}")
    public String findPaginated(@PathVariable int pageno, Model model){
        Page<Invoice> invoiceList= invoiceService.getInvoicesPaginated(pageno, 8);

        model.addAttribute("invoiceList", invoiceList);
        model.addAttribute("currentPage",pageno);
        model.addAttribute("totalPages",invoiceList.getTotalPages());
        model.addAttribute("totalItem", invoiceList.getTotalElements());

        return "marketing/invoice-view";
    }

    @GetMapping("/specific-inv")
    @ResponseBody
    public Map<String, Object> getInvById(@RequestParam("id") int id){
        Invoice inv= invoiceService.getInvByIdAndFillFields(id);
        List<InvoiceProductInfo> prodList = invoiceService.getAllProdByInvNum(inv.getInvoiceNum());

        Map<String, Object> result = new HashMap<>();
        result.put("invoiceNum", inv.getInvoiceNum());
        result.put("poNum", inv.getPoNum());
        result.put("dateCreated", inv.getDateCreated());
        result.put("supplierName", inv.getSupplierName());
        result.put("clientName", inv.getClientName());
        result.put("clientContactPerson", inv.getClientContactPerson());
        result.put("productList", prodList);

        return result;
    }

    @PostMapping("/submit-form")
    public String handleSubmitForm(
            @RequestParam("inv-num") String invoiceNumber,
            @RequestParam("date-created")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateCreated,
            @RequestParam("supp-name") String supplierName,
            @RequestParam("client-name") String clientName,
            @RequestParam("client-cp") String clientContactPerson,
            @RequestParam("total-amt") String totalAmt,
            @RequestParam("qty-input") List<String> qtyList,
            @RequestParam("unit-input") List<String> unitList,
            @RequestParam("articles-input") List<String> articlesList,
            @RequestParam("unitP-input") List<String> unitPriceList,
            @RequestParam("amount-input") List<String> amountList,
            @RequestParam("id-input") List<String> prodIdList,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

         boolean ifSuccess = invoiceService.updateInvoices(invoiceNumber, dateCreated, supplierName, clientName, clientContactPerson, totalAmt, qtyList, unitList, articlesList, unitPriceList, amountList, prodIdList);

         if(ifSuccess == true){
             boolean hideDivSuccess = true;
             redirectAttributes.addFlashAttribute("hideDivSuccess", hideDivSuccess);
             return "redirect:/invoice-view";
         }else{
             boolean hideDivError = true;
             redirectAttributes.addFlashAttribute("hideDivError", hideDivError);
             return "redirect:/invoice-view";
         }
    }

    // @GetMapping("/create-or")
    // public String viewOfficialReceipt(Model model){
    //     // get the current date
    //     LocalDate currentDate = LocalDate.now();
    //     Random random = new Random();
    //     // get the current day and month
    //     int currentDay = currentDate.getDayOfMonth();
    //     int  currentMonth = currentDate.getMonthValue();
    //     int generateORNumber = (int) officialRecptService.getOrCount();
    //     // generate two random two-digit numbers between 10 and 99
    //     int randomNum = random.nextInt(90) + 10;
    //     if(generateORNumber == 0 || generateORNumber < 0){
    //         generateORNumber += 1;
    //     }else {
    //         generateORNumber += 1;
    //     }
    //     // format generateORNumber with a leading zero
    //     String generateORNumberStr = String.format("%02d", generateORNumber);
    //     String resultStr = String.format("%d%d%s", currentMonth, currentDay, generateORNumberStr);
    //     String orGeneratedNum = resultStr + randomNum;
    //     int result = Integer.parseInt(orGeneratedNum);

    //     model.addAttribute("generateORNumber", result);
    //     model.addAttribute("clientList", clientService.getAllClient());
    //     model.addAttribute("supplierList", supplierService.getAllSupplier());
    //     model.addAttribute("officialReceiptInfo", new OfficialReceiptInfo());
    //     // return destination = "marketing/officialreceipt";
    //     return "marketing/officialreceiptNew";
    // }

    @GetMapping("marketing-officialreceipt")
    public String marketingR(Model model){
        String role = GlobalUser.getUserRole();
        String userFname = GlobalUser.getUserFirstName();
        String userLname = GlobalUser.getUserLastName();
        String fullName = userLname+", "+userFname;
        String destination=null;
        if(role == null){
            return destination = "redirect:/login";
        } else if (role.equals("vr-staff")) {
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals(("marketing"))) {
            // get the current date
            LocalDate currentDate = LocalDate.now();
            Random random = new Random();
            // get the current day and month
            int currentDay = currentDate.getDayOfMonth();
            int  currentMonth = currentDate.getMonthValue();
            int generateORNumber = (int) officialRecptService.getOrCount();
            // generate two random two-digit numbers between 10 and 99
            int randomNum = random.nextInt(90) + 10;
            if(generateORNumber == 0 || generateORNumber < 0){
                generateORNumber += 1;
            }else {
                generateORNumber += 1;
            }
            // format generateORNumber with a leading zero
            String generateORNumberStr = String.format("%02d", generateORNumber);
            String resultStr = String.format("%d%d%s", currentMonth, currentDay, generateORNumberStr);
            String orGeneratedNum = resultStr + randomNum;
            int result = Integer.parseInt(orGeneratedNum);

            model.addAttribute("generateORNumber", result);
            model.addAttribute("fullName",fullName);
            model.addAttribute("clientList", clientService.getAllClient());
            model.addAttribute("supplierList", supplierService.getAllSupplier());
            model.addAttribute("officialReceiptInfo", new OfficialReceiptInfo());
            return destination = "marketing/officialreceipt";
        } else if (role.equals("admin")) {
            return destination = "redirect:/admin-dashboard";
        }
        return destination;
    }

    @GetMapping("marketing-collectionreceipt")
    public String collectionR(Model model){
        String role = GlobalUser.getUserRole();
        String userFname = GlobalUser.getUserFirstName();
        String userLname = GlobalUser.getUserLastName();
        String fullName = userLname+", "+userFname;
        String destination=null;
        if(role == null){
            return destination = "redirect:/login";
        } else if (role.equals("vr-staff")) {
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals(("marketing"))) {
            // get the current date
            LocalDate currentDate = LocalDate.now();
            Random random = new Random();
            // get the current day and month
            int currentDay = currentDate.getDayOfMonth();
            int  currentMonth = currentDate.getMonthValue();
            int generateORNumber = (int) collectionService.getCrCount();
            // generate two random two-digit numbers between 10 and 99
            int randomNum = random.nextInt(90) + 10;
            if(generateORNumber == 0 || generateORNumber < 0){
                generateORNumber += 1;
            }else {
                generateORNumber += 1;
            }
            // format generateORNumber with a leading zero
            String generateORNumberStr = String.format("%02d", generateORNumber);
            String resultStr = String.format("%d%d%s", currentMonth, currentDay, generateORNumberStr);
            String orGeneratedNum = resultStr + randomNum;
            int result = Integer.parseInt(orGeneratedNum);

            model.addAttribute("generateORNumber", result);
            model.addAttribute("fullName",fullName);
            model.addAttribute("clientList", clientService.getAllClient());
            model.addAttribute("supplierList", supplierService.getAllSupplier());
            model.addAttribute("collectionReceiptInfo", new CollectionReceiptInfo());
            return destination = "marketing/collectionreceipt";
        } else if (role.equals("admin")) {
            return destination = "redirect:/admin-dashboard";
        }
        return destination;
    }
}
