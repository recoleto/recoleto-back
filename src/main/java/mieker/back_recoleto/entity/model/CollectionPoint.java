package mieker.back_recoleto.entity.model;

import jakarta.persistence.*;
import lombok.Data;
import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_collection_point")
public class CollectionPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "point_id")
    private UUID id;

    @Column(name = "point_name", nullable = false)
    private String name;

    @Column(name = "point_phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_usw", nullable = false)
    private UrbanSolidWasteEnum urbanSolidWasteEnum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "point_created_at", nullable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "point_status", nullable = false)
    private Boolean status;
}
