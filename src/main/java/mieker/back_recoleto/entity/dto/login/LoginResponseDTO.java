package mieker.back_recoleto.entity.dto.login;

import lombok.Data;
import mieker.back_recoleto.entity.Enum.Role;

@Data
public class LoginResponseDTO {
    private String token;
    private long expiresIn;
    private Role role;
}
