package mieker.back_recoleto.repository;

import mieker.back_recoleto.entity.model.CollectionPoint;
import mieker.back_recoleto.entity.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {
    @Query("SELECT MAX(o.number) FROM Order o")
    Long findLastNumber();

    List<Request> findByPointId(UUID point);

    List<Request> findByUserId(UUID id);
}
