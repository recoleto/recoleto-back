package mieker.back_recoleto.repository;

import mieker.back_recoleto.entity.Enum.UrbanSolidWaste;
import mieker.back_recoleto.entity.model.CollectionPoint;
import mieker.back_recoleto.entity.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollectionPointRepository extends JpaRepository<CollectionPoint, UUID> {
    List<CollectionPoint> findAllByCompany(Company company);
    List<CollectionPoint> findAllByCompanyAndStatus(Company company, boolean status);
    List<CollectionPoint> findCollectionPointsByUrbanSolidWaste(UrbanSolidWaste usw);
    List<CollectionPoint> findCollectionPointsByUrbanSolidWasteAndStatus(UrbanSolidWaste usw, boolean status);
    List<CollectionPoint> findAllByStatus(Boolean status);
}
