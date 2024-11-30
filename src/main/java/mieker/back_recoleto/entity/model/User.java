package mieker.back_recoleto.entity.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mieker.back_recoleto.entity.Enum.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_user")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@AttributeOverride(name = "email", column = @Column(name = "user_email", unique = true))
@AttributeOverride(name = "password", column = @Column(name = "user_password"))
@AttributeOverride(name = "phone", column = @Column(name = "user_phone"))
@AttributeOverride(name = "createdAt", column = @Column(name = "user_created_at"))
@AttributeOverride(name = "status", column = @Column(name = "user_status"))
public class User extends Account implements UserDetails {
    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(name = "user_last_name", nullable = false)
    private String lastName;
    @Column(name = "user_cpf", nullable = false, unique = true)
    private String cpf;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.toString());

        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
