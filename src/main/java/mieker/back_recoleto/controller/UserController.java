package mieker.back_recoleto.controller;

import mieker.back_recoleto.entity.dto.UpdateUserDTO;
import mieker.back_recoleto.entity.dto.UserDTO;
import mieker.back_recoleto.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@PreAuthorize("hasAuthority('USUARIO')")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser () {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return ResponseEntity.status(200).body(userService.getUser(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById (@PathVariable UUID id) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return ResponseEntity.status(200).body(userService.getUser(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers () {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser (@RequestBody UpdateUserDTO input) {
        return ResponseEntity.status(200).body(userService.updateUser(input));
    }

    @PutMapping("/disable")
    public ResponseEntity<String> disableUser () {
        return ResponseEntity.status(200).body(userService.disableUser());
    }

}
// get user pelo contexto
// get user pelo id
// get all users
// update user
// delete user (desativar)
