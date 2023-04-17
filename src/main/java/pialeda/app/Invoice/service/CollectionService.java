package pialeda.app.Invoice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pialeda.app.Invoice.dto.CollectionReceiptInfo;
import pialeda.app.Invoice.model.CollectionReceipt;
import pialeda.app.Invoice.model.CollectionReceiptInvoices;
import pialeda.app.Invoice.repository.CollectionRecptInvoicesRepo;
import pialeda.app.Invoice.repository.CollectionRecptRepository;

@Service
public class CollectionService {
    @Autowired
    private CollectionRecptRepository collectionRecptRepository;

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

                crInv.setInvoiceNo(invoice);
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
        cr.setCheckNo(chckNo);
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

    public long getCrCount(){
        return collectionRecptRepository.count();
    }
}
