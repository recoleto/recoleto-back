package mieker.back_recoleto.entity.dto;

import lombok.Data;

@Data
public class CompanyRegisterDTO {
    private String name;
    private String email;
    private String cnpj;
    private String password;
    private String phone;
}
