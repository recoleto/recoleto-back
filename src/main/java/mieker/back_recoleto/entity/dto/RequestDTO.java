package mieker.back_recoleto.entity.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RequestDTO {
    private UUID userId;
    private UUID collectionPointId;
    private UUID companyId;
    private Long requestNumber;
    private List<Waste> waste;
    private Integer points;

    @Data
    public static class Waste {
        private String name;
        private Integer quantity;
    }
}
