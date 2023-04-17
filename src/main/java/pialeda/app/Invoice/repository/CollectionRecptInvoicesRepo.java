package pialeda.app.Invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.CollectionReceiptInvoices;

@Repository
public interface CollectionRecptInvoicesRepo extends JpaRepository<CollectionReceiptInvoices, Integer>{
    
}
