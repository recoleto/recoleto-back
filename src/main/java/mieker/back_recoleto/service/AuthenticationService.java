package mieker.back_recoleto.service;

import mieker.back_recoleto.entity.Enum.Role;
import mieker.back_recoleto.entity.dto.LoginDTO;
import mieker.back_recoleto.entity.dto.UserRegisterDTO;
import mieker.back_recoleto.entity.model.User;
import mieker.back_recoleto.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public String signUp (UserRegisterDTO input) throws LoginException {

        if (userRepository.existsByEmail(input.getEmail())) {
            throw new LoginException("Email já cadastrado.");
        }

        if (userRepository.existsByCpf(input.getCpf())) {
            throw new LoginException("CPF já cadastrado.");
        }

        User user = new User();
        user.setName(input.getName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        user.setCpf(input.getCpf());
        user.setPhone(input.getPhone());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(Role.USUARIO);
        userRepository.save(user);

        return "Usuário cadastrado com sucesso.";
    }

    public User authenticate (LoginDTO input) {

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email ou senha incorretos."));

        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            // Handle invalid email or password
            throw new BadCredentialsException("Email ou senha incorretos.") {
                // This is a custom exception with a message for invalid credentials
            };
        }

        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }
}