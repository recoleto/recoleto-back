package mieker.back_recoleto.controller;


import mieker.back_recoleto.entity.dto.CollectionPointCreateDTO;
import mieker.back_recoleto.entity.dto.UrbanSolidWasteCreateDTO;
import mieker.back_recoleto.service.UrbanSolidWasteService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usw")
public class UrbanSolidWasteController {
    private final UrbanSolidWasteService urbanSolidWasteService;

    public UrbanSolidWasteController(UrbanSolidWasteService urbanSolidWasteService) {
        this.urbanSolidWasteService = urbanSolidWasteService;
    }

    @PreAuthorize("hasAuthority('EMPRESA') or hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<String> createUrbanSolidWaste (@RequestBody UrbanSolidWasteCreateDTO input) throws BadRequestException {
        String response = urbanSolidWasteService.createUrbanSolidWaste(input);
        return ResponseEntity.status(201).body(response);
    }

    @PreAuthorize("hasAuthority('EMPRESA') or hasAuthority('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<String> createUrbanSolidWasteAdminRoute (@RequestBody UrbanSolidWasteCreateDTO input) throws BadRequestException {
        String response = urbanSolidWasteService.createUrbanSolidWaste(input);
        return ResponseEntity.status(201).body(response);
    }
}
// create usw // unique name // only empresas e admin
// get usw by id
// get usw by name
// get usw by type
// get all usw
// update usw // only admin
// delete usw // only admin
