package pialeda.app.Invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.OfficialReceipt;

@Repository
public interface OfficialRecptRepository extends JpaRepository<OfficialReceipt, Integer> {
}
