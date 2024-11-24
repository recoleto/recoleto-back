package mieker.back_recoleto.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Data
public class Account {
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
//    private Address address;
}
