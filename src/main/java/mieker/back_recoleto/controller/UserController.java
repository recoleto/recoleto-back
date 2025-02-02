package mieker.back_recoleto.controller;

import mieker.back_recoleto.entity.dto.user.UpdateUserDTO;
import mieker.back_recoleto.entity.dto.user.UserDTO;
import mieker.back_recoleto.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
//@PreAuthorize("hasAuthority('USUARIO') or hasAuthority('ADMIN')")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('USUARIO') or hasAuthority('ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser () {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return ResponseEntity.status(200).body(userService.getUser(null));
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById (@PathVariable UUID id) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return ResponseEntity.status(200).body(userService.getUser(id));
    }

//    só o admin pode ver todos os usuários ?
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers () {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    @PreAuthorize("hasAuthority('USUARIO')")
    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser (@RequestBody UpdateUserDTO input) throws BadRequestException {
        return ResponseEntity.status(200).body(userService.updateUser(input));
    }

    @PreAuthorize("hasAuthority('USUARIO')")
    @PutMapping("/disable")
    public ResponseEntity<String> disableUser () {
        return ResponseEntity.status(200).body(userService.disableUser(null));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/disable/{userId}")
    public ResponseEntity<String> disableUser (@PathVariable UUID userId) {
        return ResponseEntity.status(200).body(userService.disableUser(userId));
    }

}
// get user pelo contexto
// get user pelo id
// get all users
// update user
// delete user (desativar)
