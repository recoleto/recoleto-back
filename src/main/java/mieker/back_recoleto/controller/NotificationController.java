package mieker.back_recoleto.controller;

import mieker.back_recoleto.entity.dto.NotificationDTO;
import mieker.back_recoleto.entity.dto.RequestCreateDTO;
import mieker.back_recoleto.service.NotificationService;
import mieker.back_recoleto.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notifService;

    public NotificationController(NotificationService notifService) {
        this.notifService = notifService;
    }

//    @PreAuthorize("hasAuthority('USUARIO')")
    @GetMapping("/company")
    public ResponseEntity<List<NotificationDTO>> getCompanyNotifications() {
        List<NotificationDTO> notificationDTOList = notifService.getAllTheCompanyNotifications();
        return ResponseEntity.status(200).body(notificationDTOList);
    }

    @GetMapping("/user")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications () {
        List<NotificationDTO> notificationDTOList = notifService.getAllTheUserNotifications();
        return ResponseEntity.status(200).body(notificationDTOList);
    }
}
