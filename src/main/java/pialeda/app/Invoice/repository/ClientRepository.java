package pialeda.app.Invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>{
    Client findByName(String name);

}
