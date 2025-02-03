package mieker.back_recoleto.entity.dto.collectionPoint;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;

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
    private UrbanSolidWasteEnum urbanSolidWaste;
    private String companyName;
    private UUID companyUUID;
}
