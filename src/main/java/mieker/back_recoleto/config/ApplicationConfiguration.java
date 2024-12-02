package mieker.back_recoleto.config;

import mieker.back_recoleto.repository.AccountRepository;
import mieker.back_recoleto.repository.CompanyRepository;
import mieker.back_recoleto.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final AccountRepository[] accountRepositories;

    public ApplicationConfiguration(UserRepository userRepository, CompanyRepository companyRepository, AccountRepository[] accountRepositories) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.accountRepositories = accountRepositories;
    }

    //    defines how to retrieve the user using the UserRepository that is injected
    @Bean
    UserDetailsService userDetailsService(List<AccountRepository> accountRepositories) {
        return email -> {
            return accountRepositories.stream()
                    .map(repo -> repo.findByEmail(email))
                    .flatMap(Optional::stream)
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        };
    }

    //    creates an instance of the BCryptPasswordEncoder() used to encode the plain user password.
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    //    sets the new strategy to perform the authentication.
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService(List.of(accountRepositories)));
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}