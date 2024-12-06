package mieker.back_recoleto.controller;

import mieker.back_recoleto.entity.dto.CompanyRegisterDTO;
import mieker.back_recoleto.entity.dto.LoginDTO;
import mieker.back_recoleto.entity.dto.LoginResponseDTO;
import mieker.back_recoleto.entity.dto.UserRegisterDTO;
import mieker.back_recoleto.service.AuthenticationService;
import mieker.back_recoleto.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) throws LoginException {
        this.authService = authService;
    }

    @PostMapping("/user/sign-up")
    public ResponseEntity<String> userRegister (@RequestBody UserRegisterDTO input) throws LoginException {
        String message = authService.userSignUp(input);
        return ResponseEntity.status(201).body(message);
    }

    @PostMapping("/company/sign-up")
    public ResponseEntity<String> companyRegister (@RequestBody CompanyRegisterDTO input) throws LoginException {
        String message = authService.companySignUp(input);
        return ResponseEntity.status(201).body(message);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> userLogin (@RequestBody LoginDTO input) {
        LoginResponseDTO response = authService.authenticate(input);
        return ResponseEntity.status(200).body(response);
    }

}
