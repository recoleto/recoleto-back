package mieker.back_recoleto.entity.dto.notification;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.RequestStatus;
import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;
import mieker.back_recoleto.entity.dto.request.RequestDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class NotificationDTO {
    private UUID notificationId;
    private String userName;
    private Integer points;
    private Long requestNumber;
    private UUID companyId;
    private String collectionPointName;
    private String companyName;
    private UUID collectionPointId;
    private UUID requestId;
    private UrbanSolidWasteEnum urbanSolidWaste;
    private LocalDateTime createdAt;
    private RequestStatus status;
    private List<RequestDTO.Waste> waste;
}
