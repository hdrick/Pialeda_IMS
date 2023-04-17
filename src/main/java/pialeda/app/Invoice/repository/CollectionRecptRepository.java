package pialeda.app.Invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.CollectionReceipt;

@Repository
public interface CollectionRecptRepository extends JpaRepository<CollectionReceipt, Integer>{
    
}
