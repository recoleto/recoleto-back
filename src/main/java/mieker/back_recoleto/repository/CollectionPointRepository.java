package mieker.back_recoleto.repository;

import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;
import mieker.back_recoleto.entity.model.CollectionPoint;
import mieker.back_recoleto.entity.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollectionPointRepository extends JpaRepository<CollectionPoint, UUID> {
    List<CollectionPoint> findAllByCompany(Company company);
    List<CollectionPoint> findAllByCompanyAndStatus(Company company, boolean status);
    List<CollectionPoint> findCollectionPointsByUrbanSolidWasteEnum(UrbanSolidWasteEnum usw);
    List<CollectionPoint> findCollectionPointsByUrbanSolidWasteEnumAndStatus(UrbanSolidWasteEnum usw, boolean status);
    List<CollectionPoint> findAllByStatus(Boolean status);

    List<CollectionPoint> findByCompanyId(UUID id);
}
