package mieker.back_recoleto.entity.model;

import jakarta.persistence.*;
import lombok.Data;
import mieker.back_recoleto.entity.Enum.RequestStatus;
import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notif_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Column(name = "notif_points",nullable = false)
    private Integer points;

    @Column(name = "request_number", nullable = false)
    private Long requestNumber;

    @Column(name = "notif_status", nullable = false)
    private RequestStatus status;

    @Column(name = "notif_created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
