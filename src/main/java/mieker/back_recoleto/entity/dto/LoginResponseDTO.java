package mieker.back_recoleto.entity.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private long expiresIn;
}
