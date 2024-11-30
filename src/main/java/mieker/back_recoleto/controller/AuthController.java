package mieker.back_recoleto.controller;

import mieker.back_recoleto.entity.dto.LoginDTO;
import mieker.back_recoleto.entity.dto.LoginResponseDTO;
import mieker.back_recoleto.entity.dto.UserRegisterDTO;
import mieker.back_recoleto.entity.model.User;
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
        String message = authService.signUp(input);
        return ResponseEntity.status(201).body(message);
    }

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponseDTO> userLogin (@RequestBody LoginDTO input){
        User user = authService.authenticate(input);
        String token = jwtService.generateToken(user);
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.status(200).body(response);
    }
}
