package mieker.back_recoleto.entity.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor // Adiciona um construtor sem argumentos necessário para o Hibernate
@Table(name = "tb_urban_solid_waste")
public class UrbanSolidWaste {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "usw_id")
    private UUID id;

    @Column(name = "usw_name", nullable = false)
    private String name;

    @Column(name = "usw_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UrbanSolidWasteEnum type;

    @Column(name = "usw_points", nullable = false)
    private Integer points;

    @Column(name = "usw_created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "usw_created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    // Construtor correto
    public UrbanSolidWaste(String name, UrbanSolidWasteEnum type, int points, UUID createdBy) {
        this.name = name;
        this.type = type;
        this.points = points;
        this.createdBy = createdBy;
    }
}
