package pialeda.app.Invoice.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.Supplier;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    @Query("SELECT s.name FROM Supplier s")
    List<String> getAllSupplierNames(Sort name);
    Supplier findByName(String name);

}
