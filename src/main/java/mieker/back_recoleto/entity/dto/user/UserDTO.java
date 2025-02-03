package mieker.back_recoleto.entity.dto.user;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String name;
    private String lastName;
    private Integer points;
    private String email;
    private String phone;
    private String cpf;
    private String cep;
    private String street;
    private String number;
    private Date createdAt;
}
