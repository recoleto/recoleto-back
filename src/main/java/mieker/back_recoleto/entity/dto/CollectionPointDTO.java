package mieker.back_recoleto.entity.dto;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.UrbanSolidWaste;

import java.util.UUID;

@Data
public class CollectionPointDTO {
    private UUID pointUUID;
    private String name;
    private String phone;
    private String cep;
    private String street;
    private String number;
    private String latitude;
    private String longitude;
    private UrbanSolidWaste urbanSolidWaste;
    private String companyName;
    private UUID companyUUID;
}
