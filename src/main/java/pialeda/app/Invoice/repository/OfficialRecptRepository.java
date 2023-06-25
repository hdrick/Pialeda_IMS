package pialeda.app.Invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.OfficialReceipt;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OfficialRecptRepository extends JpaRepository<OfficialReceipt, Integer> {
    OfficialReceipt findById(int orId);
    OfficialReceipt findByOfficialReceiptNum(int orNum);

    List<OfficialReceipt> findBySupplierName(String supplierName);

    @Query("SELECT COALESCE(SUM(i.total), 0) FROM OfficialReceipt i WHERE i.supplierName = :supplierName")
    BigDecimal getSumOfAllOfficialReceiptAmountsBySupplierName(@Param("supplierName") String supplierName);
}
