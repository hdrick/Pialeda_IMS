package pialeda.app.Invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.CollectionReceiptInvoices;

import java.util.List;

@Repository
public interface CollectionRecptInvoicesRepo extends JpaRepository<CollectionReceiptInvoices, Integer>{

    // List<CollectionReceiptInvoices> findByInvoice(Invoice invoiceNum);
    List<CollectionReceiptInvoices> findByCollectionReceiptNum(int crNum);
    List<CollectionReceiptInvoices> getAllByInvoice(String invoiceNum);
//    List<CollectionReceiptInvoices> findCollectionReceiptNumByInvoiceNum(String invoiceNum);
}
