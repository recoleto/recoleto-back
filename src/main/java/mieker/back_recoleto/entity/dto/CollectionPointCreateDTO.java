package mieker.back_recoleto.entity.dto;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.UrbanSolidWaste;

@Data
public class CollectionPointCreateDTO {
    private String name;
    private String phone;
    private String cep;
    private String street;
    private String number;
    private UrbanSolidWaste urbanSolidWaste;
}
