package mieker.back_recoleto.entity.dto.request;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.RequestStatus;

import java.util.List;
import java.util.UUID;

@Data
public class RequestDTO {
    private UUID requestId;
    private UUID userId;
    private String userName;
    private String companyName;
    private String collectionPointName;
    private UUID collectionPointId;
    private UUID companyId;
    private Long solicitationNumber;
    private List<Waste> waste;
    private Integer points;
    private RequestStatus status;

    @Data
    public static class Waste {
        private String name;
        private Integer quantity;
    }
}
