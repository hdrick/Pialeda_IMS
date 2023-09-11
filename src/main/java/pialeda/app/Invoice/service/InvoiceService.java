package pialeda.app.Invoice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pialeda.app.Invoice.model.*;
import pialeda.app.Invoice.repository.*;
import pialeda.app.Invoice.dto.InvoiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class InvoiceService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceProdInfoRepository invoiceProdInfoRepository;
    @Autowired
    private CollectionRecptInvoicesRepo collectionRecptInvoicesRepo;
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CollectionRecptRepository collectionRecptRepository;

    @Autowired 
    private OfficialRecptRepository officialRecptRepository;
    @Autowired
    private OfficialRecptInvoicesRepo officialRecptInvoicesRepo;

    /**
     * Retrieves an Invoice by its ID.
     *
     * @param id The ID of the Invoice to retrieve.
     * @return An Optional containing the Invoice, or empty if not found.
     * @throws EntityNotFoundException If the Invoice with the specified ID is not found.
     */
    public Optional<Invoice> getInvoiceById(int id)
    {
        if (invoiceRepository.findById(id) == null )
        {
            throw new EntityNotFoundException();
        }
        Optional<Invoice> invoice = Optional.ofNullable(invoiceRepository.findById(id));
        return invoice;
    }
    /**
     * Retrieves all Invoices.
     *
     * @return A list of all Invoices.
     */
    public List<Invoice> getAllInvoice(){
        return invoiceRepository.findAll();
    }
    /**
     * Retrieves unpaid Invoices for a specific supplier and client.
     *
     * @param supplierName The name of the supplier.
     * @param clientName   The name of the client.
     * @return A list of unpaid Invoices.
     */
    public List<Invoice> getCRInvoice(String supplierName, String clientName)
    {
        // String unpaidStatus = "Unpaid";
        // return invoiceRepository.findBySupplierNameAndClientNameAndStatus(supplierName, clientName, unpaidStatus);
        return invoiceRepository.findBySupplierNameAndClientNameAndStatusInProgressAndUnPaid(clientName, supplierName);
    }
    /**
     * Retrieves unpaid Invoices for a specific supplier and client.
     *
     * @param supplierName The name of the supplier.
     * @param clientName   The name of the client.
     * @return A list of unpaid Invoices.
     */
    public List<Invoice> getORInvoice(String supplierName, String clientName)
    {
        return invoiceRepository.findBySupplierNameAndClientNameAndStatusInProgressAndUnPaid(clientName, supplierName);
    }
    /**
     * Retrieves a list of all invoices with an "Unpaid" status.
     *
     * @return A list of Invoice objects with an "Unpaid" status.
     */
    public List<Invoice> getAllInvoiceByStatusUnpaid(){
        return invoiceRepository.findInvoicesByStatusUnpaid();
    }
    /**
     * Searches for invoices containing the specified keyword in the client's name (case-insensitive).
     *
     * @param query The keyword to search for in the client's name.
     * @return A list of Invoice objects matching the keyword in the client's name.
     */
    public List<Invoice> getKeyword(String query)
    {
        return invoiceRepository.findByClientNameContainingIgnoreCase(query);
    }

    /**
     * Creates a new Invoice based on the provided InvoiceInfo and related information.
     *
     * @param invoiceInfo        The InvoiceInfo containing invoice details.
     * @param date               The date of invoice creation.
     * @param saleInvoiceDateStr The sale invoice date in string format.
     * @param qtyList            The list of quantities for invoice products.
     * @param unitList           The list of units for invoice products.
     * @param articlesList       The list of articles for invoice products.
     * @param unitPriceList      The list of unit prices for invoice products.
     * @param amountList         The list of amounts for invoice products.
     * @throws DateTimeParseException If date or saleInvoiceDateStr is not in the expected date format.
     */
    public void createInvoice(InvoiceInfo invoiceInfo, String date, String saleInvoiceDateStr, List<String> qtyList, List<String> unitList,
                            List<String> articlesList, List<String> unitPriceList, List<String> amountList){
                                Invoice invoice = new Invoice();     
        //Insert invoice info
        invoice.setInvoiceNum(invoiceInfo.getInvoiceNum());
        invoice.setPoNum(invoiceInfo.getPoNum());
        invoice.setDateCreated(LocalDate.parse(date));
        invoice.setSaleInvoiceDate(LocalDate.parse(saleInvoiceDateStr));
        invoice.setClientContactPerson(invoiceInfo.getClientContactPerson());

        invoice.setSupplierName(invoiceInfo.getSupplierName());
        invoice.setSupplierAddress(invoiceInfo.getSupplierAddress());
        invoice.setSupplierTin(invoiceInfo.getSupplierTin());
        invoice.setClientName(invoiceInfo.getClientName());
        invoice.setClientTin(invoiceInfo.getClientTin());
        invoice.setClientAddress(invoiceInfo.getClientAddress());
        invoice.setClientBusStyle(invoiceInfo.getClientBusStyle());
        invoice.setClientTerms(invoiceInfo.getClientTerms());

        invoice.setGrandTotal(invoiceInfo.getGrandTotal());
        invoice.setAddVat(invoiceInfo.getAddVat());
        invoice.setAmountNetOfVat(invoiceInfo.getAmountNetOfVat());
        invoice.setTotalSalesVatInc(invoiceInfo.getTotalSalesVatInc());
        invoice.setCashier(invoiceInfo.getCashier());     
        invoice.setStatus("Unpaid"); 
        Invoice savedInvoice = invoiceRepository.save(invoice);

        if(savedInvoice != null){
            List<InvoiceProductInfo> items = new ArrayList<>();
            for(int i = 0; i < qtyList.size(); i++){
                InvoiceProductInfo item = new InvoiceProductInfo();
                item.setQty(Integer.parseInt(qtyList.get(i)));
                item.setUnit(unitList.get(i));
                item.setArticles(articlesList.get(i));
                item.setUnitPrice(Double.parseDouble(unitPriceList.get(i)));
                item.setAmount(Double.parseDouble(amountList.get(i)));
                item.setInvoiceNumber(invoiceInfo.getInvoiceNum());
                item.setPurchaseOrderNumber(invoiceInfo.getPoNum());
                items.add(item);
                invoiceProdInfoRepository.saveAll(items);
            }
        }
        // if(savedInvoice != null){
        //     List<InvoiceProductInfo> items = new ArrayList<>();
        //     for(int i = 0; i < qtyList.size(); i++){
        //         InvoiceProductInfo item = new InvoiceProductInfo();
        //         item.setQty(Integer.parseInt(qtyList.get(i)));
        //         item.setUnit(unitList.get(i));
        //         item.setArticles(articlesList.get(i));
        //         item.setUnitPrice(Double.parseDouble(unitPriceList.get(i)));
        //         item.setAmount(Double.parseDouble(amountList.get(i)));
        //         item.setInvoiceNumber(invoiceInfo.getInvoiceNum());
        //         item.setPurchaseOrderNumber(invoiceInfo.getPoNum());
        //         items.add(item);
        //         invoiceProdInfoRepository.saveAll(items);
        //     }
        // }
    }

    public String formatTIN(String tin) {
        String formattedTIN = "";
        int length = tin.length();
        if (length == 9) {
            formattedTIN = tin.substring(0, 3) + "-" + tin.substring(3, 6) + "-" + tin.substring(6);
        } else if (length == 12) {
            formattedTIN = tin.substring(0, 3) + "-" + tin.substring(3, 6) + "-" + tin.substring(6, 9) + "-" + tin.substring(9);
        }
        return formattedTIN;
    }
    


    public void insertNotNullItem(int qty, String unit, String articles, double unitPrice, double amount,String invNum, String poNum){
        InvoiceProductInfo invoiceProduct = new InvoiceProductInfo();
        if(qty !=0 && !(unit.isEmpty() ||  articles.isEmpty()) && unitPrice!=0.0){
            invoiceProduct.setInvoiceNumber(invNum);
            invoiceProduct.setPurchaseOrderNumber(poNum);
            invoiceProduct.setQty(qty);
            invoiceProduct.setUnit(unit);
            invoiceProduct.setArticles(articles);
            invoiceProduct.setUnitPrice(unitPrice);
            invoiceProduct.setAmount(amount);

            invoiceProdInfoRepository.save(invoiceProduct);
        }
    }
    /**
     * Checks if an invoice with the specified invoice number exists in the repository.
     *
     * @param invNum The invoice number to check.
     * @return True if an invoice with the given invoice number exists, otherwise false.
     */
    public boolean checkInvoiceNum(String invNum){
        Invoice isExist = invoiceRepository.findByInvoiceNum(invNum);
        if(isExist == null){
            return false;
        }
        return true;
    }
    /**
     * Retrieves the total number of invoices in the repository.
     *
     * @return The total count of invoices.
     */
    public long getInvoiceCunt(){
        return invoiceRepository.count();
    }
    /**
     * Retrieves the total invoice limit for the specified supplier.
     *
     * @param suppName The name of the supplier.
     * @return The total invoice limit for the supplier.
     */
    public double getSuppTotalLimit(String suppName){
        return invoiceRepository.findSumLimitByName(suppName);
    }
    /**
     * Retrieves a paginated list of invoices.
     *
     * @param pageNumber The page number to retrieve (1-based).
     * @return A Page object containing a list of Invoice objects for the specified page.
     */
    public Page<Invoice> findPage(int pageNumber)
    {
        Pageable pageable = PageRequest.of(pageNumber -1, 7);
        return invoiceRepository.findAll(pageable);
    }

    /**
     * Retrieves a paginated list of invoices containing a specific keyword in the client name.
     *
     * @param keyword    The keyword to search for in the client name.
     * @param pageNumber The page number to retrieve (1-based).
     * @return A Page object containing a list of Invoice objects matching the keyword on the specified page.
     */
    public Page<Invoice> getPageByKeyword(String keyword, int pageNumber)
    {
        Pageable pageable = PageRequest.of(pageNumber -1, 7);
        return invoiceRepository.searchByKeyword(keyword, pageable);
    }

    /**
     * Retrieves a list of distinct client names associated with a supplier, sorted in ascending order.
     *
     * @param supplierName The name of the supplier.
     * @return A list of distinct client names associated with the supplier, sorted alphabetically.
     */
    public List<String> getClientOfSupplier(String supplierName)
    {
//        return invoiceRepository.findClientNameBySupplierName(supplierName);
        // Create a Sort object to specify the sorting criteria
        Sort sort = Sort.by(Sort.Direction.ASC, "clientName");

        // Call the repository method with the sorting criteria
        return invoiceRepository.findDistinctClientNameBySupplierName(supplierName, sort);
    }
    /**
     * Filters and retrieves a paginated list of invoices associated with a client's name, ignoring case.
     *
     * @param name       The client's name to filter by.
     * @param pageNumber The page number to retrieve (1-based).
     * @return A Page object containing a list of Invoice objects associated with the client's name on the specified page.
     */
    public Page<Invoice> filterPageByClient(String name, int pageNumber)
    {
        Pageable pageable = PageRequest.of(pageNumber -1, 7);
        return invoiceRepository.findByClientNameContainingIgnoreCase(name, pageable);
    }
    /**
     * Filters and retrieves a paginated list of invoices associated with a client's name and a specific month in their creation date, ignoring case.
     *
     * @param name       The client's name to filter by.
     * @param month      The month to filter by in the creation date.
     * @param pageNumber The page number to retrieve (1-based).
     * @return A Page object containing a list of Invoice objects associated with the client's name and the specified month on the specified page, sorted by date.
     */
    public Page<Invoice> filterPageByClientSortByMonth(String name, String month, int pageNumber)
    {
        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findByClientNameAndDateCreatedContainingIgnoreCase(name, month, pageable);
    }
    /**
     * Filters and retrieves a paginated list of invoices associated with a supplier's name, ignoring case.
     *
     * @param name       The supplier's name to filter by.
     * @param pageNumber The page number to retrieve (1-based).
     * @return A Page object containing a list of Invoice objects associated with the supplier's name on the specified page.
     */
    public Page<Invoice> filterPageBySupplier(String name, int pageNumber)
    {
        Pageable pageable = PageRequest.of(pageNumber -1, 7);
        return invoiceRepository.findBySupplierNameContainingIgnoreCase(name, pageable);
    }
    /**
     * Filters and retrieves a paginated list of invoices associated with a client's name and a supplier's name, ignoring case.
     *
     * @param client     The client's name to filter by.
     * @param supplier   The supplier's name to filter by.
     * @param pageNumber The page number to retrieve (1-based).
     * @return A Page object containing a list of Invoice objects associated with the client's name and the supplier's name on the specified page.
     */
    public Page<Invoice> filterPageByClientAndSupplier(String client, String supplier, int pageNumber)
    {
        Pageable pageable = PageRequest.of(pageNumber -1, 7);
        return invoiceRepository.findByClientNameAndSupplierNameContainingIgnoreCase(client, supplier, pageable);
    }
    /**
     * Filters and retrieves a paginated list of invoices associated with a client's name, a supplier's name, and a specific month in their creation date, ignoring case. The results are sorted by date in ascending order.
     *
     * @param client     The client's name to filter by.
     * @param supplier   The supplier's name to filter by.
     * @param month      The month to filter by in the creation date.
     * @param pageNumber The page number to retrieve (1-based).
     * @return A Page object containing a list of Invoice objects associated with the client's name, the supplier's name, and the specified month on the specified page, sorted by date in ascending order.
     */
    public Page<Invoice> filterPageByClientAndSupplierSortByMonth(String client, String supplier, String month, int pageNumber)
    {
        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findByClientNameAndSupplierNameAndDateCreatedContainingIgnoreCase(client, supplier, month, pageable);
    }
    /**
     * Retrieves a paginated list of invoices created in a specific month, ignoring case. The results are sorted by date in ascending order.
     *
     * @param month      The month to filter by in the creation date.
     * @param pageNumber The page number to retrieve (1-based).
     * @return A Page object containing a list of Invoice objects created in the specified month on the specified page, sorted by date in ascending order.
     */
    public Page<Invoice> sortByMonthAsc(String month, int pageNumber)
    {

        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findByDateCreatedContainingIgnoreCase(month, pageable);
    }
    /**
     * Retrieves a paginated list of invoices for a specific client within a date range. The results are sorted by the creation date in ascending order.
     *
     * @param name       The name of the client to filter by.
     * @param startDate  The start date of the date range.
     * @param endDate    The end date of the date range.
     * @param pageNumber The page number to retrieve (1-based).
     * @return A Page object containing a list of Invoice objects for the specified client within the specified date range, sorted by creation date in ascending order.
     */
    public Page<Invoice> filterPageByClientSortByDateRange(String name, LocalDate startDate, LocalDate endDate, int pageNumber)
    {
        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findByClientNameAndDateCreatedBetween(name, startDate, endDate, pageable);
    }
    /**
     * Retrieves a paginated list of invoices for a specific supplier within a date range. The results are sorted by the creation date in ascending order.
     *
     * @param name       The name of the supplier to filter by.
     * @param startDate  The start date of the date range.
     * @param endDate    The end date of the date range.
     * @param pageNumber The page number to retrieve (1-based).
     * @return A Page object containing a list of Invoice objects for the specified supplier within the specified date range, sorted by creation date in ascending order.
     */
    public Page<Invoice> filterPageBySupplierSortByDateRange(String name, LocalDate startDate, LocalDate endDate, int pageNumber)
    {
        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findBySupplierNameAndDateCreatedBetween(name, startDate, endDate, pageable);
    }
    /**
     * Retrieves a paginated list of invoices for a specific client and supplier within a date range. The results are sorted by the creation date in ascending order.
     *
     * @param client     The name of the client to filter by.
     * @param supplier   The name of the supplier to filter by.
     * @param startDate  The start date of the date range.
     * @param endDate    The end date of the date range.
     * @param pageNumber The page number to retrieve (1-based).
     * @return A Page object containing a list of Invoice objects for the specified client and supplier within the specified date range, sorted by creation date in ascending order.
     */
    public Page<Invoice> filterPageByClientAndSupplierSortByDateRange(String client, String supplier, LocalDate startDate, LocalDate endDate, int pageNumber)
    {
        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findByClientNameAndSupplierNameAndDateCreatedBetween(client, supplier, startDate, endDate, pageable);
    }
    /**
     * Retrieves the total amount of all invoices for a specific supplier.
     *
     * @param supplierName The name of the supplier.
     * @return The total amount of all invoices for the specified supplier.
     */
    public BigDecimal getTotalAmountBySupplierName(String supplierName) {
        return invoiceRepository.getSumOfAllInvoiceAmountsBySupplierName(supplierName);
    }
    /**
     * Retrieves the total amount of all official receipts for a specific supplier.
     *
     * @param supplierName The name of the supplier.
     * @return The total amount of all official receipts for the specified supplier.
     */
    public BigDecimal getTotalReceiptBySupplierName(String supplierName) {
        return officialRecptRepository.getSumOfAllOfficialReceiptAmountsBySupplierName(supplierName);
    }
    /**
     * Retrieves the total amount of all invoices for a specific client and supplier combination.
     *
     * @param clientName   The name of the client.
     * @param supplierName The name of the supplier.
     * @return The total amount of all invoices for the specified client and supplier combination.
     */
    public BigDecimal getTotalAmountByClientNameAndSupplierName(String clientName, String supplierName) {
        return invoiceRepository.getSumOfAllInvoiceAmountsByClientNameAndSupplierName(clientName, supplierName);
    }
    /**
     * Retrieves the total amount of all invoices for a specific supplier within a specified date range.
     *
     * @param supplierName The name of the supplier.
     * @param startDate    The start date of the date range.
     * @param endDate      The end date of the date range.
     * @return The total amount of all invoices for the specified supplier within the specified date range.
     */
    public BigDecimal getTotalAmountBySupplierNameBetweenDateRange(String supplierName, LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.sumOfGrandTotalBySupplierNameBetweenDateCreated(supplierName, startDate, endDate);
    }
    /**
     * Retrieves the total amount of all invoices for a specific client and supplier combination within a specified date range.
     *
     * @param clientName   The name of the client.
     * @param supplierName The name of the supplier.
     * @param startDate    The start date of the date range.
     * @param endDate      The end date of the date range.
     * @return The total amount of all invoices for the specified client and supplier combination within the specified date range.
     */
    public BigDecimal getTotalAmountByClientNameAndSupplierNameBetweenDateRange(String clientName, String supplierName, LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.sumOfGrandTotalByClientNameAndSupplierNameBetweenDateCreated(clientName, supplierName, startDate, endDate);
    }
    /**
     * Retrieves a list of all invoices for a specific supplier.
     *
     * @param supplierName The name of the supplier.
     * @return A list of all invoices for the specified supplier.
     */
    public List<Invoice> getAllInvoiceBySupplier(String supplierName)
    {
        return invoiceRepository.findBySupplierName(supplierName);
    }
    /**
     * Retrieves a list of all official receipts for a specific supplier.
     *
     * @param supplierName The name of the supplier.
     * @return A list of all official receipts for the specified supplier.
     */
    public List<OfficialReceipt> getSupplierOR(String supplierName)
    {
        return officialRecptRepository.findBySupplierName(supplierName);
    }

    /**
     * Retrieves a paginated list of invoices.
     *
     * @param currentPage The current page number (starting from 0).
     * @param size        The number of invoices to retrieve per page.
     * @return A page of invoices based on the specified pagination criteria.
     */
    public Page<Invoice> getInvoicesPaginated(int currentPage, int size){
        Pageable p = PageRequest.of(currentPage, size);
        return invoiceRepository.findAll(p);
    }
    /**
     * Retrieves an invoice by its unique identifier and populates its fields.
     *
     * @param id The unique identifier of the invoice to retrieve.
     * @return An invoice object with populated fields if found, or null if not found.
     */
    public Invoice getInvByIdAndFillFields(int id){
        return invoiceRepository.findById(id);
    }
    /**
     * Retrieves a list of all product information associated with a specific invoice.
     *
     * @param invNum The invoice number associated with the products.
     * @return A list of product information objects associated with the specified invoice.
     */
    public List<InvoiceProductInfo> getAllProdByInvNum(String invNum){
        return invoiceProdInfoRepository.findByInvoiceNumber(invNum);
    }
    /**
     * Updates an existing invoice and related information.
     *
     * @param invoiceNumber        The unique identifier of the invoice to update.
     * @param dateCreated          The new creation date for the invoice.
     * @param supplierName         The new supplier name for the invoice.
     * @param clientName           The new client name for the invoice.
     * @param clientContactPerson  The new client contact person for the invoice.
     * @param totalAmt             The new total amount for the invoice.
     * @param addVat               The new additional VAT amount for the invoice.
     * @param amtNetVat            The new amount net of VAT for the invoice.
     * @param totalSalesVatInc     The new total sales amount including VAT for the invoice.
     * @param qtyList              The new quantities for the invoice products.
     * @param unitList             The new unit values for the invoice products.
     * @param articlesList         The new article values for the invoice products.
     * @param unitPriceList        The new unit price values for the invoice products.
     * @param amountList           The new amount values for the invoice products.
     * @param prodIdList           The list of product IDs to identify which products to update.
     * @param clientTerms          The new client terms for the invoice.
     * @param clientBStyle         The new client business style for the invoice.
     * @return True if the invoice was successfully updated, false otherwise.
     */
    public boolean updateInvoices(String invoiceNumber, LocalDate dateCreated,
                                 String supplierName, String clientName, String clientContactPerson,
                                 String totalAmt, String addVat, String amtNetVat, String totalSalesVatInc, List<String> qtyList, List<String> unitList, List<String> articlesList,
                                 List<String> unitPriceList, List<String> amountList, List<String> prodIdList, String clientTerms, String clientBStyle){

        try {
            logger.info("Updating invoice with number: " + invoiceNumber);

               Invoice invDb = invoiceRepository.findByInvoiceNum(invoiceNumber);
               Supplier supplier = supplierRepository.findByName(supplierName);
               Client client = clientRepository.findByName(clientName);
               List<InvoiceProductInfo> productInfo = invoiceProdInfoRepository.findByInvoiceNumber(invoiceNumber);
               String totalAmtNew = totalAmt.replace(",", "");
               invDb.setDateCreated(dateCreated);

               invDb.setSupplierName(supplier.getName());
               invDb.setSupplierAddress(supplier.getAddress());
               invDb.setSupplierTin(supplier.getTin());

               invDb.setClientName(client.getName());
               invDb.setClientContactPerson(clientContactPerson);
               invDb.setClientBusStyle(clientBStyle);
               invDb.setClientTerms(clientTerms);
               invDb.setClientTin(client.getTin());
               invDb.setClientAddress(client.getAddress());

               //Update Official ReceiptInvoice Info if inv has or
            OfficialReceiptInvoices or = officialRecptInvoicesRepo.findByInvoiceNo(invoiceNumber);
            if (or != null) {
                logger.info("Found associated OfficialReceiptInvoices for invoice: " + invoiceNumber);
                or.setInvoiceAmount(Double.parseDouble(totalAmtNew));
                officialRecptInvoicesRepo.save(or);
                OfficialReceipt specfcOr = officialRecptRepository.findByOfficialReceiptNum(or.getOfficialReceiptNum());
                double total = Double.parseDouble(totalAmtNew);
                double totalResult = 0;

                if (specfcOr.getAddVat() != 0) {
                    // Update VAT
                    // Add VAT: 0.12 * total amount
                    // Net of Vat: total amount * 0.02
                    // totalResult = (totalAmount + addVat) - lwTax

                    double newAddVat = 0.12 * total;
                    double newNOV = total * 0.02;
                    totalResult = (total + newAddVat) - newNOV;

                    logger.info("Updating VAT-related fields for Official Receipt: " + specfcOr.getOfficialReceiptNum());
                    logger.info("Total Sales: " + total);
                    logger.info("Add VAT: " + newAddVat);
                    logger.info("Net of Vat: " + newNOV);
                    logger.info("Amount Due: " + totalResult);

                    specfcOr.setTotalSales(total);
                    specfcOr.setAddVat(newAddVat);
                    specfcOr.setLessWithHoldTax(newNOV);
                    specfcOr.setAmountDue(totalResult);

                    if (specfcOr.getEwt() != 0) {
                        // Calculate and update EWT
                        double ewt = specfcOr.getEwt();
                        double ewtPercentage = ewt / (specfcOr.getAmountDue() / 1.12);
                        double roundedEwtPercentage = Math.round(ewtPercentage * 100.0) / 100.0;
                        double newTotalAmount = totalResult;
                        double newEwt = (newTotalAmount / 1.12) * roundedEwtPercentage;
                        double newTotal = newTotalAmount - newEwt;

                        // Round newEwt and newTotal to 2 decimal places
                        newEwt = Math.round(newEwt * 100.0) / 100.0;
                        totalResult = Math.round(newTotal * 100.0) / 100.0;

                        logger.info("Updating EWT-related fields for Official Receipt: " + specfcOr.getOfficialReceiptNum());
                        logger.info("EWT Percentage: " + roundedEwtPercentage);
                        logger.info("New EWT: " + newEwt);
                        logger.info("New Total: " + newTotal);
                        logger.info("New Total Amount: " + newTotalAmount);

                        specfcOr.setEwt(newEwt);
                        specfcOr.setTotal(totalResult);
                        officialRecptRepository.save(specfcOr);
                    } else {
                        specfcOr.setTotal(totalResult);
                        officialRecptRepository.save(specfcOr);
                    }
                }

                if (specfcOr.getEwt() != 0) {
                    // Update EWT
                    double ewt = specfcOr.getEwt();
                    double ewtPercentage = ewt / (specfcOr.getAmountDue() / 1.12);
                    double roundedEwtPercentage = Math.round(ewtPercentage * 100.0) / 100.0;
                    totalResult = total;
                    double newTotalAmount = totalResult;
                    double newEwt = (newTotalAmount / 1.12) * roundedEwtPercentage;
                    double newTotal = newTotalAmount - newEwt;

                    // Round newEwt and newTotal to 2 decimal places
                    newEwt = Math.round(newEwt * 100.0) / 100.0;
                    totalResult = Math.round(newTotal * 100.0) / 100.0;

                    logger.info("Updating EWT-related fields for Official Receipt: " + specfcOr.getOfficialReceiptNum());
                    logger.info("EWT Percentage: " + roundedEwtPercentage);
                    logger.info("New EWT: " + newEwt);
                    logger.info("New Total: " + newTotal);
                    logger.info("New Total Amount: " + newTotalAmount);

                    specfcOr.setTotalSales(total);
                    specfcOr.setAmountDue(total);
                    specfcOr.setEwt(newEwt);
                    specfcOr.setTotal(totalResult);
                    officialRecptRepository.save(specfcOr);
                }

                if (specfcOr.getEwt() == 0 && specfcOr.getEwt() == 0) {
                    logger.info("No EWT and Add VAT in Official Receipt: " + specfcOr.getOfficialReceiptNum());
                    specfcOr.setTotalSales(total);
                    specfcOr.setTotal(total);
                    specfcOr.setAmountDue(total);
                }
            }
               invDb.setGrandTotal(Double.parseDouble(totalAmtNew));

               String addVatNew = addVat.replace(",", "");
               invDb.setAddVat(Double.parseDouble(addVatNew));

               String amtNetVatNew = amtNetVat.replace(",", "");
               invDb.setAmountNetOfVat(Double.parseDouble(amtNetVatNew));

               String totalSalesVatIncNew = totalSalesVatInc.replace(",", "");
               invDb.setTotalSalesVatInc(Double.parseDouble(totalSalesVatIncNew));

               // Update the product info for each product
               for (int i = 0; i < prodIdList.size(); i++) {
                   String prodId = prodIdList.get(i);
                   for (InvoiceProductInfo prodInfo : productInfo) {
                       if (String.valueOf(prodInfo.getId()).equals(prodId)) {
                           prodInfo.setQty(Integer.parseInt(qtyList.get(i)));
                           prodInfo.setUnit(unitList.get(i));
                           prodInfo.setArticles(articlesList.get(i));
                           prodInfo.setUnitPrice(Double.parseDouble(unitPriceList.get(i)));
                           prodInfo.setAmount(Double.parseDouble(amountList.get(i)));
                           break;
                       }
                   }
               }

               invoiceRepository.save(invDb);
               invoiceProdInfoRepository.saveAll(productInfo);

            logger.info("Invoice with number " + invoiceNumber + " updated successfully.");
            return true;
        } catch (Exception e) {
            logger.error("Error updating invoice with number: " + invoiceNumber, e);
            return false;
        }
    }

    /**
     * Retrieve details of an invoice by its invoice number.
     *
     * @param invoiceNum The invoice number to search for.
     * @return The Invoice object containing the details, or null if not found.
     */
    public Invoice getInvoiceDetails(String invoiceNum) {
        try {
            // Search for the invoice by its invoice number
            Invoice invoice = invoiceRepository.findByInvoiceNum(invoiceNum);

            if (invoice != null) {
                // Log an info message if the invoice is found
                logger.info("Invoice details retrieved successfully for invoice number: " + invoiceNum);
            } else {
                // Log an info message if the invoice is not found
                logger.info("Invoice details not found for invoice number: " + invoiceNum);
            }

            return invoice;
        } catch (Exception e) {
            // Log an error message if an exception occurs
            logger.error("Error retrieving invoice details for invoice number: " + invoiceNum, e);
            return null;
        }
    }

    /**
     * Find an invoice by its invoice number.
     *
     * @param invoiceNum The invoice number to search for.
     * @return The Invoice object if found, or null if not found.
     */
    public Invoice findByInvNum(String invoiceNum) {
        try {
            // Search for the invoice by its invoice number
            Invoice invoice = invoiceRepository.findByInvoiceNum(invoiceNum);

            if (invoice != null) {
                // Log an info message if the invoice is found
                logger.info("Invoice found for invoice number: " + invoiceNum);
            } else {
                // Log an info message if the invoice is not found
                logger.info("Invoice not found for invoice number: " + invoiceNum);
            }

            return invoice;
        } catch (Exception e) {
            // Log an error message if an exception occurs
            logger.error("Error finding invoice by invoice number: " + invoiceNum, e);
            return null;
        }
    }

    /**
     * Duplicate a specific invoice with checks for existing invoice numbers and purchase order numbers.
     *
     * @param id       The ID of the specific invoice to duplicate.
     * @param invoiceNum The new invoice number for the duplicated invoice.
     * @param poNum      The new purchase order number for the duplicated invoice.
     * @param date       The creation date of the duplicated invoice.
     * @param si_date    The sale invoice date of the duplicated invoice.
     * @return A status message indicating the result of the duplication process.
     */
    public String duplicateSpecificInvoice(int id, String invoiceNum, String poNum, String date, String si_date) {
        try {
            // Retrieve the content of the specific invoice to duplicate
            Invoice invoiceToDuplicate = invoiceRepository.findById(id);

            // Create a new invoice for duplication
            Invoice newInvoice = new Invoice();

            // Check if the invoice number already exists in the database
            Invoice ifInvExist = findByInvNum(invoiceNum);

            if (ifInvExist == null) {
                // Check if the purchase order number already exists
                Invoice ifPOExist = invoiceRepository.findByPoNum(poNum);

                if (ifPOExist == null) {
                    // Set properties for the new invoice based on the original invoice
                    newInvoice.setInvoiceNum(invoiceNum);
                    newInvoice.setPoNum(poNum);
                    newInvoice.setDateCreated(LocalDate.parse(date));
                    newInvoice.setSaleInvoiceDate(LocalDate.parse(si_date));

                    newInvoice.setClientContactPerson(invoiceToDuplicate.getClientContactPerson());
                    newInvoice.setSupplierName(invoiceToDuplicate.getSupplierName());
                    newInvoice.setSupplierAddress(invoiceToDuplicate.getSupplierAddress());
                    newInvoice.setSupplierTin(invoiceToDuplicate.getSupplierTin());

                    newInvoice.setClientName(invoiceToDuplicate.getClientName());
                    newInvoice.setClientTin(invoiceToDuplicate.getClientTin());
                    newInvoice.setClientAddress(invoiceToDuplicate.getClientAddress());
                    newInvoice.setClientBusStyle(invoiceToDuplicate.getClientBusStyle());
                    newInvoice.setClientTerms(invoiceToDuplicate.getClientTerms());

                    newInvoice.setGrandTotal(invoiceToDuplicate.getGrandTotal());
                    newInvoice.setAddVat(invoiceToDuplicate.getAddVat());
                    newInvoice.setAmountNetOfVat(invoiceToDuplicate.getAmountNetOfVat());
                    newInvoice.setTotalSalesVatInc(invoiceToDuplicate.getTotalSalesVatInc());
                    newInvoice.setCashier(invoiceToDuplicate.getCashier());
                    newInvoice.setStatus("Unpaid");

                    // Save the new invoice
                    invoiceRepository.save(newInvoice);
                    // Log an info message after setting the properties for the new invoice
                    logger.info("Properties set for the new invoice with number: " + invoiceNum);

                    // Duplicate invoice product information
                    List<InvoiceProductInfo> invProd = invoiceProdInfoRepository.findByInvoiceNumber(invoiceToDuplicate.getInvoiceNum());
                    List<InvoiceProductInfo> duplicatedInvProd = new ArrayList<>();

                    for (InvoiceProductInfo invoiceProductInfo : invProd) {
                        InvoiceProductInfo duplicatedInvoiceProductInfo = new InvoiceProductInfo();
                        duplicatedInvoiceProductInfo.setInvoiceNumber(invoiceNum); // Set the new invoice number
                        duplicatedInvoiceProductInfo.setPurchaseOrderNumber(poNum); // Set the new purchase order number
                        // Copy other properties from the original object
                        duplicatedInvoiceProductInfo.setAmount(invoiceProductInfo.getAmount());
                        duplicatedInvoiceProductInfo.setArticles(invoiceProductInfo.getArticles());
                        duplicatedInvoiceProductInfo.setQty(invoiceProductInfo.getQty());
                        duplicatedInvoiceProductInfo.setUnit(invoiceProductInfo.getUnit());
                        duplicatedInvoiceProductInfo.setUnitPrice(invoiceProductInfo.getUnitPrice());
                        // Add the duplicated object to the list
                        duplicatedInvProd.add(duplicatedInvoiceProductInfo);
                    }

                    // Save the duplicated invoice product information
                    invoiceProdInfoRepository.saveAll(duplicatedInvProd);
                    // Log an info message when the invoice duplication is successful
                    logger.info("Invoice '" + invoiceNum + "' duplicated successfully.");
                    return "success";
                } else {
                    // Log an info message when the purchase number is found
                    logger.info("Purchase order number '" + poNum + "' already exists.");
                    return "purchaseNumberFound";
                }
            } else {
                // Log an info message when the invoice number is found
                logger.info("Invoice number '" + invoiceNum + "' already exists.");
                return "invoiceNumberFound";
            }
        } catch (Exception e) {
            // Log an error message if an exception occurs during the duplication process
            logger.error("Error duplicating invoice with ID: " + id, e);
            return "error";
        }
    }
}
