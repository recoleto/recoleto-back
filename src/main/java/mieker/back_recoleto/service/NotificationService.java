package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.Enum.RequestStatus;
import mieker.back_recoleto.entity.dto.NotificationDTO;
import mieker.back_recoleto.entity.model.CollectionPoint;
import mieker.back_recoleto.entity.model.Company;
import mieker.back_recoleto.entity.model.Notification;
import mieker.back_recoleto.entity.model.Request;
import mieker.back_recoleto.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {
    private final ApplicationConfiguration appConfig;
    private final NotificationRepository notifRepository;

    public NotificationService(ApplicationConfiguration appConfig, NotificationRepository notifRepository) {
        this.appConfig = appConfig;
        this.notifRepository = notifRepository;
    }

    public List<NotificationDTO> getAllTheCompanyNotifications() {
        UUID companyId = appConfig.companyAuthenticator();

        // Assuming you have a method in the repository to find notifications by company ID
        List<Notification> notifications = notifRepository.findByCompanyIdAndStatusAndStatus(companyId, RequestStatus.PENDENTE, RequestStatus.CANCELADO);

        if (notifications.isEmpty()) {
            throw new RuntimeException("Nenhuma notificação encontrada para a empresa.");
        }

        List<NotificationDTO> notificationDTOList = new ArrayList<>();

        // Iterate over the list of notifications and map them to NotificationDTO
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setNotificationId(notification.getId());
            notificationDTO.setUserName(notification.getUser().getName());
            notificationDTO.setRequestNumber(notification.getRequestNumber());
            notificationDTO.setPoints(notification.getPoints());
            notificationDTO.setStatus(notification.getStatus());
            notificationDTO.setUrbanSolidWaste(notification.getRequest().getPoint().getUrbanSolidWasteEnum());
            notificationDTO.setCreatedAt(notification.getCreatedAt());

            // Add the DTO to the list
            notificationDTOList.add(notificationDTO);
        }

        return notificationDTOList;
    }

    public List<NotificationDTO> getAllTheUserNotifications() {
        UUID userId = appConfig.userAuthenticator();

        // Assuming you have a method in the repository to find notifications by user ID
        List<Notification> notifications = notifRepository.findByUserIdAndStatusNot(userId, RequestStatus.PENDENTE);

        List<NotificationDTO> notificationDTOList = new ArrayList<>();

        // Iterate over the list of notifications and map them to NotificationDTO
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setNotificationId(notification.getId());
            notificationDTO.setUserName(notification.getUser().getName());
            notificationDTO.setRequestNumber(notification.getRequestNumber());
            notificationDTO.setPoints(notification.getPoints());
            notificationDTO.setStatus(notification.getStatus());
            notificationDTO.setUrbanSolidWaste(notification.getRequest().getPoint().getUrbanSolidWasteEnum());
            notificationDTO.setCreatedAt(notification.getCreatedAt());

            // Add the DTO to the list
            notificationDTOList.add(notificationDTO);
        }

        return notificationDTOList;
    }
}
