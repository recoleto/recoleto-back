package mieker.back_recoleto.entity.dto.urbanSolidWaste;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;

import java.util.UUID;

@Data
public class UrbanSolidWasteDTO {
    private UUID id;
    private String name;
    private UrbanSolidWasteEnum type;
    private int points;
    private UUID createdBy;
}
