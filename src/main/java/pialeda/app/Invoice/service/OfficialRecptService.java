package pialeda.app.Invoice.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pialeda.app.Invoice.dto.CollectionReceiptInfo;
import pialeda.app.Invoice.dto.OfficialReceiptInfo;
import pialeda.app.Invoice.model.OfficialReceipt;
import pialeda.app.Invoice.model.OfficialReceiptInvoices;
import pialeda.app.Invoice.repository.OfficialRecptInvoicesRepo;
import pialeda.app.Invoice.repository.OfficialRecptRepository;
import java.util.ArrayList;
import java.util.List;
@Service
public class OfficialRecptService {
    @Autowired
    private OfficialRecptRepository officialRecptRepository;

    @Autowired
    private OfficialRecptInvoicesRepo officialRecptInvoicesRepo;

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
        or.setTotal(formatStringToDouble(total));
        or.setCash(cash);
        or.setCheckNo(chckNo);
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

}
