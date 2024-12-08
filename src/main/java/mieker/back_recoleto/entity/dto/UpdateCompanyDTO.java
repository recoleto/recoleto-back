package mieker.back_recoleto.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateCompanyDTO {
    private String name;
    private String email;
    private String phone;
    private String password;
}
