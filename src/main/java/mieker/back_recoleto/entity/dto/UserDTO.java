package mieker.back_recoleto.entity.dto;

import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
public class UserDTO {
    private String email;
    private String phone;
    private String name;
    private String lastName;
    private String cpf;
    private Date createdAt;
}
