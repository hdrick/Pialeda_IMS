package pialeda.app.Invoice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pialeda.app.Invoice.dto.*;
import pialeda.app.Invoice.model.*;
import pialeda.app.Invoice.repository.CurrentInvoiceRepo;
import pialeda.app.Invoice.repository.InvoiceRepository;
import pialeda.app.Invoice.repository.OfficialRecptRepository;
import pialeda.app.Invoice.service.ClientService;
import pialeda.app.Invoice.service.CollectionService;
import pialeda.app.Invoice.service.OfficialRecptService;
import pialeda.app.Invoice.service.SupplierService;
import pialeda.app.Invoice.service.InvoiceService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;

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
    @Autowired
    private CurrentInvoiceRepo currentInvoiceRepo;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping("marketing-view/invoices")
    public String viewInvoices(Model model){
        return "marketing/invoice-view";
    }


    @PostMapping("/createInvoice")
    public String createInvoice(@ModelAttribute("invoiceInfo") InvoiceInfo invoiceInfo,
                                @RequestParam("invDate") String date,
                                @RequestParam("saleInvoiceDate") String saleInvoiceDateStr,
                                @RequestParam("qty") List<String> qtyList,
                                @RequestParam("unit") List<String> unitList,
                                @RequestParam("article") List<String> articlesList,
                                @RequestParam("unitPrice") List<String> unitPriceList,
                                @RequestParam("amount") List<String> amountList,
                                RedirectAttributes redirectAttributes,HttpSession session
                                 ){
        double remainingLimitOfSupp = invoiceService.getSuppTotalLimit(invoiceInfo.getSupplierName());
        double supplierLimit = supplierService.findLimitByName(invoiceInfo.getSupplierName());
        double remainingLimit = supplierLimit - remainingLimitOfSupp;
        Invoice ifInvExist = invoiceService.findByInvNum(invoiceInfo.getInvoiceNum());

        if(ifInvExist == null){
            if(invoiceInfo.getGrandTotal() <= remainingLimit){
                invoiceService.createInvoice(invoiceInfo, date, saleInvoiceDateStr, qtyList, unitList,articlesList,unitPriceList,amountList);
                boolean hideDivSuccess = true;
                redirectAttributes.addFlashAttribute("hideDivSuccess", hideDivSuccess);
                updateInvoiceAmount(invoiceInfo.getInvoiceNum());
                return "redirect:/marketing-invoice";
            }else{
                boolean hideDivError = true;
                redirectAttributes.addFlashAttribute("hideDivError", hideDivError);
                return "redirect:/marketing-invoice";
            }
        }else{
            boolean hideDivDuplicateInv = true;
            redirectAttributes.addFlashAttribute("hideDivDuplicateInv", hideDivDuplicateInv);
            return "redirect:/marketing-invoice";
        }
    }

    @PutMapping()
    public void updateInvoiceAmount(String invoiceNum) {
        Optional<CurrentInvoice> cinv = currentInvoiceRepo.findById(1);
        
        if(cinv.isPresent()){
            CurrentInvoice currentInvoice = cinv.get();
            currentInvoice.setInvoiceNum(invoiceNum);
            currentInvoiceRepo.save(currentInvoice);
        }else{
            CurrentInvoice currentInvoice = new CurrentInvoice(invoiceNum);
            currentInvoice.setInvoiceNum(invoiceNum);
            currentInvoiceRepo.save(currentInvoice);
        }
    }
    
    @PostMapping("/createClient")
    public String createClient(@ModelAttribute("clientInfo") ClientInfo clientInfo, 
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Client ifExist = clientService.findByName(clientInfo.getName());

        if(ifExist == null){
            boolean hideDivSuccessClient = true;
            redirectAttributes.addFlashAttribute("hideDivSuccessClient", hideDivSuccessClient);
            clientService.createClient(clientInfo);
            return "redirect:/marketing-invoice";
        }else{
            boolean hideDivErrorDuplicateClient = true;
            redirectAttributes.addFlashAttribute("hideDivErrorDuplicateClient", hideDivErrorDuplicateClient);
            return "redirect:/marketing-invoice";
        }
    }

    @PostMapping("/createSupplier")
    public String createSupplier(@ModelAttribute("supplierInfo") SupplierInfo supplierInfo, Model model,
                                  RedirectAttributes redirectAttributes) {

        Supplier ifExist = supplierService.findByName(supplierInfo.getName());

        if(ifExist == null){
            boolean hideDivSuccessSupp = true;
            redirectAttributes.addFlashAttribute("hideDivSuccessSupp", hideDivSuccessSupp);
            supplierService.createSupplier(supplierInfo);
            return "redirect:/marketing-invoice";
        }else{
            boolean hideDivErrorDuplicateSupp = true;
            redirectAttributes.addFlashAttribute("hideDivErrorDuplicateSupp", hideDivErrorDuplicateSupp);
            return "redirect:/marketing-invoice";
        }
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
            System.out.println("ifExistssss: ");
            OfficialReceipt ifExist = officialRecptService.getOrNumByOrNum(orNumber);
            System.out.println("ifExist: "+ifExist);
            if(ifExist != null){
                boolean hideDivDuplicateOR = true;
                redirectAttributes.addFlashAttribute("hideDivDuplicateOR", hideDivDuplicateOR);
                return "redirect:/marketing-officialreceipt";
            }else{
                officialRecptService.createOR(orNumber, totalSales, addVat, lwTax, amtDue, ewt, total, cash, chckNo, orAmount, cashierName, requestParams, officialReceiptInfo);

                boolean hideDivSuccessOR = true;
                redirectAttributes.addFlashAttribute("hideDivSuccessOR", hideDivSuccessOR);
                return "redirect:/marketing-officialreceipt";
            }
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

      CollectionReceipt ifExist = collectionService.getCrNumByCrNum(orNumber);

      if(ifExist !=null){
        boolean hideDivDuplicateCR = true;
        redirectAttributes.addFlashAttribute("hideDivDuplicateCR", hideDivDuplicateCR);
        return "redirect:/marketing-collectionreceipt";
      }else{
        collectionService.createCR(orNumber, amtDue, ewt, total, cash, chckNo, cashierName, requestParams, collectionReceiptInfo);
        boolean hideDivSuccessOR = true;
        redirectAttributes.addFlashAttribute("hideDivSuccessOR", hideDivSuccessOR);
        return "redirect:/marketing-collectionreceipt";
      }
    }
      
    @GetMapping("/getInvoiceInfo")
    @ResponseBody
    public Invoice getInvoiceInfo(@RequestParam("invNum") String invNum) {
        Invoice invInfo = invoiceService.findByInvNum(invNum);
        // Client clientInfo = new Client(client.getName(), client.getAddress(), 
        //                                client.getCityAddress(), client.getTin(), 
        //                                client.getAgent(),client.getBusStyle(),
        //                                client.getTerms());
        return invInfo;
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

    @GetMapping("/getCRInvoiceDetails")
    @ResponseBody
    public List<Invoice> getInvoiceInfo(@RequestParam("supplier") String supplierName, @RequestParam("client") String clientName) {
        List<Invoice> crInvoice = invoiceService.getCRInvoice(supplierName, clientName);
        return crInvoice;
    }

    @GetMapping("/getORInvoiceDetails")
    @ResponseBody
    public List<Invoice> getORInvoiceInfo(@RequestParam("supplier") String supplierName, @RequestParam("client") String clientName) {
        List<Invoice> orInvoice = invoiceService.getORInvoice(supplierName, clientName);
        return orInvoice;
    }

    @GetMapping("/official-receipt-list")
    public String officialReceiptList(Model model){
        String role = GlobalUser.getUserRole();
        String destination=null;
        if(role == null){
            return destination = "redirect:/login";
        } else if (role.equals("vr-staff")) {
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals(("marketing"))) {
            return destination = findPaginatedOR(0, model);
        } else if (role.equals("admin")) {
            return destination = "redirect:/admin-dashboard";
        }
        return destination;
    }

    @GetMapping("/collection-receipt-list")
    public String collectionReceiptList(Model model){
        String role = GlobalUser.getUserRole();
        String destination=null;
        if(role == null){
            return destination = "redirect:/login";
        } else if (role.equals("vr-staff")) {
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals(("marketing"))) {
            return destination = findPaginatedCR(0, model);
        } else if (role.equals("admin")) {
            return destination = "redirect:/admin-dashboard";
        }
        return destination;

    }

    @GetMapping("/pages/{pageno}")
    public String findPaginatedOR(@PathVariable int pageno, Model model){
        Page<OfficialReceipt> officialReceiptList= officialRecptService.getOfficialReceiptPaginated(pageno, 8);
        System.out.println("officialReceiptList: "+officialReceiptList);

        model.addAttribute("officialReceiptList", officialReceiptList);
        model.addAttribute("currentPage",pageno);
        model.addAttribute("totalPages",officialReceiptList.getTotalPages());
        model.addAttribute("totalItem", officialReceiptList.getTotalElements());

        return "marketing/listofficialreceipt";
    }

    @GetMapping("/pagescr/{pageno}")
    public String findPaginatedCR(@PathVariable int pageno, Model model){
        Page<CollectionReceipt> collectionReceiptList= collectionService.getCollectionReceiptPaginated(pageno, 8);
        System.out.println("collectionReceiptList: "+collectionReceiptList);

        model.addAttribute("collectionReceiptList", collectionReceiptList);
        model.addAttribute("currentPage",pageno);
        model.addAttribute("totalPages",collectionReceiptList.getTotalPages());
        model.addAttribute("totalItem", collectionReceiptList.getTotalElements());

        return "marketing/listcollectionreceipt";
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
        result.put("saleInvoiceDate", inv.getSaleInvoiceDate());
        result.put("poNum", inv.getPoNum());
        result.put("dateCreated", inv.getDateCreated());

        result.put("supplierName", inv.getSupplierName());
        result.put("supplierAddress", inv.getSupplierAddress());
        result.put("supplierTin", inv.getSupplierTin());



        result.put("clientName", inv.getClientName());
        result.put("clientBusStyle", inv.getClientBusStyle());
        result.put("clientTerms", inv.getClientTerms());
        result.put("clientAddress", inv.getClientAddress());
        result.put("clientTin", inv.getClientTin());

        result.put("clientContactPerson", inv.getClientContactPerson());
        result.put("productList", prodList);

        result.put("addVat", inv.getAddVat());
        result.put("amountNetOfVat", inv.getAmountNetOfVat());
        result.put("totalSalesVatInc", inv.getTotalSalesVatInc());
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
            @RequestParam("addVat") String addVat,
            @RequestParam("amtNetVat") String amtNetVat,
            @RequestParam("totalSalesVatInc") String totalSalesVatInc,
            @RequestParam("client-terms") String clientTerms,
            @RequestParam("client-bStyle") String clientBStyle,
            @RequestParam("qty-input") List<String> qtyList,
            @RequestParam("unit-input") List<String> unitList,
            @RequestParam("articles-input") List<String> articlesList,
            @RequestParam("unitP-input") List<String> unitPriceList,
            @RequestParam("amount-input") List<String> amountList,
            @RequestParam("id-input") List<String> prodIdList,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

         boolean ifSuccess = invoiceService.updateInvoices(invoiceNumber, dateCreated, supplierName, clientName, 
         clientContactPerson, totalAmt, addVat, amtNetVat, totalSalesVatInc, qtyList, unitList, articlesList, 
         unitPriceList, amountList, prodIdList, clientTerms, clientBStyle);

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
            model.addAttribute("invoiceList", invoiceService.getAllInvoiceByStatusUnpaid());
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
            model.addAttribute("invoiceList", invoiceService.getAllInvoiceByStatusUnpaid());
            model.addAttribute("supplierList", supplierService.getAllSupplier());
            model.addAttribute("collectionReceiptInfo", new CollectionReceiptInfo());

            return destination = "marketing/collectionreceipt";
        } else if (role.equals("admin")) {
            return destination = "redirect:/admin-dashboard";
        }
        return destination;
    }

    @GetMapping("/official-receipt-view/{id}")
    public String specificOfficialReceipt(@PathVariable int id,Model model){
        OfficialReceipt specificId = officialRecptService.getSpecificOfficialReceipt(id);
        List<OfficialReceiptInvoices> invoices = officialRecptService.getInvoiceByOrNum(specificId.getOfficialReceiptNum());
        model.addAttribute("specificId", specificId);

        model.addAttribute("orInvoiceInfo", invoices);
        return "marketing/specificor";
    }

    @GetMapping("/collection-receipt-view/{id}")
    public String specificCollectionReceipt(@PathVariable int id,Model model){
        CollectionReceipt specificId = collectionService.getSpecificCollectionReceipt(id);
        List<CollectionReceiptInvoices> invoices = collectionService.getInvoiceByCrNum(specificId.getCollectionReceiptNum());
        model.addAttribute("specificId", specificId);

        model.addAttribute("crInvoiceInfo", invoices);
        return "marketing/specificcr";
    }

    @PostMapping("/updateSpecificOr/{id}")
    public String updateOrById(@PathVariable("id") int id, @RequestParam("clientName") String clientName,
                             @RequestParam("clientAddress") String clientAddress,
                             @RequestParam("clientTIN") String clientTIN,
                             @RequestParam("busStyle") String busStyle,
                             @RequestParam("wPayment") String wPayment,
                             @RequestParam("nPayment") Double nPayment,
                             @RequestParam("partialP") String partialP,
                             @RequestParam("suppName") String suppName,
                             @RequestParam("suppAddrs") String suppAddrs,
                             @RequestParam("suppTIN") String suppTIN,
                             @RequestParam("cash") String cash,
                             @RequestParam("chckNo") String chckNo,
                             @RequestParam("amount") Double amount,
                             @RequestParam("orDate") String orDate,
                             RedirectAttributes redirectAttributes){

        Boolean result = officialRecptService.updateOr(id, clientName, clientAddress, clientTIN, busStyle, wPayment, nPayment, partialP, suppName, suppAddrs, suppTIN, cash, chckNo, amount, orDate);                       
        if(result == true){
            boolean hideDivSuccess = true;
            redirectAttributes.addFlashAttribute("hideDivSuccess", hideDivSuccess);
            return "redirect:/official-receipt-view/" + id;
        }else{
            boolean hideDivError = true;
            redirectAttributes.addFlashAttribute("hideDivError", hideDivError);
            return "redirect:/official-receipt-view/" + id;
        } 
    }

    @PostMapping("/updateSpecificCr/{id}")
    public String updateCrById(@PathVariable("id") int id, @RequestParam("clientName") String clientName,
                             @RequestParam("clientAddress") String clientAddress,
                             @RequestParam("clientTIN") String clientTIN,
                             @RequestParam("busStyle") String busStyle,
                             @RequestParam("wPayment") String wPayment,
                             @RequestParam("nPayment") Double nPayment,
                             @RequestParam("partialP") String partialP,
                             @RequestParam("suppName") String suppName,
                             @RequestParam("suppAddrs") String suppAddrs,
                             @RequestParam("suppTIN") String suppTIN,
                             @RequestParam("cash") String cash,
                             @RequestParam("chckNo") String chckNo,
                             @RequestParam("amount") Double amount,
                             @RequestParam("orDate") String orDate,
                             RedirectAttributes redirectAttributes){

        Boolean result = collectionService.updateCr(id, clientName, clientAddress, clientTIN, busStyle, wPayment, nPayment, partialP, suppName, suppAddrs, suppTIN, cash, chckNo, amount, orDate);                       
        if(result == true){
            boolean hideDivSuccess = true;
            redirectAttributes.addFlashAttribute("hideDivSuccess", hideDivSuccess);
            return "redirect:/official-receipt-view/" + id;
        }else{
            boolean hideDivError = true;
            redirectAttributes.addFlashAttribute("hideDivError", hideDivError);
            return "redirect:/official-receipt-view/" + id;
        } 
    }

    @GetMapping("/supplier-list")
    public String viewSupplierList(Model model){
        String role = GlobalUser.getUserRole();
        String destination=null;
        if(role == null){
            return destination = "redirect:/login";
        } else if (role.equals("vr-staff")) {
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals(("marketing"))) {
            return destination = findPaginatedSupplier(0, model);
        } else if (role.equals("admin")) {
            return destination = "redirect:/admin-dashboard";
        }
        return destination;
    }

    @GetMapping("/pagesupp/{pageno}")
    public String findPaginatedSupplier(@PathVariable int pageno, Model model){
        Page<Supplier> supplierList= supplierService.getSupplierPaginated(pageno,8);

        model.addAttribute("supplierList", supplierList);
        model.addAttribute("currentPage",pageno);
        model.addAttribute("totalPages",supplierList.getTotalPages());
        model.addAttribute("totalItem", supplierList.getTotalElements());

        return "marketing/supplierList";
    }

    @GetMapping("/supplier-view/{id}")
    public String specificSupplier(@PathVariable int id,Model model){
        Supplier specificId = supplierService.getSpecificSupplier(id);
        BigDecimal supplierReachAmount = invoiceService.getTotalAmountBySupplierName(specificId.getName());

        double limit = specificId.getLimit();
        double supplierReachAmountConv = supplierReachAmount.doubleValue();
        double currentLimit = limit - supplierReachAmountConv;

        model.addAttribute("specificId", specificId);
        model.addAttribute("supplierReachAmount", supplierReachAmount);
        model.addAttribute("currentLimit", currentLimit);
        return "marketing/specificSupplier";
    }

    @PostMapping("/updateSpecificSupplier/{id}")
    public String updateSupplierById(@PathVariable("id") int id,
                                     @RequestParam("name") String suppName,
                                     @RequestParam("address") String address,
                                     @RequestParam("cityAddress") String cityAddress,
                                     @RequestParam("tin") String tin,
                                     @RequestParam("secNum") String secNum,
                                     @RequestParam("secYear") String secYear,
                                     @RequestParam("atp") String atp,
                                     @RequestParam("corNum") String corNum,
                                     @RequestParam("corDate") String corDate,
                                     @RequestParam("limit") double limit,
                                     RedirectAttributes redirectAttributes){

        Boolean result = supplierService.updateSupplierById(id,suppName,address,cityAddress,tin,secNum,secYear,atp,
                                                            corNum,corDate,limit);                       
        if(result == true){
            boolean hideDivSuccess = true;
            redirectAttributes.addFlashAttribute("hideDivSuccess", hideDivSuccess);
            return "redirect:/supplier-view/" + id;
        }else{
            boolean hideDivError = true;
            redirectAttributes.addFlashAttribute("hideDivError", hideDivError);
            return "redirect:/supplier-view/" + id;
        } 
    }


    @GetMapping("/client-list")
    public String viewClientList(Model model){
        String role = GlobalUser.getUserRole();
        String destination=null;

        if(role == null){
            return destination = "redirect:/login";
        } else if (role.equals("vr-staff")) {
            return destination = "redirect:/vr/user/invoices";
        } else if (role.equals(("marketing"))) {
            return destination = findPaginatedClient(0, model);
        } else if (role.equals("admin")) {
            return destination = "redirect:/admin-dashboard";
        }
        return destination;
    }

    @GetMapping("/pageclient/{pageno}")
    public String findPaginatedClient(@PathVariable int pageno, Model model){
        Page<Client> clientList= clientService.getClientPaginated(pageno,8);

        model.addAttribute("clientList", clientList);
        model.addAttribute("currentPage",pageno);
        model.addAttribute("totalPages",clientList.getTotalPages());
        model.addAttribute("totalItem", clientList.getTotalElements());

        return "marketing/clientList";
    }

    @GetMapping("/client-view/{id}")
    public String specificClient(@PathVariable int id,Model model){
        Client specificId = clientService.getSpecificClient(id);

        model.addAttribute("specificId", specificId);
        return "marketing/specificClient";
    }

    @PostMapping("/updateSpecificClient/{id}")
    public String updateClientById(@PathVariable("id") int id,
                                     @RequestParam("name") String suppName,
                                     @RequestParam("address") String address,
                                     @RequestParam("cityAddress") String cityAddress,
                                     @RequestParam("tin") String tin,
                                     @RequestParam("agent") String agent,
                                     @RequestParam("busStyle") String busStyle,
                                     @RequestParam("terms") String terms,
                                     RedirectAttributes redirectAttributes){

        Boolean result = clientService.updateClientById(id,suppName,address,cityAddress,tin,agent,busStyle,terms);                       
        if(result == true){
            boolean hideDivSuccess = true;
            redirectAttributes.addFlashAttribute("hideDivSuccess", hideDivSuccess);
            return "redirect:/client-view/" + id;
        }else{
            boolean hideDivError = true;
            redirectAttributes.addFlashAttribute("hideDivError", hideDivError);
            return "redirect:/client-view/" + id;
        } 
    }

    @GetMapping("/deleteClient/{id}")
    public String deleteClient(@PathVariable("id") int id, 
                                RedirectAttributes redirectAttributes){
                                    System.out.println("CONTROLLER");
        // clientService.deleteClient(id);
        // return "redirect:/client-list";
        if(clientService.deleteClient(id)){
            boolean hideDivSuccessDel = true;
            redirectAttributes.addFlashAttribute("hideDivSuccessDel", hideDivSuccessDel);
            System.out.println("CONTROLLER:TRUE");
            return "redirect:/client-list";
        }else{
            boolean hideDivErrorDel = true;
            redirectAttributes.addFlashAttribute("hideDivErrorDel", hideDivErrorDel);
            System.out.println("CONTROLLER:FALSE");
            return "redirect:/client-list";
        } 
    }

    @GetMapping("/deleteSupp/{id}")
    public String deleteSupplier(@PathVariable("id") int id, 
                                RedirectAttributes redirectAttributes){
        if(supplierService.deleteSupp(id)){
            boolean hideDivSuccessDel = true;
            redirectAttributes.addFlashAttribute("hideDivSuccessDel", hideDivSuccessDel);
            System.out.println("CONTROLLER:TRUE");
            return "redirect:/supplier-list";
        }else{
            boolean hideDivErrorDel = true;
            redirectAttributes.addFlashAttribute("hideDivErrorDel", hideDivErrorDel);
            System.out.println("CONTROLLER:FALSE");
            return "redirect:/supplier-list";
        } 
    }




    // @PostMapping("/duplicate/{id}")
    // public String duplicateInvoice(@PathVariable("id") int id,
    //                                  @RequestParam("d-invName") String invoiceNum,
    //                                  @RequestParam("d-poNum") String poNum,
    //                                  @RequestParam("d-date") String date,
    //                                  RedirectAttributes redirectAttributes){
        
    //    String result = invoiceService.duplicateSpecificInvoice(id, invoiceNum, poNum, date);
    //    System.out.println("result: "+result);
    //    if(result.equals("success")){
    //         return "success";
    //    }else{
    //      return "failed";
    //    }
    // }

@PostMapping("/duplicate/{id}")
public ResponseEntity<String> duplicateInvoice(@PathVariable("id") int id,
                               @RequestParam("d-invName") String invoiceNum,
                               @RequestParam("d-poNum") String poNum,
                               @RequestParam("d-date") String date,
                               RedirectAttributes redirectAttributes, Model model) {
  
    Invoice invoiceToDuplicate = invoiceRepository.findById(id);

    double remainingLimitOfSupp = invoiceService.getSuppTotalLimit(invoiceToDuplicate.getSupplierName());
    double supplierLimit = supplierService.findLimitByName(invoiceToDuplicate.getSupplierName());
    double remainingLimit = supplierLimit - remainingLimitOfSupp;

     if(invoiceToDuplicate.getGrandTotal() <= remainingLimit){
        String result = invoiceService.duplicateSpecificInvoice(id, invoiceNum, poNum, date);
        return ResponseEntity.ok(result);
     }else{
        String result2 = "supplierLimitExceed";
        return ResponseEntity.ok(result2);
     }

    
}



}
