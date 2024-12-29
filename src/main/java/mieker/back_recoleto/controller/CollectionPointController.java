package mieker.back_recoleto.controller;


import mieker.back_recoleto.entity.Enum.UrbanSolidWaste;
import mieker.back_recoleto.entity.dto.CollectionPointCreateDTO;
import mieker.back_recoleto.entity.dto.CollectionPointDTO;
import mieker.back_recoleto.service.CollectionPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@PreAuthorize("hasAuthority('EMPRESA')")
@RequestMapping("/collection-point")
public class CollectionPointController {
    private final CollectionPointService collectionPointService;

    public CollectionPointController(CollectionPointService collectionPointService) {
        this.collectionPointService = collectionPointService;
    }

    @PostMapping()
    public ResponseEntity<String> createCollectionPoint (@RequestBody CollectionPointCreateDTO input) {
        String response = collectionPointService.createCollectionPoint(input);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CollectionPointDTO>> getAllCollectionPoints () {
        List<CollectionPointDTO> collectionPointDTOList = collectionPointService.getAllCollectionPoints();
        return ResponseEntity.status(200).body(collectionPointDTOList);
    }

    @GetMapping("/company")
    public ResponseEntity<List<CollectionPointDTO>> getCollectionPointsByCompany () {
        List<CollectionPointDTO> collectionPointDTOList = collectionPointService.getAllCollectionPointsByCompanyId(null);
        return ResponseEntity.status(200).body(collectionPointDTOList);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CollectionPointDTO>> getCollectionPointsByCompanyId (@PathVariable UUID companyId) {
        List<CollectionPointDTO> collectionPointDTOList = collectionPointService.getAllCollectionPointsByCompanyId(companyId);
        return ResponseEntity.status(200).body(collectionPointDTOList);
    }

    @GetMapping("/{pointId}")
    public ResponseEntity<CollectionPointDTO> getCollectionPointsById (@PathVariable UUID pointId) {
        CollectionPointDTO collectionPointDTO = collectionPointService.getCollectionPointById(pointId);
        return ResponseEntity.status(200).body(collectionPointDTO);
    }

    @GetMapping("/usw/{usw}")
    public ResponseEntity<List<CollectionPointDTO>> getCollectionPointsByUSWType(@PathVariable UrbanSolidWaste usw) {
        List<CollectionPointDTO> collectionPointDTOList = collectionPointService.getAllCollectionPointsByUSW(usw);
        return ResponseEntity.status(200).body(collectionPointDTOList);
    }
}

// get all collection points
// get all collection point by company
// get collection point by id
// get collection point by usw
// update collection point
// delete collection point
