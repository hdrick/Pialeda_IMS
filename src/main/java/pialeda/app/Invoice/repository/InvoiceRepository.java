package pialeda.app.Invoice.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;



@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    Invoice findById(int id);
    List<Invoice> findByClientNameContainingIgnoreCase(String query);
    List<Invoice> findByClientName(String client);
    List<Invoice> findBySupplierNameAndClientNameAndStatus(String supplierName, String clientName, String status);
    @Query(value ="SELECT COALESCE(SUM(grand_total), 0) FROM invoice where supplier_name like:suppliername", nativeQuery = true)
    double findSumLimitByName(@Param("suppliername") String suppliername);

    @Query(value = "SELECT * FROM invoice WHERE status LIKE 'Unpaid' OR status LIKE 'In Progress' ORDER BY invoice_date_created DESC", nativeQuery = true)
    List<Invoice> findInvoicesByStatusUnpaid();
    // SELECT * FROM invoice WHERE supplier_name = 'Supplier Test1' AND client_name = 'client test1' AND (status = 'In Progress' OR status = 'UnPaid');
    @Query(value = "SELECT * FROM invoice WHERE supplier_name = :supplierName AND client_name = :clientName AND (status = 'In Progress' OR status = 'UnPaid') ORDER BY invoice_date_created DESC;", nativeQuery = true)
    List<Invoice> findBySupplierNameAndClientNameAndStatusInProgressAndUnPaid(@Param("clientName") String clientName, @Param("supplierName") String supplierName);

    Invoice findByInvoiceNum(String invNum);
    Page<Invoice> findByClientNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Invoice> findByClientNameAndDateCreatedContainingIgnoreCase(String name, String month, Pageable pageable);
    Page<Invoice> findBySupplierNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Invoice> findBySupplierNameAndDateCreatedContainingIgnoreCase(String name, String month, Pageable pageable);
    Page<Invoice> findByClientNameAndSupplierNameContainingIgnoreCase(String client, String supplier, Pageable pageable);

    Page<Invoice> findByDateCreatedContainingIgnoreCase(String month, Pageable pageable);
    Page<Invoice> findByClientNameAndSupplierNameAndDateCreatedContainingIgnoreCase(String client, String supplier, String month, Pageable pageable);
    Page<Invoice> findByClientNameAndDateCreatedBetween(String clientName, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Invoice> findBySupplierNameAndDateCreatedBetween(String name, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Invoice> findByClientNameAndSupplierNameAndDateCreatedBetween(String client, String supplier, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT COALESCE(SUM(i.grandTotal), 0) FROM Invoice i WHERE i.supplierName = :supplierName")
    BigDecimal getSumOfAllInvoiceAmountsBySupplierName(@Param("supplierName") String supplierName);

    @Query("SELECT COALESCE(SUM(i.grandTotal), 0) FROM Invoice i WHERE i.clientName = :clientName AND i.supplierName = :supplierName")
    BigDecimal getSumOfAllInvoiceAmountsByClientNameAndSupplierName(@Param("clientName") String clientName, @Param("supplierName") String supplierName);

    @Query("SELECT COALESCE(SUM(i.grandTotal), 0) FROM Invoice i WHERE i.supplierName = :supplierName AND i.dateCreated BETWEEN :startDate AND :endDate")
    BigDecimal sumOfGrandTotalBySupplierNameBetweenDateCreated(@Param("supplierName") String supplierName, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(i.grandTotal), 0) FROM Invoice i WHERE i.clientName = :clientName AND  i.supplierName = :supplierName AND i.dateCreated BETWEEN :startDate AND :endDate")
    BigDecimal sumOfGrandTotalByClientNameAndSupplierNameBetweenDateCreated(@Param("clientName") String clientName, @Param("supplierName") String supplierName, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT i FROM Invoice i WHERE CONCAT(i.clientName, i.supplierName, i.clientAddress, i.supplierAddress, i.clientBusStyle, i.cashier) LIKE %:keyword%")
    Page<Invoice> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
