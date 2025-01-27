package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.dto.UpdateUserDTO;
import mieker.back_recoleto.entity.dto.UserDTO;
import mieker.back_recoleto.entity.model.Address;
import mieker.back_recoleto.entity.model.User;
import mieker.back_recoleto.entity.response.ResponseGeoCodeAPI;
import mieker.back_recoleto.repository.AddressRepository;
import mieker.back_recoleto.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ModelMapper modelMapper;

    public UserService(ApplicationConfiguration appConfig, UserRepository userRepository, PasswordEncoder passwordEncoder, AddressRepository addressRepository, AddressService addressService) {
        this.appConfig = appConfig;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
        this.addressService = addressService;
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
                    }
                    return userDTO;
                })
                .toList(); // Use `collect(Collectors.toList())` for older Java versions
    }


    public UserDTO updateUser(UpdateUserDTO input) {
        UUID userId = this.getUserId();
        User user = userRepository.findUserById(userId);

        if (input.getName() != null) {
            user.setName(input.getName());
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
            userDTO.setNumber(user.getAddress().getNumber());
        }

        return userDTO;
    }

    public String disableUser() {
        UUID userId = this.getUserId();
        User user = userRepository.findUserById(userId);
        user.setStatus(false);
        userRepository.save(user);
        return "Usuário foi desativado. Para reativar a conta entre em contato com o Administrador. \nEmail: admin@recoleto.com";
    }
}
