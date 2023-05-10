package pialeda.app.Invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.CollectionReceiptInvoices;
import pialeda.app.Invoice.model.Invoice;

import java.util.List;

@Repository
public interface CollectionRecptInvoicesRepo extends JpaRepository<CollectionReceiptInvoices, Integer>{

    List<CollectionReceiptInvoices> findByInvoice(Invoice invoiceNum);
//    List<CollectionReceiptInvoices> findCollectionReceiptNumByInvoiceNum(String invoiceNum);
}
