package mieker.back_recoleto.repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository {
    Optional<? extends UserDetails> findByEmail(String email);
}
