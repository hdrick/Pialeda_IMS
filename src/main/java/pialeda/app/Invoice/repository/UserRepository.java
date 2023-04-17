package pialeda.app.Invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pialeda.app.Invoice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(int id);
    User findByEmail(String email);
    User findTokenByEmail(String email);

}
