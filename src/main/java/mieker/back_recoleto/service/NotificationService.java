package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.Enum.RequestStatus;
import mieker.back_recoleto.entity.Enum.Role;
import mieker.back_recoleto.entity.dto.notification.NotificationDTO;
import mieker.back_recoleto.entity.dto.request.RequestDTO;
import mieker.back_recoleto.entity.model.Notification;
import mieker.back_recoleto.repository.NotificationRepository;
import mieker.back_recoleto.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {
    private final ApplicationConfiguration appConfig;
    private final NotificationRepository notifRepository;
    private final OrderRepository orderRepository;

    public NotificationService(ApplicationConfiguration appConfig, NotificationRepository notifRepository, OrderRepository orderRepository) {
        this.appConfig = appConfig;
        this.notifRepository = notifRepository;
        this.orderRepository = orderRepository;
    }


    public List<NotificationDTO> getAllTheNotifications(Role role) {
        List<Notification> notifications = new ArrayList<>();
        if (role == Role.USUARIO) {
            UUID userId = appConfig.userAuthenticator();
            notifications = notifRepository.findByUserIdAndStatusNot(userId, RequestStatus.PENDENTE);
        } else {
            UUID companyId = appConfig.companyAuthenticator();
            // Assuming you have a method in the repository to find notifications by company ID
            notifications = notifRepository.findByCompanyIdAndStatusOrStatus(companyId, RequestStatus.PENDENTE, RequestStatus.CANCELADO);
            System.out.println(notifications);
        }
        List<NotificationDTO> notificationDTOList = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setNotificationId(notification.getId());
            notificationDTO.setUserName(notification.getUser().getName());
            notificationDTO.setRequestNumber(notification.getRequestNumber());
            notificationDTO.setPoints(notification.getPoints());
            notificationDTO.setStatus(notification.getStatus());
            notificationDTO.setUrbanSolidWaste(notification.getRequest().getPoint().getUrbanSolidWasteEnum());
            notificationDTO.setCreatedAt(notification.getCreatedAt());
            notificationDTO.setRequestId(notification.getRequest().getId());
            notificationDTO.setCollectionPointId(notification.getRequest().getPoint().getId());
            notificationDTO.setCollectionPointName(notification.getRequest().getPoint().getName());
            notificationDTO.setCompanyId(notification.getRequest().getPoint().getCompany().getId());
            notificationDTO.setCompanyName(notification.getRequest().getPoint().getCompany().getName());

            // Obtendo a lista de resíduos associados ao pedido
            List<RequestDTO.Waste> wasteList = orderRepository.findByRequestId(notification.getRequest().getId())
                    .stream()
                    .map(order -> {
                        RequestDTO.Waste wasteDTO = new RequestDTO.Waste();
                        wasteDTO.setName(order.getUsw().getName());
                        wasteDTO.setQuantity(order.getQuantity());
                        return wasteDTO;
                    })
                    .toList();

            notificationDTO.setWaste(wasteList);

            notificationDTOList.add(notificationDTO);
        }

        return notificationDTOList;
    }
}
