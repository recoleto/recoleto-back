package mieker.back_recoleto.entity.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String cpf;
//    private String cep;
//    private String street;
//    private String number;
}
