package mieker.back_recoleto.entity.dto;

import lombok.Data;

@Data
public class CollectionPointCreateDTO {
    private String name;
    private String phone;
    private String cep;
    private String street;
    private String number;
    private String urbanSolidWaste;
}
