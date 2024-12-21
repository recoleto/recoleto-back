package mieker.back_recoleto.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository {
    Optional<? extends UserDetails> findByEmail(String email);


//    @Query("SELECT CASE " +
//            "WHEN u.email = :email THEN 'user' " +
//            "WHEN c.email = :email THEN 'company' " +
//            "END " +
//            "FROM User u FULL JOIN Company c " +
//            "ON u.email = c.email " +
//            "WHERE u.email = :email OR c.email = :email")
//    String userRole(String email);
}
