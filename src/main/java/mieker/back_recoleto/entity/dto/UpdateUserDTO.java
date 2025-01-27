package mieker.back_recoleto.entity.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String name;
    private String email;
    private String cep;
    private String street;
    private String number;
}
