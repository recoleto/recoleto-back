package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.dto.login.PasswordDTO;
import mieker.back_recoleto.entity.dto.user.UpdateUserDTO;
import mieker.back_recoleto.entity.dto.user.UserDTO;
import mieker.back_recoleto.entity.model.Address;
import mieker.back_recoleto.entity.model.User;
import mieker.back_recoleto.entity.response.ResponseGeoCodeAPI;
import mieker.back_recoleto.repository.AddressRepository;
import mieker.back_recoleto.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final ApplicationConfiguration appConfig;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final RequestService requestService;

    @Autowired
    private ModelMapper modelMapper;

    public UserService(ApplicationConfiguration appConfig, UserRepository userRepository, PasswordEncoder passwordEncoder, AddressRepository addressRepository, AddressService addressService, RequestService requestService) {
        this.appConfig = appConfig;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
        this.addressService = addressService;

        this.requestService = requestService;
    }

    private UUID getUserId() {
        return appConfig.userAuthenticator();
    }

    public UserDTO getUser(UUID userId) {
        UUID userID = userId;
        if (userId == null) {
            userID = this.getUserId();
        }

        User user = userRepository.findUserById(userID);

        return getUserDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user ->
                {
                    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                    if (user.getAddress() != null) {
                        userDTO.setCep(user.getAddress().getCep());
                        userDTO.setStreet(user.getAddress().getStreet());
                        userDTO.setNumber(user.getAddress().getNumber());
                        userDTO.setPoints(requestService.getPoints(user.getId()));
                    }
                    return userDTO;
                })
                .toList(); // Use `collect(Collectors.toList())` for older Java versions
    }


    public UserDTO updateUser(UpdateUserDTO input) throws BadRequestException {
        UUID userId = this.getUserId();
        User user = userRepository.findUserById(userId);

        if (input.getName() != null) {
            user.setName(input.getName());
        }

        if (input.getLastName() != null) {
            user.setLastName(input.getLastName());
        }

        if (input.getPhone() != null) {
            user.setPhone(input.getPhone());
        }

        Address address = user.getAddress();

        if (input.getCep() != null) {
            address.setCep(input.getCep());
        }

        if (input.getStreet() != null) {
            address.setStreet(input.getStreet());
        }

        if (input.getNumber() != null) {
            address.setNumber(input.getNumber());
        }

        if (input.getCep() != null && input.getNumber() != null) {
            ResponseGeoCodeAPI responseGeoCodeAPI = addressService.getAddress(input.getCep(), input.getNumber());
            address.setLatitude(responseGeoCodeAPI.getLat());
            address.setLongitude(responseGeoCodeAPI.getLon());
        }
        addressRepository.save(address);
        System.out.println(address.getNumber());
        user.setAddress(address);

        userRepository.save(user);

        return getUserDTO(user);
    }

    private UserDTO getUserDTO(User user) {
        UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);

        if (user.getAddress() != null) {
            userDTO.setCep(user.getAddress().getCep());
            userDTO.setStreet(user.getAddress().getStreet());
            userDTO.setPoints(requestService.getPoints(user.getId()));
            userDTO.setNumber(user.getAddress().getNumber());
        }

        return userDTO;
    }

    public String disableUser(UUID userId) {
        if (userId == null) {
            userId = this.getUserId();
        }
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        String uniqueSuffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8); // Generates a short unique string

        user.setName("[desativado]");
        user.setLastName("[desativado]");
        user.setEmail("desativado_" + uniqueSuffix + "@example.com"); // Unique deactivated email
        user.setPassword("[desativado]");
        user.setAddress(null);
        user.setPhone(null);
        user.setCpf("xxx" + uniqueSuffix); // Unique deactivated CPF
        user.setStatus(false);

        userRepository.save(user);
        return "Usuário foi desativado com sucesso";
    }

    public String updatePassword(PasswordDTO passwordDTO) {
        UUID userId = this.getUserId();
        User user = userRepository.findUserById(userId);
        if (!passwordEncoder.matches(passwordDTO.getPassword(), user.getPassword())) {
            throw new DataIntegrityViolationException("Senha incorreta");
        }
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
        return "Senha atualizada com sucesso";
    }
}
