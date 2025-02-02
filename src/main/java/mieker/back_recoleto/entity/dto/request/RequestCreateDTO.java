package mieker.back_recoleto.entity.dto.request;

import lombok.Data;

import java.util.List;

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
