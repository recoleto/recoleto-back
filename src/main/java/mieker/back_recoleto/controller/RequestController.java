package mieker.back_recoleto.controller;


import mieker.back_recoleto.entity.dto.CollectionPointCreateDTO;
import mieker.back_recoleto.entity.dto.RequestCreateDTO;
import mieker.back_recoleto.service.RequestService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/request")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PreAuthorize("hasAuthority('USUARIO')")
    @PostMapping()
    public ResponseEntity<String> createCollectionPoint (@RequestBody RequestCreateDTO input) {
        String response = requestService.createRequest(input);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllRequests () {
        return ResponseEntity.status(200).body("all requests");
    }
}
// get request by id
// get all requests
// create request
// update request
// delete request
// get request by user
// get request by company
// get request by status
// get request by collection point

