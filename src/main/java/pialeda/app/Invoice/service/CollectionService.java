package pialeda.app.Invoice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pialeda.app.Invoice.dto.CollectionReceiptInfo;
import pialeda.app.Invoice.model.CollectionReceipt;
import pialeda.app.Invoice.model.CollectionReceiptInvoices;
import pialeda.app.Invoice.model.Invoice;
import pialeda.app.Invoice.repository.CollectionRecptInvoicesRepo;
import pialeda.app.Invoice.repository.CollectionRecptRepository;
import pialeda.app.Invoice.repository.InvoiceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for handling collection-related operations.
 */
@Service
public class CollectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionService.class);

    @Autowired
    private CollectionRecptRepository collectionRecptRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private CollectionRecptInvoicesRepo collectionRecptInvoicesRepo;

    /**
     * Create a new collection receipt and update associated invoices.
     *
     * @param orNum         The official receipt number.
     * @param amtDue        The amount due.
     * @param ewt           The EWT (Expanded Withholding Tax).
     * @param total         The total amount.
     * @param cash          The cash payment.
     * @param chckNo        The check number.
     * @param cashierName   The cashier's name.
     * @param requestParams The request parameters containing invoice information.
     * @param crDTO         The CollectionReceiptInfo DTO.
     */
    public void createCR(long crNum,
                         String amtDue,
                         String ewt,
                         String total,
                         String cash,
                         String chckNo,
                         String cashierName,
                         Map<String, String> requestParams,
                         CollectionReceiptInfo crDTO) {


        try {
            CollectionReceipt cr = new CollectionReceipt();

            String formattedString = total.replace(",", "");

            List<CollectionReceiptInvoices> items = new ArrayList<>();
            double converted_Total = Double.parseDouble(formattedString);
            double roundedAmount = Math.round(converted_Total * 100.0) / 100.0;
            for (int i = 1; i <= 8; i++) {
                String invoice = requestParams.get("inv" + i);
                String amount = requestParams.get("inv" + i + "-amt");

                if (invoice != null && !invoice.isEmpty()) {
                    CollectionReceiptInvoices crInv = new CollectionReceiptInvoices();

                    Invoice inv = invoiceRepository.findByInvoiceNum(invoice);
                    double clientPaymentTotal = inv.getClientPayment();
                    double clientSumOfPayment = clientPaymentTotal + roundedAmount;

                    LOGGER.info("SUM OF CLIENT PAYMENT: {}", clientPaymentTotal);
                    LOGGER.info("roundedAmount: {}", roundedAmount);

                    try {
                        if (clientPaymentTotal < 1) { // first time to pay if cp is 0
                            if (roundedAmount < converted_Total) {
                                inv.setStatus("In Progress");
                                inv.setClientPayment(clientSumOfPayment);
                                invoiceRepository.save(inv);
                                LOGGER.info("Invoice {} status updated to 'In Progress'.", invoice);
                            }
                        } else {
                            LOGGER.info("clientSumOfPayment: {}", clientSumOfPayment);
                            if (clientSumOfPayment >= converted_Total) {
                                inv.setStatus("Paid");
                                inv.setClientPayment(clientSumOfPayment);
                                invoiceRepository.save(inv);
                                LOGGER.info("Invoice {} status updated to 'Paid'.", invoice);
                            } else {
                                inv.setStatus("In Progress");
                                inv.setClientPayment(clientSumOfPayment);
                                invoiceRepository.save(inv);
                                LOGGER.info("Invoice {} status updated to 'In Progress'.", invoice);
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("Error while updating invoice {}: {}", invoice, e.getMessage(), e);
                    }

                    crInv.setInvoice(invoice);
                    crInv.setInvoiceAmount(formatStringToDouble(amount));
                    crInv.setCollectionReceiptNum(crNum);
                    items.add(crInv);
                }
            }

            try {
                collectionRecptInvoicesRepo.saveAll(items);
                LOGGER.info("Collection Receipt Invoices saved successfully.");
            } catch (Exception e) {
                LOGGER.error("Error while saving Collection Receipt Invoices: {}", e.getMessage(), e);
            }

            cr.setSupplierName(crDTO.getSupplierName());
            cr.setSupplierAddress(crDTO.getSupplierAddress());
            cr.setCollectionReceiptNum(crNum);
            cr.setSupplierTin(crDTO.getSupplierTin());

            cr.setAmountDue(formatStringToDouble(amtDue));
            if (!(ewt == null || ewt.isEmpty())) {
                cr.setEwt(formatStringToDouble(ewt));
            }
            cr.setTotal(formatStringToDouble(total));
            cr.setCash(cash);
            if (chckNo == null || chckNo.isEmpty()) {
                cr.setCheckNo("NA");
            } else {
                cr.setCheckNo(chckNo);
            }
            cr.setAmount(formatStringToDouble(total));

            cr.setRecvFrom(crDTO.getRecvFrom());
            cr.setCollectionReceiptDate(crDTO.getOfficialReceiptDate());
            cr.setClientAddress(crDTO.getClientAddress());
            cr.setClientTin(crDTO.getClientTin());
            cr.setClientBus(crDTO.getClientBus());
            cr.setClientSumOf(crDTO.getClientSumOf());
            cr.setClientPayment(formatStringToDouble(crDTO.getClientPayment()));
            cr.setPartialPaymentFor(crDTO.getPartialPaymentFor());
            cr.setCashierName(cashierName);

            try {
                this.collectionRecptRepository.save(cr);
                LOGGER.info("Collection Receipt saved successfully.");
            } catch (Exception e) {
                LOGGER.error("Error while saving Collection Receipt: {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            LOGGER.error("Error while processing Collection Receipt: {}", e.getMessage(), e);
        }
    }
    /**
     * Format a string to a double value.
     *
     * @param num The string representation of a number.
     * @return The double value.
     */
    public double formatStringToDouble(String num) {
        try {
            String numberString = num;
            String numberWithoutComma = numberString.replaceAll(",", "");
            double number = Double.parseDouble(numberWithoutComma);
            LOGGER.info("Successfully formatted string '{}' to double: {}", num, number);
            return number;
        } catch (NumberFormatException e) {
            LOGGER.error("Error while formatting string '{}' to double: {}", num, e.getMessage(), e);
            throw e; // You may choose to handle or rethrow the exception as needed.
        }
    }

    /**
     * Retrieve a paginated list of collection receipts.
     *
     * @param currentPage The current page number.
     * @param size        The number of items per page.
     * @return A Page containing CollectionReceipt objects.
     */
    public Page<CollectionReceipt> getCollectionReceiptPaginated(int currentPage, int size){
        try {
            Pageable p = PageRequest.of(currentPage, size);
            Page<CollectionReceipt> result = collectionRecptRepository.findAll(p);
            LOGGER.info("Retrieved paginated collection receipts - Page {}, Size {}", currentPage, size);
            return result;
        } catch (Exception e) {
            LOGGER.error("Error while retrieving paginated collection receipts: {}", e.getMessage(), e);
            throw e; // You may choose to handle or rethrow the exception as needed.
        }
    }

    /**
     * Get the count of collection receipts.
     *
     * @return The count of collection receipts.
     */
    public long getCrCount(){
        try {
            long count = collectionRecptRepository.count();
            LOGGER.info("Retrieved count of collection receipts: {}", count);
            return count;
        } catch (Exception e) {
            LOGGER.error("Error while retrieving count of collection receipts: {}", e.getMessage(), e);
            throw e; // You may choose to handle or rethrow the exception as needed.
        }
    }

    /**
     * Get a specific CollectionReceipt by its ID.
     *
     * @param id The ID of the CollectionReceipt to retrieve.
     * @return The CollectionReceipt if found, or null if not found.
     */
    public CollectionReceipt getSpecificCollectionReceipt(int id){
        try {
            CollectionReceipt collectionReceipt = collectionRecptRepository.findById(id);
            if (collectionReceipt != null) {
                LOGGER.info("Retrieved CollectionReceipt with ID: {}", id);
            } else {
                LOGGER.info("CollectionReceipt with ID {} not found", id);
            }
            return collectionReceipt;
        } catch (Exception e) {
            LOGGER.error("Error while retrieving CollectionReceipt with ID {}: {}", id, e.getMessage(), e);
            throw e; // You may choose to handle or rethrow the exception as needed.
        }
    }
    /**
     * Retrieve a list of CollectionReceiptInvoices by collection receipt number.
     *
     * @param orNum The collection receipt number.
     * @return A list of CollectionReceiptInvoices associated with the given collection receipt.
     */
    public List<CollectionReceiptInvoices> getInvoiceByCrNum(long orNum ){
        try {
            List<CollectionReceiptInvoices> invoices = collectionRecptInvoicesRepo.findByCollectionReceiptNum(orNum);
            if (!invoices.isEmpty()) {
                LOGGER.info("Retrieved {} invoices for CollectionReceipt with number: {}", invoices.size(), orNum);
            } else {
                LOGGER.info("No invoices found for CollectionReceipt with number: {}", orNum);
            }
            return invoices;
        } catch (Exception e) {
            LOGGER.error("Error while retrieving invoices for CollectionReceipt with number {}: {}", orNum, e.getMessage(), e);
            throw e; // You may choose to handle or rethrow the exception as needed.
        }
    }

    /**
     * Retrieve a CollectionReceipt by its collection receipt number.
     *
     * @param crNum The collection receipt number.
     * @return The CollectionReceipt if found, or null if not found.
     */
    public CollectionReceipt getCrNumByCrNum(long crNum){
        return collectionRecptRepository.findByCollectionReceiptNum(crNum);
    }

    /**
     * Update a CollectionReceipt with new information.
     *
     * @param id          The ID of the CollectionReceipt to update.
     * @param clientName  The new client name.
     * @param clientAddress The new client address.
     * @param clientTIN   The new client TIN (Tax Identification Number).
     * @param busStyle    The new business style.
     * @param wPayment    The new client sum of payments.
     * @param nPayment    The new client payment amount.
     * @param partialP    The new partial payment information.
     * @param suppName    The new supplier name.
     * @param suppAddrs   The new supplier address.
     * @param suppTIN     The new supplier TIN.
     * @param cash        The new cash amount.
     * @param chckNo      The new check number.
     * @param amount      The new amount.
     * @param orDate      The new official receipt date.
     * @return True if the update is successful, false otherwise.
     */
    public Boolean updateCr(int id, String clientName, String clientAddress,
                            String clientTIN, String busStyle,
                            String wPayment, Double nPayment,
                            String partialP, String suppName,
                            String suppAddrs, String suppTIN,
                            String cash, String chckNo,
                            Double amount, String orDate){
        try {
            CollectionReceipt cr = collectionRecptRepository.findById(id);

            if(cr != null){
                cr.setRecvFrom(clientName);
                cr.setClientAddress(clientAddress);
                cr.setClientTin(clientTIN);
                cr.setClientBus(busStyle);
                cr.setClientSumOf(wPayment);
                cr.setClientPayment(nPayment);
                cr.setPartialPaymentFor(partialP);
                cr.setSupplierName(suppName);
                cr.setSupplierAddress(suppAddrs);
                cr.setSupplierTin(suppTIN);
                cr.setCash(cash);
                cr.setCheckNo(chckNo);
                cr.setAmount(amount);
                cr.setCollectionReceiptDate(orDate);

                collectionRecptRepository.save(cr);
                LOGGER.info("CollectionReceipt with ID {} updated successfully.", id);
                return true;
            }else{
                LOGGER.error("CollectionReceipt with ID {} not found. Update failed.", id);
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Error while updating CollectionReceipt with ID {}: {}", id, e.getMessage(), e);
            throw e; // You may choose to handle or rethrow the exception as needed.
        }
    }

//    public long countInvoicesCr(long crId)
//    {
//        return collectionRecptInvoicesRepo.findByCollectionReceiptNumCountAll(crId);
//    }

    /**
     * Retrieve a CollectionReceiptInvoices by invoice number.
     *
     * @param invoiceNum The invoice number to search for.
     * @return The CollectionReceiptInvoices if found, or null if not found.
     */
    public CollectionReceiptInvoices getCollectionReceiptId(String invoiceNum)
    {
        try {
            CollectionReceiptInvoices collectionReceiptInvoices = collectionRecptInvoicesRepo.findByInvoice(invoiceNum);
            if (collectionReceiptInvoices != null) {
                LOGGER.info("CollectionReceiptInvoices found for invoice number: {}", invoiceNum);
            } else {
                LOGGER.info("CollectionReceiptInvoices not found for invoice number: {}", invoiceNum);
            }
            return collectionReceiptInvoices;
        } catch (Exception e) {
            LOGGER.error("Error while retrieving CollectionReceiptInvoices for invoice number {}: {}", invoiceNum, e.getMessage(), e);
            throw e; // You may choose to handle or rethrow the exception as needed.
        }
    }

    /**
     * Retrieve a list of CollectionReceiptInvoices by collection receipt number.
     *
     * @param crNum The collection receipt number.
     * @return A list of CollectionReceiptInvoices associated with the given collection receipt.
     */
    public List<CollectionReceiptInvoices> getAllInvoicesByCollectionReceiptId(long crNum) {
        try {
            List<CollectionReceiptInvoices> invoices = collectionRecptInvoicesRepo.findAllByCollectionReceiptNum(crNum);
            if (!invoices.isEmpty()) {
                LOGGER.info("Found {} CollectionReceiptInvoices for collection receipt number: {}", invoices.size(), crNum);
            } else {
                LOGGER.info("No CollectionReceiptInvoices found for collection receipt number: {}", crNum);
            }
            return invoices;
        } catch (Exception e) {
            LOGGER.error("Error while retrieving CollectionReceiptInvoices for collection receipt number {}: {}", crNum, e.getMessage(), e);
            throw e; // You may choose to handle or rethrow the exception as needed.
        }
    }

    /**
     * Retrieve a list of CollectionReceiptInvoices by collection receipt number.
     *
     * @param crNum The collection receipt number.
     * @return A list of CollectionReceiptInvoices associated with the given collection receipt.
     */
    public List<CollectionReceiptInvoices> getAllInvoicesByCollectionReceipt(long crNum) {
        try {
            List<CollectionReceiptInvoices> invoices = collectionRecptInvoicesRepo.findAllByCollectionReceiptNum(crNum);
            if (!invoices.isEmpty()) {
                LOGGER.info("Found {} CollectionReceiptInvoices for collection receipt number: {}", invoices.size(), crNum);
            } else {
                LOGGER.info("No CollectionReceiptInvoices found for collection receipt number: {}", crNum);
            }
            return invoices;
        } catch (Exception e) {
            LOGGER.error("Error while retrieving CollectionReceiptInvoices for collection receipt number {}: {}", crNum, e.getMessage(), e);
            throw e; // You may choose to handle or rethrow the exception as needed.
        }
    }

    /**
     * Retrieve detailed information about a CollectionReceipt by its collection receipt number.
     *
     * @param crNum The collection receipt number.
     * @return The detailed CollectionReceipt information if found, or null if not found.
     */
    public CollectionReceipt getCollectionReceiptDetails(long crNum) {
        try {
            CollectionReceipt collectionReceipt = collectionRecptRepository.findByCollectionReceiptNum(crNum);
            if (collectionReceipt != null) {
                LOGGER.info("Found CollectionReceipt for collection receipt number {}: {}", crNum, collectionReceipt);
            } else {
                LOGGER.info("No CollectionReceipt found for collection receipt number: {}", crNum);
            }
            return collectionReceipt;
        } catch (Exception e) {
            LOGGER.error("Error while retrieving CollectionReceipt for collection receipt number {}: {}", crNum, e.getMessage(), e);
            throw e; // You may choose to handle or rethrow the exception as needed.
        }
    }

}
