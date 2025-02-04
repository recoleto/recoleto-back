package mieker.back_recoleto.controller;

import mieker.back_recoleto.entity.Enum.Role;
import mieker.back_recoleto.entity.dto.notification.NotificationDTO;
import mieker.back_recoleto.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notifService;

    public NotificationController(NotificationService notifService) {
        this.notifService = notifService;
    }

    @PreAuthorize("hasAuthority('EMPRESA')")
    @GetMapping("/company")
    public ResponseEntity<List<NotificationDTO>> getCompanyNotifications() {
        Role role = Role.EMPRESA;
        List<NotificationDTO> notificationDTOList = notifService.getAllTheNotifications(role);
        return ResponseEntity.status(200).body(notificationDTOList);
    }

    @PreAuthorize("hasAuthority('USUARIO')")
    @GetMapping("/user")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications () {
        Role role = Role.USUARIO;
        List<NotificationDTO> notificationDTOList = notifService.getAllTheNotifications(role);
        return ResponseEntity.status(200).body(notificationDTOList);
    }
}
