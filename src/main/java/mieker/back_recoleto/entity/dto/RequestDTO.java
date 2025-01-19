package mieker.back_recoleto.entity.dto;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.RequestStatus;

import java.util.List;
import java.util.UUID;

@Data
public class RequestDTO {
    private UUID userId;
    private UUID collectionPointId;
    private UUID companyId;
    private Long number;
    private List<Waste> waste;
    private Integer points;
    private RequestStatus status;

    @Data
    public static class Waste {
        private String name;
        private Integer quantity;
    }
}
