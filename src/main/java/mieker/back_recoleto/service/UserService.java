package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.dto.UpdateUserDTO;
import mieker.back_recoleto.entity.dto.UserDTO;
import mieker.back_recoleto.entity.model.Address;
import mieker.back_recoleto.entity.model.User;
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

    @Autowired
    private ModelMapper modelMapper;

    public UserService(ApplicationConfiguration appConfig, UserRepository userRepository, PasswordEncoder passwordEncoder, AddressRepository addressRepository) {
        this.appConfig = appConfig;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
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
        Address address = addressRepository.findAddressById(user.getAddress().getId());

        UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);

        if (address != null) {
            userDTO.setStreet(address.getStreet());
            userDTO.setNumber(address.getNumber());
            userDTO.setCep(address.getCep());
        }

        return userDTO;
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
        user.setName(input.getName());
//        user.setPhone(input.getPhone());
//        user.setLastName(input.getLastName());
//        if (userRepository.existsByEmail(input.getEmail())) {
//            throw new DataIntegrityViolationException("Email já cadastrado.");
//        }
//        user.setEmail(input.getEmail());
//        user.setPassword(passwordEncoder.encode(input.getPassword()));
        userRepository.save(user);
        return this.modelMapper.map(user, UserDTO.class);
    }

    public String disableUser() {
        UUID userId = this.getUserId();
        User user = userRepository.findUserById(userId);
        user.setStatus(false);
        userRepository.save(user);
        return "Usuário foi desativado. Para reativar a conta entre em contato com o Administrador. \nEmail: admin@recoleto.com";
    }
}
