package pialeda.app.Invoice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pialeda.app.Invoice.config.DateUtils;
import pialeda.app.Invoice.model.*;
import pialeda.app.Invoice.repository.*;
import pialeda.app.Invoice.dto.InvoiceInfo;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class InvoiceService {

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

    public Optional<Invoice> getInvoiceById(int id)
    {
        if (invoiceRepository.findById(id) == null )
        {
            throw new EntityNotFoundException();
        }
        Optional<Invoice> invoice = Optional.ofNullable(invoiceRepository.findById(id));
        return invoice;
    }
    public List<Invoice> getAllInvoice(){
        return invoiceRepository.findAll();
    }
    public List<Invoice> getCRInvoice(String supplierName, String clientName)
    {
        // String unpaidStatus = "Unpaid";
        // return invoiceRepository.findBySupplierNameAndClientNameAndStatus(supplierName, clientName, unpaidStatus);
        return invoiceRepository.findBySupplierNameAndClientNameAndStatusInProgressAndUnPaid(clientName, supplierName);
    }

    public List<Invoice> getORInvoice(String supplierName, String clientName)
    {
        return invoiceRepository.findBySupplierNameAndClientNameAndStatusInProgressAndUnPaid(clientName, supplierName);
    }

    public List<Invoice> getAllInvoiceByStatusUnpaid(){
        return invoiceRepository.findInvoicesByStatusUnpaid();
    }

    public List<Invoice> getKeyword(String query)
    {
        return invoiceRepository.findByClientNameContainingIgnoreCase(query);
    }

    public void createInvoice(InvoiceInfo invoiceInfo, String date,List<String> qtyList, List<String> unitList,
                            List<String> articlesList, List<String> unitPriceList, List<String> amountList){
                                Invoice invoice = new Invoice();     
        //Insert invoice info
        invoice.setInvoiceNum(invoiceInfo.getInvoiceNum());
        invoice.setPoNum(invoiceInfo.getPoNum());
        invoice.setDateCreated(LocalDate.parse(date));
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
    public boolean checkInvoiceNum(String invNum){
        Invoice isExist = invoiceRepository.findByInvoiceNum(invNum);
        if(isExist == null){
            return false;
        }
        return true;
    }
    public long getInvoiceCunt(){
        return invoiceRepository.count();
    }

    public double getSuppTotalLimit(String suppName){
        return invoiceRepository.findSumLimitByName(suppName);
    }

    public Page<Invoice> findPage(int pageNumber)
    {
        Pageable pageable = PageRequest.of(pageNumber -1, 7);
        return invoiceRepository.findAll(pageable);
    }

    public Page<Invoice> getPageByKeyword(String keyword, int pageNumber)
    {
        Pageable pageable = PageRequest.of(pageNumber -1, 7);
        return invoiceRepository.searchByKeyword(keyword, pageable);
    }
    public List<Invoice> getClientOfSupplier(String supplierName)
    {
        return invoiceRepository.findClientNameBySupplierName(supplierName);
    }
    public Page<Invoice> filterPageByClient(String name, int pageNumber)
    {
        Pageable pageable = PageRequest.of(pageNumber -1, 7);
        return invoiceRepository.findByClientNameContainingIgnoreCase(name, pageable);
    }
    public Page<Invoice> filterPageByClientSortByMonth(String name, String month, int pageNumber)
    {
        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findByClientNameAndDateCreatedContainingIgnoreCase(name, month, pageable);
    }
    public Page<Invoice> filterPageBySupplier(String name, int pageNumber)
    {
        Pageable pageable = PageRequest.of(pageNumber -1, 7);
        return invoiceRepository.findBySupplierNameContainingIgnoreCase(name, pageable);
    }
    public Page<Invoice> filterPageByClientAndSupplier(String client, String supplier, int pageNumber)
    {
        Pageable pageable = PageRequest.of(pageNumber -1, 7);
        return invoiceRepository.findByClientNameAndSupplierNameContainingIgnoreCase(client, supplier, pageable);
    }

    public Page<Invoice> filterPageByClientAndSupplierSortByMonth(String client, String supplier, String month, int pageNumber)
    {
        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findByClientNameAndSupplierNameAndDateCreatedContainingIgnoreCase(client, supplier, month, pageable);
    }
    public Page<Invoice> sortByMonthAsc(String month, int pageNumber)
    {

        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findByDateCreatedContainingIgnoreCase(month, pageable);
    }
    public Page<Invoice> filterPageByClientSortByDateRange(String name, LocalDate startDate, LocalDate endDate, int pageNumber)
    {
        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findByClientNameAndDateCreatedBetween(name, startDate, endDate, pageable);
    }
    public Page<Invoice> filterPageBySupplierSortByDateRange(String name, LocalDate startDate, LocalDate endDate, int pageNumber)
    {
        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findBySupplierNameAndDateCreatedBetween(name, startDate, endDate, pageable);
    }
    public Page<Invoice> filterPageByClientAndSupplierSortByDateRange(String client, String supplier, LocalDate startDate, LocalDate endDate, int pageNumber)
    {
        Sort sort = Sort.by("dateCreated");
        sort = sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
        return invoiceRepository.findByClientNameAndSupplierNameAndDateCreatedBetween(client, supplier, startDate, endDate, pageable);
    }
    public BigDecimal getTotalAmountBySupplierName(String supplierName) {
        return invoiceRepository.getSumOfAllInvoiceAmountsBySupplierName(supplierName);
    }
    public BigDecimal getTotalReceiptBySupplierName(String supplierName) {
        return officialRecptRepository.getSumOfAllOfficialReceiptAmountsBySupplierName(supplierName);
    }
    public BigDecimal getTotalAmountByClientNameAndSupplierName(String clientName, String supplierName) {
        return invoiceRepository.getSumOfAllInvoiceAmountsByClientNameAndSupplierName(clientName, supplierName);
    }
    public BigDecimal getTotalAmountBySupplierNameBetweenDateRange(String supplierName, LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.sumOfGrandTotalBySupplierNameBetweenDateCreated(supplierName, startDate, endDate);
    }
    public BigDecimal getTotalAmountByClientNameAndSupplierNameBetweenDateRange(String clientName, String supplierName, LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.sumOfGrandTotalByClientNameAndSupplierNameBetweenDateCreated(clientName, supplierName, startDate, endDate);
    }
    public List<OfficialReceipt> getSupplierOR(String supplierName)
    {
        return officialRecptRepository.findBySupplierName(supplierName);
    }
    // public List<CollectionReceiptInvoices> getCollectionReceipt(Invoice invoiceNum)
    // {
    //     return collectionRecptInvoicesRepo.findByInvoice(invoiceNum);
    // }


//    DRICKS...
// public List<CollectionReceiptInvoices> getCollectionReceipt(String invoiceNum)
// {
//     return collectionRecptInvoicesRepo.findByInvoice(invoiceNum);
// }


    public Page<Invoice> getInvoicesPaginated(int currentPage, int size){
        Pageable p = PageRequest.of(currentPage, size);
        return invoiceRepository.findAll(p);
    }

    public Invoice getInvByIdAndFillFields(int id){
        return invoiceRepository.findById(id);
    }

    public List<InvoiceProductInfo> getAllProdByInvNum(String invNum){
        return invoiceProdInfoRepository.findByInvoiceNumber(invNum);
    }

    public boolean updateInvoices(String invoiceNumber, LocalDate dateCreated,
                                 String supplierName, String clientName, String clientContactPerson,
                                 String totalAmt, String addVat, String amtNetVat, String totalSalesVatInc, List<String> qtyList, List<String> unitList, List<String> articlesList,
                                 List<String> unitPriceList, List<String> amountList, List<String> prodIdList, String clientTerms, String clientBStyle){

       try {
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
            if(or != null){
                or.setInvoiceAmount(Double.parseDouble(totalAmtNew));
                officialRecptInvoicesRepo.save(or);
                OfficialReceipt  specfcOr = officialRecptRepository.findByOfficialReceiptNum(or.getOfficialReceiptNum());
                double total = Double.parseDouble(totalAmtNew);
                double totalResult = 0;

                if(specfcOr.getAddVat() != 0){
                    // update vat
                    // Add VAT: 0.12 * total amount
                    // Net of Vat: total amount * 0.02
                    // totalResult = (totalAmount + addVat) -lwTax;
                    
                    double newAddVat = 0.12 * total;
                    double newNOV = total * 0.02;
                    totalResult = (total+newAddVat)-newNOV;

                    // System.out.println("Total Sales: "+total);
                    // System.out.println("Add VAT: "+newAddVat);
                    // System.out.println("Net of Vat: "+newNOV);
                    // System.out.println("Ammount Due: "+totalResult);
                    // System.out.println("EWT: ");
                    // System.out.println("Total: "+totalResult);

                    specfcOr.setTotalSales(total);
                    specfcOr.setAddVat(newAddVat);
                    specfcOr.setLessWithHoldTax(newNOV);
                    specfcOr.setAmountDue(totalResult);

                    if(specfcOr.getEwt() !=0){
                        // let result = (totalAmountNoComma/1.12)*ewtPercentage;
                        double ewt = specfcOr.getEwt();
                        // inputEwt = (ewt / (amountDue / 1.12)) to get the percentage of ewt
                        double ewtPercentage = ewt/(specfcOr.getAmountDue()/1.12);
                        double roundedEwtPercentage = Math.round(ewtPercentage * 100.0) / 100.0;
                        double newTotalAmount = totalResult;

                        double newEwt = (newTotalAmount/1.12)*roundedEwtPercentage;
                        double newTotal = newTotalAmount - newEwt;

                        // Round newEwt and newTotal to 2 decimal places
                        newEwt = Math.round(newEwt * 100.0) / 100.0;
                        totalResult = Math.round(newTotal * 100.0) / 100.0;

                        specfcOr.setEwt(newEwt);
                        specfcOr.setTotal(totalResult);
                        officialRecptRepository.save(specfcOr);
                    }else{
                        specfcOr.setTotal(totalResult);
                        officialRecptRepository.save(specfcOr);
                    }
                }
                if(specfcOr.getEwt() !=0){
                    //update ewt
                    // let result = (totalAmountNoComma/1.12)*ewtPercentage;
                    double ewt = specfcOr.getEwt();
                    // inputEwt = (ewt / (amountDue / 1.12)) to get the percentage of ewt
                    double ewtPercentage = ewt/(specfcOr.getAmountDue()/1.12);
                    double roundedEwtPercentage = Math.round(ewtPercentage * 100.0) / 100.0;
                    totalResult = total;
                    double newTotalAmount = totalResult;

                    double newEwt = (newTotalAmount/1.12)*roundedEwtPercentage;
                    double newTotal = newTotalAmount - newEwt;

                    // Round newEwt and newTotal to 2 decimal places
                    newEwt = Math.round(newEwt * 100.0) / 100.0;
                    totalResult = Math.round(newTotal * 100.0) / 100.0;

                    specfcOr.setTotalSales(total);
                    specfcOr.setAmountDue(total);
                    specfcOr.setEwt(newEwt);
                    specfcOr.setTotal(totalResult);
                    officialRecptRepository.save(specfcOr);
                }   
                if(specfcOr.getEwt() == 0 && specfcOr.getEwt() ==0){
                    specfcOr.setTotalSales(total);
                    specfcOr.setTotal(total);
                    specfcOr.setAmountDue(total);
                }
            }




            // Update CollectionReceiptInvoices info if inv has cr
            List<CollectionReceiptInvoices> listCR= collectionRecptInvoicesRepo.getAllByInvoice(invoiceNumber);//check if inv has cr
            if(!(listCR.isEmpty() || listCR.size() == 0)){
                for(CollectionReceiptInvoices crInvoice: listCR){
                    crInvoice.setInvoiceAmount(Double.parseDouble(totalAmtNew));
                    collectionRecptInvoicesRepo.save(crInvoice);

                    CollectionReceipt cr = collectionRecptRepository.findByCollectionReceiptNum(crInvoice.getCollectionReceiptNum());
                    
                    double ewt = cr.getEwt();
                    if(!(ewt == 0)){
                        // inputEwt = (ewt / (amountDue / 1.12)) to get the percentage of ewt
                        double ewtPercentage = ewt/(cr.getAmountDue()/1.12);
                        double roundedEwtPercentage = Math.round(ewtPercentage * 100.0) / 100.0;
                        double newTotalAmount = Double.parseDouble(totalAmtNew);

                        double newEwt = (newTotalAmount/1.12)*roundedEwtPercentage;
                        double newTotal = newTotalAmount - newEwt;

                        // Round newEwt and newTotal to 2 decimal places
                        newEwt = Math.round(newEwt * 100.0) / 100.0;
                        newTotal = Math.round(newTotal * 100.0) / 100.0;
                        System.out.println("ewtpercent: "+roundedEwtPercentage);
                        System.out.println("newEwt: "+newEwt);
                        System.out.println("newTotal: "+newTotal);
                        System.out.println("newTotalAmount: "+newTotalAmount);
                        cr.setAmountDue(newTotalAmount);
                        cr.setEwt(newEwt);
                        cr.setTotal(newTotal);
                        collectionRecptRepository.save(cr);
                    }
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
           return true;
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }
    }

    public Invoice getInvoiceDetails(String invoiceNum)
    {
        return invoiceRepository.findByInvoiceNum(invoiceNum);
    }

    public Invoice findByInvNum(String invoiceNum){
        return invoiceRepository.findByInvoiceNum(invoiceNum);
    }


    public String duplicateSpecificInvoice(int id, String invoiceNum, String poNum, String date){
        // lets get the content the content of the specific invoice
        Invoice invoiceToDuplicate = invoiceRepository.findById(id);

        Invoice newInvoice = new Invoice();   
        

        Invoice ifInvExist = findByInvNum(invoiceNum);
        //check if invoice number is in the db
        if(ifInvExist == null){
            Invoice ifPOExist = invoiceRepository.findByPoNum(poNum);
            //check if PO exist
            if(ifPOExist == null){
                newInvoice.setInvoiceNum(invoiceNum);
                newInvoice.setPoNum(poNum);
                newInvoice.setDateCreated(LocalDate.parse(date));

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
                invoiceRepository.save(newInvoice);

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
                invoiceProdInfoRepository.saveAll(duplicatedInvProd);

                return "success";
            }else{
                return "purchaseNumberFound";
            }
        }else{
            return "invoiceNumberFound";
        }
    }
}
