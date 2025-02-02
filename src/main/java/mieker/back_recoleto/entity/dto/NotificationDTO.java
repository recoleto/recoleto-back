package mieker.back_recoleto.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mieker.back_recoleto.entity.Enum.RequestStatus;
import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class NotificationDTO {
    private UUID notificationId;
    private String userName;
    private Integer points;
    private Long requestNumber;
    private UrbanSolidWasteEnum urbanSolidWaste;
    private LocalDateTime createdAt;
    private RequestStatus status;

}
