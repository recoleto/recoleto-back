package mieker.back_recoleto.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_user")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@AttributeOverride(name = "email", column = @Column(name = "user_email"))
@AttributeOverride(name = "password", column = @Column(name = "user_password"))
@AttributeOverride(name = "phone", column = @Column(name = "user_phone"))
@AttributeOverride(name = "createdAt", column = @Column(name = "user_created_at"))
@AttributeOverride(name = "status", column = @Column(name = "user_status"))
public class User extends Account{
    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(name = "user_last_name", nullable = false)
    private String lastName;
    @Column(name = "user_cpf", nullable = false)
    private String cpf;
}
