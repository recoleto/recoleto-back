package mieker.back_recoleto.service;

import mieker.back_recoleto.entity.Enum.Role;
import mieker.back_recoleto.entity.dto.CompanyRegisterDTO;
import mieker.back_recoleto.entity.dto.LoginDTO;
import mieker.back_recoleto.entity.dto.LoginResponseDTO;
import mieker.back_recoleto.entity.dto.UserRegisterDTO;
import mieker.back_recoleto.entity.model.Address;
import mieker.back_recoleto.entity.model.Company;
import mieker.back_recoleto.entity.model.User;
import mieker.back_recoleto.entity.response.ResponseGeoCodeAPI;
import mieker.back_recoleto.exception.NotFoundException;
import mieker.back_recoleto.repository.AddressRepository;
import mieker.back_recoleto.repository.CompanyRepository;
import mieker.back_recoleto.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AddressRepository addressRepository;
    private final AddressService addressService;

    public AuthenticationService(UserRepository userRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, AddressRepository addressRepository, AddressService addressService) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.addressRepository = addressRepository;
        this.addressService = addressService;
    }

    public String userSignUp (UserRegisterDTO input) {

        if (userRepository.existsByEmail(input.getEmail()) || companyRepository.existsByEmail(input.getEmail())) {
//            throw new LoginException(addressService.getAddress(input.getCep(), input.getNumber()));
            throw new DataIntegrityViolationException("Email já cadastrado.");
        }

        if (userRepository.existsByCpf(input.getCpf())) {
            throw new DataIntegrityViolationException("CPF já cadastrado.");
        }

        User user = new User();
        user.setName(input.getName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        user.setCpf(input.getCpf());
        user.setPhone(input.getPhone());
        user.setStatus(true);
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(Role.USUARIO);

        Address address = new Address();
        address.setCep(input.getCep());
        address.setNumber(input.getNumber());
        address.setStreet(input.getStreet());
//        address.setLongitude(addressService.getAddress(input.getCep()));
        if (input.getCep() != null || input.getNumber() != null) {
            ResponseGeoCodeAPI responseGeoCodeAPI = addressService.getAddress(input.getCep(), input.getNumber());
            address.setLatitude(responseGeoCodeAPI.getLat());
            address.setLongitude(responseGeoCodeAPI.getLon());
        }
        addressRepository.save(address);

        user.setAddress(address);

        userRepository.save(user);

        return "Usuário cadastrado com sucesso.";
    }

    public LoginResponseDTO authenticate (LoginDTO input) {
        User user = new User();
        Company company = new Company();

        if (userRepository.existsByEmail(input.getEmail())) {
            user = userRepository.findUserByEmail(input.getEmail());
            if (!user.getStatus()) {
                throw new NotFoundException("Usuário desativado.");
            }
            user = userRepository.findByEmail(input.getEmail())
                    .orElseThrow(() -> new NotFoundException("Email ou senha incorretos."));
            System.out.println(user.getRole());
        } else if (companyRepository.existsByEmail(input.getEmail())) {
            company = companyRepository.findUserByEmail(input.getEmail());
            if (!company.getStatus()) {
                throw new NotFoundException("Empresa desativada.");
            }
            company = companyRepository.findByEmail(input.getEmail())
                    .orElseThrow(() -> new NotFoundException("Email ou senha incorretos."));
            System.out.println(company.getRole());
        }

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
            throw new NotFoundException("Email ou senha incorretos.") {
                // This is a custom exception with a message for invalid credentials
            };
        }

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        if (userRepository.existsByEmail(input.getEmail())) {
            loginResponseDTO.setToken(jwtService.generateToken(user));
            loginResponseDTO.setRole(user.getRole());
        } else if (companyRepository.existsByEmail(input.getEmail())) {
            loginResponseDTO.setToken(jwtService.generateToken(company));
            loginResponseDTO.setRole(company.getRole());
        }
        loginResponseDTO.setExpiresIn(jwtService.getExpirationTime());
        return loginResponseDTO;
    }

    public String companySignUp(CompanyRegisterDTO input) throws LoginException {
        if (companyRepository.existsByEmail(input.getEmail()) || userRepository.existsByEmail(input.getEmail())) {
            throw new LoginException("Email já cadastrado.");
        }

        if (companyRepository.existsByCnpj(input.getCnpj())) {
            throw new LoginException("CNPJ já cadastrado.");
        }

        Company company = new Company();
        company.setName(input.getName());
        company.setEmail(input.getEmail());
        company.setCnpj(input.getCnpj());
        company.setPhone(input.getPhone());
        company.setStatus(true);
        company.setPassword(passwordEncoder.encode(input.getPassword()));
        company.setRole(Role.EMPRESA);

        Address address = new Address();
        address.setCep(input.getCep());
        address.setNumber(input.getNumber());
        address.setStreet(input.getStreet());
//        address.setLongitude(addressService.getAddress(input.getCep()));
        if (input.getCep() != null || input.getNumber() != null) {
            ResponseGeoCodeAPI responseGeoCodeAPI = addressService.getAddress(input.getCep(), input.getNumber());
            address.setLatitude(responseGeoCodeAPI.getLat());
            address.setLongitude(responseGeoCodeAPI.getLon());
        }
        addressRepository.save(address);

        company.setAddress(address);
        companyRepository.save(company);

        return "Empresa cadastrada com sucesso.";
    }
}