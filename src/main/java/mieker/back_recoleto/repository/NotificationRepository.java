package mieker.back_recoleto.repository;

import mieker.back_recoleto.entity.Enum.RequestStatus;
import mieker.back_recoleto.entity.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByCompanyId(UUID companyId);

    List<Notification> findByUserIdAndStatusNot(UUID userId, RequestStatus status);
    List<Notification> findByCompanyIdAndStatusAndStatus(UUID userId, RequestStatus status, RequestStatus status2);
    List<Notification> findByRequestId(UUID requestId);

    List<Notification> findByCompanyIdAndStatusOrStatus(UUID companyId, RequestStatus requestStatus, RequestStatus requestStatus1);
}
