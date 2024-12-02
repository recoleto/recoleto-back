package mieker.back_recoleto.entity.model;

import jakarta.persistence.*;
import lombok.Data;
import mieker.back_recoleto.entity.Enum.Role;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@MappedSuperclass
@Data
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String phone;
    @Column(nullable = false)
    @CreationTimestamp
    private Date createdAt;
    @Column(nullable = false)
    private Boolean status;
    @Enumerated(EnumType.STRING)
    private Role role;
//    private Address address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.toString());

        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return email;
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
