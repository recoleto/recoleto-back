package mieker.back_recoleto.entity.dto;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.UrbanSolidWaste;

import java.util.Date;

@Data
public class CollectionPointDTO {
    private String name;
    private String phone;
    private String cep;
    private String street;
    private String number;
    private UrbanSolidWaste urbanSolidWaste;
}
