package mieker.back_recoleto.controller;

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
    private final JwtService jwtService;
    private final AuthenticationService authService;

    public AuthController(JwtService jwtService, AuthenticationService authService) throws LoginException {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/user/sign-up")
    public ResponseEntity<String> userRegister (@RequestBody UserRegisterDTO input) throws LoginException {
        System.out.println(input);
        String message = authService.signUp(input);
        return ResponseEntity.status(201).body(message);
    }
}
