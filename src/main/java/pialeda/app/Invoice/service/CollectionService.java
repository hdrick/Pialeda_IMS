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

@Service
public class CollectionService {
    @Autowired
    private CollectionRecptRepository collectionRecptRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private CollectionRecptInvoicesRepo collectionRecptInvoicesRepo;


    public void createCR(int orNum,
                         String amtDue,
                         String ewt,
                         String total,
                         String cash,
                         String chckNo,
                         String crAmount,
                         String cashierName,
                         Map<String, String> requestParams,
                         CollectionReceiptInfo crDTO){
        CollectionReceipt cr = new CollectionReceipt();                       

        List<CollectionReceiptInvoices> items = new ArrayList<>();                                

        for (int i = 1; i <= 8; i++) {
            String invoice = requestParams.get("inv" + i);
            String amount = requestParams.get("inv" + i + "-amt");
            if (invoice != null && !invoice.isEmpty()) {
                CollectionReceiptInvoices crInv = new CollectionReceiptInvoices();
                // Invoice invoiceObject = invoiceRepository.findByInvoiceNum(invoice);

                Invoice inv = invoiceRepository.findByInvoiceNum(invoice);
                
                inv.setStatus("In Progress");
                invoiceRepository.save(inv);

                crInv.setInvoice(invoice);
                // crInv.setInvoice(invoiceObject);
                crInv.setInvoiceAmount(formatStringToDouble(amount));
                crInv.setCollectionReceiptNum(orNum);
                items.add(crInv);
            }
        }
        collectionRecptInvoicesRepo.saveAll(items);
 
        cr.setSupplierName(crDTO.getSupplierName());
        cr.setSupplierAddress(crDTO.getSupplierAddress());
        cr.setCollectionReceiptNum(orNum);
        cr.setSupplierTin(crDTO.getSupplierTin());

        cr.setAmountDue(formatStringToDouble(amtDue));
        if(!(ewt.equals(null) || ewt.isEmpty())){
            cr.setEwt(formatStringToDouble(ewt));
        }
        cr.setTotal(formatStringToDouble(total));
        cr.setCash(cash);
        if(chckNo.equals(null) || chckNo.isEmpty()){
            cr.setCheckNo("NA");
        }else{
            cr.setCheckNo(chckNo);
        }
        cr.setAmount(formatStringToDouble(crAmount));

        cr.setRecvFrom(crDTO.getRecvFrom());
        cr.setCollectionReceiptDate(crDTO.getOfficialReceiptDate());
        cr.setClientAddress(crDTO.getClientAddress());
        cr.setClientTin(crDTO.getClientTin());
        cr.setClientBus(crDTO.getClientBus());
        cr.setClientSumOf(crDTO.getClientSumOf());
        cr.setClientPayment(formatStringToDouble(crDTO.getClientPayment()));
        cr.setPartialPaymentFor(crDTO.getPartialPaymentFor());
        cr.setCashierName(cashierName);
        this.collectionRecptRepository.save(cr);
    }

    public double formatStringToDouble(String num){
        String numberString = num;
        String numberWithoutComma = numberString.replaceAll(",", "");
        double number = Double.parseDouble(numberWithoutComma);
        return number;
    }

    public Page<CollectionReceipt> getCollectionReceiptPaginated(int currentPage, int size){
        Pageable p = PageRequest.of(currentPage, size);
        return collectionRecptRepository.findAll(p);
    }

    public long getCrCount(){
        return collectionRecptRepository.count();
    }

    public CollectionReceipt getSpecificCollectionReceipt(int id){
        return collectionRecptRepository.findById(id);
    }
    public List<CollectionReceiptInvoices> getInvoiceByCrNum(int orNum ){
        return collectionRecptInvoicesRepo.findByCollectionReceiptNum(orNum);
    }

    public Boolean updateCr(int id, String clientName, String clientAddress,
                            String clientTIN, String busStyle,
                            String wPayment, Double nPayment,
                            String partialP, String suppName,
                            String suppAddrs, String suppTIN,
                            String cash, String chckNo,
                            Double amount, String orDate){
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
            return true;
         }else{
            return false;
         }                      
    }

//    public long countInvoicesCr(long crId)
//    {
//        return collectionRecptInvoicesRepo.findByCollectionReceiptNumCountAll(crId);
//    }

    public CollectionReceiptInvoices getCollectionReceiptId(String invoiceNum)
    {
        return collectionRecptInvoicesRepo.findByInvoice(invoiceNum);
    }

    public List<CollectionReceiptInvoices> getAllInvoicesByCollectionReceiptId(int crNum) {
        return collectionRecptInvoicesRepo.findAllByCollectionReceiptNum(crNum);
    }
    public List<CollectionReceiptInvoices> getAllInvoicesByCollectionReceipt(int crNum) {
        return collectionRecptInvoicesRepo.findAllByCollectionReceiptNum(crNum);
    }
    public CollectionReceipt getCollectionReceiptDetails(int crNum)
    {
        return collectionRecptRepository.findByCollectionReceiptNum(crNum);
    }

}
