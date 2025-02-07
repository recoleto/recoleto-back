package mieker.back_recoleto.entity.dto.company;

import lombok.Data;

import java.util.Date;

@Data
public class CompanyDTO {
    private String name;
    private String email;
    private String phone;
    private String cnpj;
    private String cep;
    private String street;
    private String number;
    private Date createdAt;
}
