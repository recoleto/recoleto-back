package mieker.back_recoleto.controller;

import mieker.back_recoleto.entity.dto.CompanyDTO;
import mieker.back_recoleto.entity.dto.UpdateCompanyDTO;
import mieker.back_recoleto.entity.dto.UpdateUserDTO;
import mieker.back_recoleto.entity.dto.UserDTO;
import mieker.back_recoleto.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PreAuthorize("hasAuthority('EMPRESA')")
    @GetMapping("/me")
    public ResponseEntity<CompanyDTO> getCompany () {
        return ResponseEntity.status(200).body(companyService.getCompany(null));
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById (@PathVariable UUID id) {
        return ResponseEntity.status(200).body(companyService.getCompany(id));
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<CompanyDTO>> getAllCompany () {
        return ResponseEntity.status(200).body(companyService.getAll());
    }

    @PreAuthorize("hasAuthority('EMPRESA')")
    @PutMapping("/update")
    public ResponseEntity<CompanyDTO> updateCompany (@RequestBody UpdateCompanyDTO input) {
        return ResponseEntity.status(200).body(companyService.updateCompany(input));
    }

    @PreAuthorize("hasAuthority('EMPRESA') or hasAuthority('ADMIN')")
    @PutMapping("/disable")
    public ResponseEntity<String> disableCompany () {
        return ResponseEntity.status(200).body(companyService.disableCompany());
    }

}
// get company pelo contexto
// get company pelo id
// get all companies
// update company
// delete company (desativar)
