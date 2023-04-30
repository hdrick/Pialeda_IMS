package pialeda.app.Invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pialeda.app.Invoice.model.CurrentInvoice;

@Repository
public interface CurrentInvoiceRepo extends JpaRepository<CurrentInvoice, Integer>{
    
}
