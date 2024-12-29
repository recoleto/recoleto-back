package mieker.back_recoleto.controller;


import mieker.back_recoleto.entity.dto.CollectionPointDTO;
import mieker.back_recoleto.service.CollectionPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/collection-point")
public class CollectionPointController {
    private final CollectionPointService collectionPointService;

    public CollectionPointController(CollectionPointService collectionPointService) {
        this.collectionPointService = collectionPointService;
    }

//    @PreAuthorize("hasAuthority('EMPRESA')")
    @PostMapping()
    public ResponseEntity<String> createCollectionPoint (@RequestBody CollectionPointDTO input) {
        String response = collectionPointService.createCollectionPoint(input);
        return ResponseEntity.status(201).body(response);
    }
}
