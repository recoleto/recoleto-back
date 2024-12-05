package mieker.back_recoleto.controller;

import mieker.back_recoleto.entity.dto.UserRegisterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/my")
    public ResponseEntity<String> userRegister () {
//        String message = authService.userSignUp(input);
//        return ResponseEntity.status(201).body(message);
        return ResponseEntity.status(201).body("Hello world");
    }
}
