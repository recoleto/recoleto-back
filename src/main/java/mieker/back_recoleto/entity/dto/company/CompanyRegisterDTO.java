package mieker.back_recoleto.entity.dto.company;

import lombok.Data;

@Data
public class CompanyRegisterDTO {
    private String name;
    private String email;
    private String cnpj;
    private String password;
    private String phone;
    private String cep;
    private String street;
    private String number;
}
