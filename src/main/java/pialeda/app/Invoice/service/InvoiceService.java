package pialeda.app.Invoice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pialeda.app.Invoice.config.DateUtils;
import pialeda.app.Invoice.model.Client;
import pialeda.app.Invoice.model.Invoice;
import pialeda.app.Invoice.model.InvoiceProductInfo;
import pialeda.app.Invoice.model.Supplier;
import pialeda.app.Invoice.repository.ClientRepository;
import pialeda.app.Invoice.repository.InvoiceProdInfoRepository;
import pialeda.app.Invoice.repository.InvoiceRepository;
import pialeda.app.Invoice.dto.InvoiceInfo;
import pialeda.app.Invoice.repository.SupplierRepository;

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
    private SupplierRepository supplierRepository;

    @Autowired
    private ClientRepository clientRepository;

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
        Pageable pageable = PageRequest.of(pageNumber -1, 2);
        return invoiceRepository.findAll(pageable);
    }

    public Page<Invoice> getPageByKeyword(String keyword, int pageNumber)
    {
        Pageable pageable = PageRequest.of(pageNumber -1, 7);
        return invoiceRepository.searchByKeyword(keyword, pageable);
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
    public BigDecimal getTotalAmountByClientNameAndSupplierName(String clientName, String supplierName) {
        return invoiceRepository.getSumOfAllInvoiceAmountsByClientNameAndSupplierName(clientName, supplierName);
    }
    public BigDecimal getTotalAmountBySupplierNameBetweenDateRange(String supplierName, LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.sumOfGrandTotalBySupplierNameBetweenDateCreated(supplierName, startDate, endDate);
    }
    public BigDecimal getTotalAmountByClientNameAndSupplierNameBetweenDateRange(String clientName, String supplierName, LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.sumOfGrandTotalByClientNameAndSupplierNameBetweenDateCreated(clientName, supplierName, startDate, endDate);
    }

//    DRICKS...
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
                                 String totalAmt, List<String> qtyList, List<String> unitList, List<String> articlesList,
                                 List<String> unitPriceList, List<String> amountList, List<String> prodIdList){

       try {
           Invoice invDb = invoiceRepository.findByInvoiceNum(invoiceNumber);
           Supplier supplier = supplierRepository.findByName(supplierName);
           Client client = clientRepository.findByName(clientName);
           List<InvoiceProductInfo> productInfo = invoiceProdInfoRepository.findByInvoiceNumber(invoiceNumber);

           invDb.setDateCreated(dateCreated);

           invDb.setSupplierName(supplier.getName());
           invDb.setSupplierAddress(supplier.getAddress());
           invDb.setSupplierTin(supplier.getTin());

           invDb.setClientName(client.getName());
           invDb.setClientContactPerson(clientContactPerson);
           invDb.setClientTin(client.getTin());
           invDb.setClientAddress(client.getAddress());

           String totalAmtNew = totalAmt.replace(",", "");
           invDb.setGrandTotal(Double.parseDouble(totalAmtNew));

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

//    public Page<Invoice> findAllWithSort(String field, String direction, int pageNumber)
//    {
//        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())?
//                Sort.by(field).ascending(): Sort.by(field).descending();
//
//        Pageable pageable = PageRequest.of(pageNumber -1, 7, sort);
//
//        return invoiceRepository.findAll(pageable);
//    }

}
