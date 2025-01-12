package mieker.back_recoleto.repository;

import mieker.back_recoleto.entity.model.UrbanSolidWaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UrbanSolidWasteRepository extends JpaRepository<UrbanSolidWaste, UUID> {
}
