package pialeda.app.Invoice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pialeda.app.Invoice.config.DateUtils;
import pialeda.app.Invoice.config.MonthEnum;
import pialeda.app.Invoice.model.*;
import pialeda.app.Invoice.service.ClientService;
import pialeda.app.Invoice.service.CollectionService;
import pialeda.app.Invoice.service.InvoiceService;
import pialeda.app.Invoice.service.SupplierService;
import pialeda.app.Invoice.dto.GlobalUser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Controller
public class VRController {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CollectionService collectionService;

    @GetMapping("vr/user/invoices/search")
    public String searchInvoice(Model model, @RequestParam(name="query", required = false) String query,
                                @RequestParam(name="page", required = false, defaultValue = "1") int currentPage)
    {


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
        } else if (role.equals("vr-staff"))
        {
            String keyword;
            String monthValue = MonthEnum.getMonthValueByName(query);
//            if (isValidMonth)
//            {
//                keyword = String.valueOf(monthValue);
//            }
//            else
//            {
//                keyword = query;
//            }

            Page<Invoice> page = invoiceService.getPageByKeyword(monthValue, currentPage);
            List<Client> clients = clientService.getAllClient();
            List<String> suppliers = supplierService.getAllSupplierName();
            List<Invoice> invoices = page.getContent();

            int totalPages = page.getTotalPages();
            long totalItems = page.getTotalElements();

            int startPage = Math.max(0, currentPage - 2);
            int endPage = Math.min(totalPages - 1, startPage + 4);

            if (currentPage > 2 && currentPage + 2 < totalPages) {
                startPage = currentPage - 2;
                endPage = currentPage + 2;
            } else if (currentPage + 2 >= totalPages) {
                endPage = totalPages - 1;
                startPage = Math.max(0, endPage - 4);
            }

            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = startPage; i <= endPage; i++) {
                pageNumbers.add(i);
            }

            model.addAttribute("fullName",fullName);

            model.addAttribute("currentPage", currentPage);
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", totalItems);
            model.addAttribute("invoices", invoices);

            model.addAttribute("clients", clients);
            model.addAttribute("suppliers", suppliers);

            model.addAttribute("selectedClient", null);
            model.addAttribute("selectedSupplier", null);
            model.addAttribute("selectedQuery", query);

            model.addAttribute("isQueryPresent", true);
            model.addAttribute("isClientPresent", false);
            model.addAttribute("isSupplierPresent", false);
            model.addAttribute("isDateString", false);

            return destination = "vr-staff/vr";
        }
        return destination;

    }

    public String getDefaultPage(Model model, int currentPage, String fullName)
    {
        Page<Invoice> page = invoiceService.findPage(currentPage);
        List<Client> clients = clientService.getAllClient();
        List<String> suppliers = supplierService.getAllSupplierName();
        List<Invoice> invoices = page.getContent();

        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();

        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, startPage + 4);

        if (currentPage > 2 && currentPage + 2 < totalPages) {
            startPage = currentPage - 2;
            endPage = currentPage + 2;
        } else if (currentPage + 2 >= totalPages) {
            endPage = totalPages - 1;
            startPage = Math.max(0, endPage - 4);
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("fullName",fullName);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("invoices", invoices);

        model.addAttribute("clients", clients);
        model.addAttribute("suppliers", suppliers);

        model.addAttribute("selectedClient", null);
        model.addAttribute("selectedSupplier", null);

        model.addAttribute("isClientPresent", false);
        model.addAttribute("isSupplierPresent", false);
        model.addAttribute("isDateString", false);

        return "vr-staff/vr";
    }
    public String filterSortClientSupplierPage(Model model, String client, String supplier, int currentPage, String fullName)
    {
        Page<Invoice> page = invoiceService.filterPageByClientAndSupplier(client, supplier, currentPage);
        BigDecimal totalInvoice = invoiceService.getTotalAmountByClientNameAndSupplierName(client, supplier);
        BigDecimal totalAmount = invoiceService.getTotalAmountBySupplierName(supplier);
        BigDecimal totalReceipt = invoiceService.getTotalReceiptBySupplierName(supplier);
        BigDecimal supplierReachAmount = totalAmount.add(totalReceipt);
        double supplierLimit = supplierService.findLimitByName(supplier);

        NumberFormat format = new DecimalFormat("#,##0.00");
        String formattedTotalAmount = format.format(totalInvoice);
        String formattedSupplierReachAmount = format.format(supplierReachAmount);
        String formattedSupplierLimit = format.format(supplierLimit);

//        List<Client> clients = clientService.getAllClient();
        List<Invoice> clients = invoiceService.getClientOfSupplier(supplier);
        List<String> suppliers = supplierService.getAllSupplierName();
        List<Invoice> invoices = page.getContent();

        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();

        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, startPage + 4);

        if (currentPage > 2 && currentPage + 2 < totalPages) {
            startPage = currentPage - 2;
            endPage = currentPage + 2;
        } else if (currentPage + 2 >= totalPages) {
            endPage = totalPages - 1;
            startPage = Math.max(0, endPage - 4);
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("fullName",fullName);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("invoices", invoices);

        model.addAttribute("clients", clients);
        model.addAttribute("suppliers", suppliers);

        model.addAttribute("totalInvoicesAmount", formattedTotalAmount);
        model.addAttribute("supplierReachAmount", formattedSupplierReachAmount);
        model.addAttribute("supplierLimit", formattedSupplierLimit);

        model.addAttribute("selectedClient", client);
        model.addAttribute("selectedSupplier", supplier);

        model.addAttribute("isClientPresent", true);
        model.addAttribute("isSupplierPresent", true);
        model.addAttribute("isDateString", false);
        return "vr-staff/vr";
    }

    public String filterPageClient(Model model, String client, int currentPage, String fullName)
    {
        Page<Invoice> page = invoiceService.filterPageByClient(client, currentPage);
        List<Client> clients = clientService.getAllClient();
        List<String> suppliers = supplierService.getAllSupplierName();
        List<Invoice> invoices = page.getContent();

        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();

        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, startPage + 4);

        if (currentPage > 2 && currentPage + 2 < totalPages) {
            startPage = currentPage - 2;
            endPage = currentPage + 2;
        } else if (currentPage + 2 >= totalPages) {
            endPage = totalPages - 1;
            startPage = Math.max(0, endPage - 4);
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("fullName",fullName);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("invoices", invoices);

        model.addAttribute("clients", clients);
        model.addAttribute("suppliers", suppliers);

        model.addAttribute("selectedClient", client);
        model.addAttribute("selectedSupplier", null);

        model.addAttribute("isClientPresent", true);
        model.addAttribute("isSupplierPresent", false);
        model.addAttribute("isDateString", false);
        return "vr-staff/vr";
    }

    public String filterPageSupplier(Model model, String supplier, int currentPage, String fullName)
    {
        Page<Invoice> page = invoiceService.filterPageBySupplier(supplier, currentPage);
        BigDecimal totalAmount = invoiceService.getTotalAmountBySupplierName(supplier);
        BigDecimal totalReceipt = invoiceService.getTotalReceiptBySupplierName(supplier);
        BigDecimal supplierReachAmount = totalAmount.add(totalReceipt);

        double supplierLimit = supplierService.findLimitByName(supplier);

        NumberFormat format = new DecimalFormat("#,##0.00");
        String formattedTotalAmount = format.format(totalAmount);
        String formattedSupplierReachAmount = format.format(supplierReachAmount);
        String formattedSupplierLimit = format.format(supplierLimit);

//        List<Client> clients = clientService.getAllClient();
        List<Invoice> clients = invoiceService.getClientOfSupplier(supplier);
        List<String> suppliers = supplierService.getAllSupplierName();
        List<Invoice> invoices = page.getContent();
        List<OfficialReceipt> officialReceipts = invoiceService.getSupplierOR(supplier);

        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        long receiptItems = officialReceipts.size();

        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, startPage + 4);

        if (currentPage > 2 && currentPage + 2 < totalPages) {
            startPage = currentPage - 2;
            endPage = currentPage + 2;
        } else if (currentPage + 2 >= totalPages) {
            endPage = totalPages - 1;
            startPage = Math.max(0, endPage - 4);
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("fullName",fullName);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("invoices", invoices);
        model.addAttribute("officialReceipts", officialReceipts);
        model.addAttribute("receiptItems", receiptItems);

        model.addAttribute("clients", clients);
        model.addAttribute("suppliers", suppliers);

        model.addAttribute("totalInvoicesAmount", formattedTotalAmount);
        model.addAttribute("supplierReachAmount", formattedSupplierReachAmount);
        model.addAttribute("supplierLimit", formattedSupplierLimit);

        model.addAttribute("selectedClient", null);
        model.addAttribute("selectedSupplier", supplier);

        model.addAttribute("isClientPresent", false);
        model.addAttribute("isSupplierPresent", true);
        model.addAttribute("isDateString", false);
        return "vr-staff/vr";
    }

    public String invalidDateFormat(Model model, String message)
    {
        List<Client> clients = clientService.getAllClient();
        List<String> suppliers = supplierService.getAllSupplierName();

        model.addAttribute("clients", clients);
        model.addAttribute("suppliers", suppliers);

        model.addAttribute("isClientPresent", true);
        model.addAttribute("isSupplierPresent", false);
        model.addAttribute("isInvalidDate", true);
        model.addAttribute("errorMessage", message);
        return "vr-staff/vr";
    }
    public String filterClientSortByDateRange(Model model, String client, LocalDate startDate, LocalDate endDate, int currentPage, String fullName)
    {
        Page<Invoice> page = invoiceService.filterPageByClientSortByDateRange(client, startDate, endDate, currentPage);
        List<Client> clients = clientService.getAllClient();
        List<String> suppliers = supplierService.getAllSupplierName();
        List<Invoice> invoices = page.getContent();

        String dateStartString = DateUtils.parseDateToString(startDate);
        String dateEndString = DateUtils.parseDateToString(endDate);

        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();

        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, startPage + 4);

        if (currentPage > 2 && currentPage + 2 < totalPages) {
            startPage = currentPage - 2;
            endPage = currentPage + 2;
        } else if (currentPage + 2 >= totalPages) {
            endPage = totalPages - 1;
            startPage = Math.max(0, endPage - 4);
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("fullName",fullName);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("invoices", invoices);

        model.addAttribute("clients", clients);
        model.addAttribute("suppliers", suppliers);

        model.addAttribute("selectedClient", client);
        model.addAttribute("selectedSupplier", null);

        model.addAttribute("isClientPresent", true);
        model.addAttribute("isSupplierPresent", false);
        model.addAttribute("isDateString", true);

        model.addAttribute("startDate", dateStartString);
        model.addAttribute("endDate", dateEndString);

        return "vr-staff/vr";
    }



    public String filterSupplierSortByDateRange(Model model, String supplier, LocalDate startDate, LocalDate endDate, int currentPage, String fullName)
    {
        Page<Invoice> page = invoiceService.filterPageBySupplierSortByDateRange(supplier, startDate, endDate, currentPage);
        BigDecimal totalInvoice = invoiceService.getTotalAmountBySupplierNameBetweenDateRange(supplier,startDate, endDate);
        BigDecimal totalAmount = invoiceService.getTotalAmountBySupplierName(supplier);
        BigDecimal totalReceipt = invoiceService.getTotalReceiptBySupplierName(supplier);
        BigDecimal supplierReachAmount = totalAmount.add(totalReceipt);
        double supplierLimit = supplierService.findLimitByName(supplier);

        NumberFormat format = new DecimalFormat("#,##0.00");
        String formattedTotalAmount = format.format(totalInvoice);
        String formattedSupplierReachAmount = format.format(supplierReachAmount);
        String formattedSupplierLimit = format.format(supplierLimit);

//        List<Client> clients = clientService.getAllClient();
        List<Invoice> clients = invoiceService.getClientOfSupplier(supplier);
        List<String> suppliers = supplierService.getAllSupplierName();
        List<Invoice> invoices = page.getContent();

        String dateStartString = DateUtils.parseDateToString(startDate);
        String dateEndString = DateUtils.parseDateToString(endDate);

        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();

        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, startPage + 4);

        if (currentPage > 2 && currentPage + 2 < totalPages) {
            startPage = currentPage - 2;
            endPage = currentPage + 2;
        } else if (currentPage + 2 >= totalPages) {
            endPage = totalPages - 1;
            startPage = Math.max(0, endPage - 4);
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("fullName",fullName);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("invoices", invoices);

        model.addAttribute("clients", clients);
        model.addAttribute("suppliers", suppliers);

        model.addAttribute("totalInvoicesAmount", formattedTotalAmount);
        model.addAttribute("supplierReachAmount", formattedSupplierReachAmount);
        model.addAttribute("supplierLimit", formattedSupplierLimit);

        model.addAttribute("selectedClient", null);
        model.addAttribute("selectedSupplier", supplier);

        model.addAttribute("isClientPresent", false);
        model.addAttribute("isSupplierPresent", true);
        model.addAttribute("isDateString", true);

        model.addAttribute("startDate", dateStartString);
        model.addAttribute("endDate", dateEndString);

        return "vr-staff/vr";
    }

    public String filterClientSupplierSortByDateRange(Model model, String client, String supplier, LocalDate startDate, LocalDate endDate, int currentPage, String fullName)
    {
        Page<Invoice> page = invoiceService.filterPageByClientAndSupplierSortByDateRange(client, supplier, startDate, endDate, currentPage);
        BigDecimal totalInvoice = invoiceService. getTotalAmountByClientNameAndSupplierNameBetweenDateRange(client, supplier,startDate, endDate);
        BigDecimal totalAmount = invoiceService.getTotalAmountBySupplierName(supplier);
        BigDecimal totalReceipt = invoiceService.getTotalReceiptBySupplierName(supplier);
        BigDecimal supplierReachAmount = totalAmount.add(totalReceipt);
        double supplierLimit = supplierService.findLimitByName(supplier);

        NumberFormat format = new DecimalFormat("#,##0.00");
        String formattedTotalAmount = format.format(totalInvoice);
        String formattedSupplierReachAmount = format.format(supplierReachAmount);
        String formattedSupplierLimit = format.format(supplierLimit);

//        List<Client> clients = clientService.getAllClient();
        List<Invoice> clients = invoiceService.getClientOfSupplier(supplier);
        List<String> suppliers = supplierService.getAllSupplierName();
        List<Invoice> invoices = page.getContent();

        String dateStartString = DateUtils.parseDateToString(startDate);
        String dateEndString = DateUtils.parseDateToString(endDate);

        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();

        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, startPage + 4);

        if (currentPage > 2 && currentPage + 2 < totalPages) {
            startPage = currentPage - 2;
            endPage = currentPage + 2;
        } else if (currentPage + 2 >= totalPages) {
            endPage = totalPages - 1;
            startPage = Math.max(0, endPage - 4);
        }

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("fullName",fullName);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("invoices", invoices);

        model.addAttribute("clients", clients);
        model.addAttribute("suppliers", suppliers);

        model.addAttribute("totalInvoicesAmount", formattedTotalAmount);
        model.addAttribute("supplierReachAmount", formattedSupplierReachAmount);
        model.addAttribute("supplierLimit", formattedSupplierLimit);

        model.addAttribute("selectedClient", client);
        model.addAttribute("selectedSupplier", supplier);

        model.addAttribute("isClientPresent", true);
        model.addAttribute("isSupplierPresent", true);
        model.addAttribute("isDateString", true);

        model.addAttribute("startDate", dateStartString);
        model.addAttribute("endDate", dateEndString);

        return "vr-staff/vr";
    }

    @GetMapping("/vr/user/invoice/invoiceDetails/{invoiceNum}")
    public String viewInvoice(Model model, @PathVariable("invoiceNum") String invoiceNum) {
        String role = GlobalUser.getUserRole();

        if (role == null) {
            String jsCode = "<script>window.close();</script>";
            model.addAttribute("jsCode", jsCode);
            model.addAttribute("autoClose", true);
            return "vr-staff/vr-invoice";
        }

        Invoice invoiceDetails = invoiceService.getInvoiceDetails(invoiceNum);
        Supplier supplierDetails = supplierService.findByName(invoiceDetails.getSupplierName());
        Client clientDetails = clientService.findByName(invoiceDetails.getClientName());
        List<InvoiceProductInfo> invoicePurchaseProducts = invoiceService.getAllProdByInvNum(invoiceDetails.getInvoiceNum());
        CollectionReceiptInvoices collectionReceipt;
        CollectionReceipt collectionReceiptDetails;


        if (invoiceDetails.getStatus().equals("Unpaid")) {

            model.addAttribute("noCollectionReceipt", false);
            model.addAttribute("supplierDetails", supplierDetails);
            model.addAttribute("invoiceDetails", invoiceDetails);
            model.addAttribute("clientDetails", clientDetails);
            model.addAttribute("invoicePurchaseProducts", invoicePurchaseProducts);
        }
        else
        {
            collectionReceipt = collectionService.getCollectionReceiptId(invoiceDetails.getInvoiceNum());
            collectionReceiptDetails = collectionService.getCollectionReceiptDetails(collectionReceipt.getCollectionReceiptNum());
            model.addAttribute("collectionReceiptDetails", collectionReceiptDetails);
            model.addAttribute("collectionReceipt", collectionReceipt);
            model.addAttribute("supplierDetails", supplierDetails);
            model.addAttribute("invoiceDetails", invoiceDetails);
            model.addAttribute("clientDetails", clientDetails);
            model.addAttribute("invoicePurchaseProducts", invoicePurchaseProducts);
            model.addAttribute("noCollectionReceipt", true);
        }



        return "vr-staff/vr-invoice";
    }

}