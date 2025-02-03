package mieker.back_recoleto.config;

import mieker.back_recoleto.entity.model.Company;
import mieker.back_recoleto.entity.model.User;
import mieker.back_recoleto.repository.AccountRepository;
import mieker.back_recoleto.repository.CompanyRepository;
import mieker.back_recoleto.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Configuration
public class ApplicationConfiguration {
    private final AccountRepository[] accountRepositories;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public ApplicationConfiguration(AccountRepository[] accountRepositories, UserRepository userRepository, CompanyRepository companyRepository) {
        this.accountRepositories = accountRepositories;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
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

    public UUID userAuthenticator() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }

    public UUID companyAuthenticator() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company currentCompany = (Company) authentication.getPrincipal();
        return currentCompany.getId();
    }

    public void emailValidator(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email já cadastrado");
        } else if (companyRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email já cadastrado");
        }
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}