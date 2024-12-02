package mieker.back_recoleto.repository;

import mieker.back_recoleto.entity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, AccountRepository{
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByCpf(String cpf);
}
