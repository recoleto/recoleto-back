package mieker.back_recoleto.controller;


import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;
import mieker.back_recoleto.entity.dto.urbanSolidWaste.UrbanSolidWasteCreateDTO;
import mieker.back_recoleto.entity.dto.urbanSolidWaste.UrbanSolidWasteDTO;
import mieker.back_recoleto.entity.dto.urbanSolidWaste.UrbanSolidWasteUpdateDTO;
import mieker.back_recoleto.service.UrbanSolidWasteService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usw")
public class UrbanSolidWasteController {
    private final UrbanSolidWasteService urbanSolidWasteService;

    public UrbanSolidWasteController(UrbanSolidWasteService urbanSolidWasteService) {
        this.urbanSolidWasteService = urbanSolidWasteService;
    }

    @PreAuthorize("hasAuthority('EMPRESA')")
    @PostMapping()
    public ResponseEntity<String> createUrbanSolidWaste (@RequestBody UrbanSolidWasteCreateDTO input) throws BadRequestException {
        String user = "company";
        String response = urbanSolidWasteService.createUrbanSolidWaste(input, user);
        return ResponseEntity.status(201).body(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<String> createUrbanSolidWasteAdminRoute (@RequestBody UrbanSolidWasteCreateDTO input) throws BadRequestException {
        String user = "admin";
        String response = urbanSolidWasteService.createUrbanSolidWaste(input, user);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<UrbanSolidWasteDTO>> getUrbanSolidWasteList () {
        List<UrbanSolidWasteDTO> response = urbanSolidWasteService.getUrbanSolidWasteList();
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<UrbanSolidWasteDTO> getUrbanSolidWasteById (@PathVariable UUID id) {
        UrbanSolidWasteDTO response = urbanSolidWasteService.getUrbanSolidWasteById(id);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<UrbanSolidWasteDTO> getUrbanSolidWasteByName (@PathVariable String name) {
        UrbanSolidWasteDTO response = urbanSolidWasteService.getUrbanSolidWasteByName(name);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("type/{type}")
    public ResponseEntity<List<UrbanSolidWasteDTO>> getUrbanSolidWasteByType (@PathVariable UrbanSolidWasteEnum type) {
        List<UrbanSolidWasteDTO> response = urbanSolidWasteService.getUrbanSolidWasteByType(type);
        return ResponseEntity.status(200).body(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("admin/update/{id}")
    public ResponseEntity<UrbanSolidWasteDTO> updateUrbanSolidWaste (@PathVariable UUID id, @RequestBody UrbanSolidWasteUpdateDTO input) {
        UrbanSolidWasteDTO response = urbanSolidWasteService.updateUrbanSolidWaste(id, input);
        return ResponseEntity.status(200).body(response);
    }

}
// create usw // unique name // only empresas e admin
// get usw by id
// get usw by name
// get usw by type
// get all usw
// update usw // only admin
