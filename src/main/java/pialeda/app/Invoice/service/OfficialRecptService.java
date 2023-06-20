package pialeda.app.Invoice.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import pialeda.app.Invoice.dto.CollectionReceiptInfo;
import pialeda.app.Invoice.dto.OfficialReceiptInfo;
import pialeda.app.Invoice.model.Invoice;
import pialeda.app.Invoice.model.OfficialReceipt;
import pialeda.app.Invoice.model.OfficialReceiptInvoices;
import pialeda.app.Invoice.repository.InvoiceRepository;
import pialeda.app.Invoice.repository.OfficialRecptInvoicesRepo;
import pialeda.app.Invoice.repository.OfficialRecptRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;


@Service
public class OfficialRecptService {
    @Autowired
    private OfficialRecptRepository officialRecptRepository;

    @Autowired
    private OfficialRecptInvoicesRepo officialRecptInvoicesRepo;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public void createOR(int orNum,
                         String totalSales,
                         String addVat,
                         String lwTax,
                         String amtDue,
                         String ewt,
                         String total,
                         String cash,
                         String chckNo,
                         String orAmount,
                         String cashierName,
                         Map<String, String> requestParams,
                         OfficialReceiptInfo orDTO){
        OfficialReceipt or = new OfficialReceipt();
                            

        List<OfficialReceiptInvoices> items = new ArrayList<>();                                

        for (int i = 1; i <= 8; i++) {
            String invoice = requestParams.get("inv" + i);
            String amount = requestParams.get("inv" + i + "-amt");
            if (invoice != null && !invoice.isEmpty()) {
                OfficialReceiptInvoices orInv = new OfficialReceiptInvoices();
                Invoice inv = invoiceRepository.findByInvoiceNum(invoice);
                
                inv.setStatus("Paid");
                invoiceRepository.save(inv);

                orInv.setInvoiceNo(invoice);
                orInv.setInvoiceAmount(formatStringToDouble(amount));
                orInv.setOfficialReceiptNum(orNum);
                items.add(orInv);
            }
        }
        officialRecptInvoicesRepo.saveAll(items);
 
        or.setSupplierName(orDTO.getSupplierName());
        or.setSupplierAddress(orDTO.getSupplierAddress());
        or.setOfficialReceiptNum(orNum);
        or.setSupplierTin(orDTO.getSupplierTin());

        or.setTotalSales(formatStringToDouble(totalSales));

        if(!(addVat.equals(null) || addVat.isEmpty())){
            or.setAddVat(formatStringToDouble(addVat));
        }
        if(!(lwTax.equals(null) || lwTax.isEmpty())){
            or.setLessWithHoldTax(formatStringToDouble(lwTax));
        }
        or.setAmountDue(formatStringToDouble(amtDue));
        if(!(ewt.equals(null) || ewt.isEmpty())){
            or.setEwt(formatStringToDouble(ewt));
        }
        if(total.equals(null) || total.isEmpty()){
            or.setTotal(formatStringToDouble(amtDue));
        }else{
            or.setTotal(formatStringToDouble(total));
        }
        or.setCash(cash);
        if(chckNo.equals(null) || chckNo.isEmpty()){
            or.setCheckNo("NA");
        }else{
            or.setCheckNo(chckNo);
        }
        
        or.setAmount(formatStringToDouble(orAmount));

        or.setRecvFrom(orDTO.getRecvFrom());
        or.setOfficialReceiptDate(orDTO.getOfficialReceiptDate());
        or.setClientAddress(orDTO.getClientAddress());
        or.setClientTin(orDTO.getClientTin());
        or.setClientBus(orDTO.getClientBus());
        or.setClientSumOf(orDTO.getClientSumOf());
        or.setClientPayment(formatStringToDouble(orDTO.getClientPayment()));
        or.setPartialPaymentFor(orDTO.getPartialPaymentFor());
        or.setCashierName(cashierName);
        
        


        this.officialRecptRepository.save(or);
    }

    public double formatStringToDouble(String num){
        String numberString = num;
        String numberWithoutComma = numberString.replaceAll(",", "");
        double number = Double.parseDouble(numberWithoutComma);
        return number;
    }

    public long getOrCount(){
        return officialRecptRepository.count();
    }

    public void createCR(int orNumber, String amtDue, String ewt, String total, String cash, String chckNo,
            String crAmount, String cashierName, Map<String, String> requestParams,
            CollectionReceiptInfo collectionReceiptInfo) {
    }

    public Page<OfficialReceipt> getOfficialReceiptPaginated(int currentPage, int size){
        Pageable p = PageRequest.of(currentPage, size);
        return officialRecptRepository.findAll(p);
    }

    public OfficialReceipt getSpecificOfficialReceipt(int id){
        return officialRecptRepository.findById(id);
    }

    public List<OfficialReceiptInvoices> getInvoiceByOrNum(int orNum ){
        return officialRecptInvoicesRepo.findByOfficialReceiptNum(orNum);
    }

    public OfficialReceipt getOrNumByOrNum(int orNum){
        return officialRecptRepository.findByOfficialReceiptNum(orNum);
    }

    public Boolean updateOr(int id, String clientName, String clientAddress,
                         String clientTIN, String busStyle,
                         String wPayment, Double nPayment,
                         String partialP, String suppName,
                         String suppAddrs, String suppTIN,
                         String cash, String chckNo,
                         Double amount, String orDate){
    
         OfficialReceipt or= officialRecptRepository.findById(id);
        
         if(or != null){
            or.setRecvFrom(clientName);
            or.setClientAddress(clientAddress);
            or.setClientTin(clientTIN);
            or.setClientBus(busStyle);
            or.setClientSumOf(wPayment);
            or.setClientPayment(nPayment);
            or.setPartialPaymentFor(partialP);
            or.setSupplierName(suppName);
            or.setSupplierAddress(suppAddrs);
            or.setSupplierTin(suppTIN);
            or.setCash(cash);
            or.setCheckNo(chckNo);
            or.setAmount(amount);
            or.setOfficialReceiptDate(orDate);
   
            officialRecptRepository.save(or);
            return true;
         }else{
            return false;
         } 
    }
}
