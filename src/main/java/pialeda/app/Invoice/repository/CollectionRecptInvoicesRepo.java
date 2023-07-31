package pialeda.app.Invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.CollectionReceiptInvoices;

import java.util.List;

@Repository
public interface CollectionRecptInvoicesRepo extends JpaRepository<CollectionReceiptInvoices, Integer>{

    CollectionReceiptInvoices findByInvoice(String invoiceNum);
    List<CollectionReceiptInvoices> findByCollectionReceiptNum(long crNum);
    List<CollectionReceiptInvoices> getAllByInvoice(String invoiceNum);
//    long findByCollectionReceiptNumCountAll(Long id);
    List<CollectionReceiptInvoices> findAllByCollectionReceiptNum(long crNum);

}
