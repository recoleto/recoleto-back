package mieker.back_recoleto.repository;

import mieker.back_recoleto.entity.model.CollectionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CollectionPointRepository extends JpaRepository<CollectionPoint, UUID> {
}
