package mieker.back_recoleto.entity.dto;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;

@Data
public class UrbanSolidWasteCreateDTO {
    private String name;
    private UrbanSolidWasteEnum type;
    private Integer points;
}
