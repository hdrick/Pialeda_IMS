package pialeda.app.Invoice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.OfficialReceiptInvoices;

@Repository
public interface OfficialRecptInvoicesRepo extends JpaRepository<OfficialReceiptInvoices, Integer>{
    List<OfficialReceiptInvoices> findByOfficialReceiptNum(int orNum);
}
