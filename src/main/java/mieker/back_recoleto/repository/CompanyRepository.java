package mieker.back_recoleto.repository;

import mieker.back_recoleto.entity.model.Company;
import mieker.back_recoleto.entity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID>, AccountRepository {
    Optional<Company> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByCnpj(String cnpj);
    Company findCompanyById(UUID id);
}
