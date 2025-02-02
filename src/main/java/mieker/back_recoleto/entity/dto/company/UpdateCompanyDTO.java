package mieker.back_recoleto.entity.dto.company;

import lombok.Data;

@Data
public class UpdateCompanyDTO {
    private String name;
    private String phone;
    private String email;
    private String cep;
    private String street;
    private String number;
}
