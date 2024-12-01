package mieker.back_recoleto.entity.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_company")
@AttributeOverride(name = "id", column = @Column(name = "company_id"))
@AttributeOverride(name = "email", column = @Column(name = "company_email"))
@AttributeOverride(name = "password", column = @Column(name = "company_password"))
@AttributeOverride(name = "phone", column = @Column(name = "company_phone"))
@AttributeOverride(name = "createdAt", column = @Column(name = "company_created_at"))
@AttributeOverride(name = "status", column = @Column(name = "company_status"))
@AttributeOverride(name = "role", column = @Column(name = "company_role"))
public class Company extends Account {
    @Column(name = "company_cnpj", nullable = false, unique = true)
    private String cnpj;
    @Column(name = "company_name", nullable = false)
    private String name;
}
