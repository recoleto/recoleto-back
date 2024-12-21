package mieker.back_recoleto.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String cpf;
    private String cep;
    private String street;
    private String number;
    private Date createdAt;
}
