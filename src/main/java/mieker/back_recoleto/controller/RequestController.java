package mieker.back_recoleto.controller;


import mieker.back_recoleto.entity.Enum.Role;
import mieker.back_recoleto.entity.dto.CollectionPointCreateDTO;
import mieker.back_recoleto.entity.dto.RequestCreateDTO;
import mieker.back_recoleto.entity.dto.RequestDTO;
import mieker.back_recoleto.entity.dto.UpdateRequestDTO;
import mieker.back_recoleto.service.RequestService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/request")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PreAuthorize("hasAuthority('USUARIO')")
    @PostMapping("{pointId}")
    public ResponseEntity<String> createCollectionPoint (@RequestBody RequestCreateDTO input, @PathVariable UUID pointId) {
        String response = requestService.createRequest(input, pointId);
        return ResponseEntity.status(201).body(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<RequestDTO>> getAllRequests () {
        List<RequestDTO> requestDTOList = requestService.getAllRequests();
        return ResponseEntity.status(200).body(requestDTOList);
    }

    @PreAuthorize("hasAuthority('EMPRESA')")
    @GetMapping("/point/{pointId}/all")
    public ResponseEntity<List<RequestDTO>> getRequestsByPoint (@PathVariable UUID pointId) {
        List<RequestDTO> requestDTOList = requestService.getAllRequestsByPoint(pointId);
        return ResponseEntity.status(200).body(requestDTOList);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<RequestDTO> getRequestById (@PathVariable UUID requestId) {
        RequestDTO requestDTO = requestService.getRequestById(requestId);
        return ResponseEntity.status(200).body(requestDTO);
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<RequestDTO>> getRequestsByUser () {
        List<RequestDTO> requestDTOList = requestService.getAllRequestsByUser();
        return ResponseEntity.status(200).body(requestDTOList);
    }

//    @GetMapping("/user/all")
//    public ResponseEntity<List<RequestDTO>> updateRequest () {
//        List<RequestDTO> requestDTOList = requestService.getAllRequestsByUser();
//        return ResponseEntity.status(200).body(requestDTOList);
//    }

    @PreAuthorize("hasAuthority('EMPRESA')")
    @GetMapping("/company/all")
    public ResponseEntity<List<RequestDTO>> getRequestsByCompany () {
        List<RequestDTO> requestDTOList = requestService.getAllRequestsByCompany();
        return ResponseEntity.status(200).body(requestDTOList);
    }

    @PutMapping("/update/company/{requestId}")
    public ResponseEntity<RequestDTO> companyUpdateRequest (@RequestBody UpdateRequestDTO input, @PathVariable UUID requestId) {
        Role role = Role.EMPRESA;
        RequestDTO requestDTO = requestService.updateStatusRequest(input, requestId, role);
        return ResponseEntity.status(200).body(requestDTO);
    }

    @PutMapping("/update/user/{requestId}")
    public ResponseEntity<RequestDTO> userUpdateRequest (@RequestBody UpdateRequestDTO input, @PathVariable UUID requestId) {
        Role role = Role.USUARIO;
        RequestDTO requestDTO = requestService.updateStatusRequest(input, requestId, role);
        return ResponseEntity.status(200).body(requestDTO);
    }

    @GetMapping("/update/collection-point/{pointId}")
    public ResponseEntity<RequestDTO> getCollectionPointRequests (@PathVariable UUID pointId) {
        RequestDTO requestDTO = requestService.getCollectionPointRequests(pointId);
        return ResponseEntity.status(200).body(requestDTO);
    }


}
// get request by id **
// get all requests **
// create request **
// update request // mudar status que vai ser o cancelar também
// delete request // cancelar
// get request by user **
// get request by company **
// get request by status
// get request by collection point **
//

// fazer verificação do request q só pode ser feito solicitação de descarte em pontos de coleta que aceitam o tipo de resíduo

