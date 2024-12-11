package mieker.back_recoleto.entity.model;

import jakarta.persistence.*;
import lombok.Data;
import mieker.back_recoleto.entity.Enum.Role;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private UUID id;
    @Column(name = "address_street")
    private String street;
    @Column(name = "address_number")
    private String number;
    @Column(name = "address_cep")
    private String cep;
    @Column(name = "address_latitude")
    private String latitude;
    @Column(name = "address_longitude")
    private String longitude;
    @Column(name = "address_role")
    private Role role;
    @CreationTimestamp
    @Column(name = "address_creaded_at")
    private Date createdAt;

}
