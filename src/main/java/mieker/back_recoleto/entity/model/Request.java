package mieker.back_recoleto.entity.model;

import jakarta.persistence.*;
import lombok.Data;
import mieker.back_recoleto.entity.Enum.RequestStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "request_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private CollectionPoint point;

    @Column(name = "request_number", nullable = false, unique = true)
    private Long number;

    private RequestStatus status;

    @Transient
    private Integer points;

    @Column(name = "request_created_at", nullable = false)
    @CreationTimestamp
    private Date createdAt;

}
