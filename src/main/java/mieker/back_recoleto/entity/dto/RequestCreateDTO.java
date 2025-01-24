package mieker.back_recoleto.entity.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RequestCreateDTO {
//    private UUID collectionPointId;
    private List<WasteDTO> waste;

    @Data
    public static class WasteDTO {
        private String name;
        private Integer quantity;
    }
}
