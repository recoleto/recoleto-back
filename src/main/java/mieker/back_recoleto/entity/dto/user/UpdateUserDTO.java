package mieker.back_recoleto.entity.dto.user;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String cep;
    private String street;
    private String number;
}
