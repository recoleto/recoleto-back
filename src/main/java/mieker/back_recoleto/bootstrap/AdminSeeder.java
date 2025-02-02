package mieker.back_recoleto.bootstrap;

import mieker.back_recoleto.entity.Enum.Role;
import mieker.back_recoleto.entity.model.User;
import mieker.back_recoleto.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createAdministrator();
    }

    private void createAdministrator() {
        Optional<User> optionalUser = userRepository.findByEmail("admin@gmail.com");

        if (optionalUser.isPresent()) {
            return;
        }

        User user = new User();
        user.setName("Administrador");
        user.setLastName("Principal");
        user.setEmail("admin@gmail.com");
        user.setCpf("00000000001");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.ADMIN);
        user.setStatus(true);

        userRepository.save(user);
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}