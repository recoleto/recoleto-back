package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.dto.CompanyDTO;
import mieker.back_recoleto.entity.dto.UpdateCompanyDTO;
import mieker.back_recoleto.entity.dto.UserDTO;
import mieker.back_recoleto.entity.model.Address;
import mieker.back_recoleto.entity.model.Company;
import mieker.back_recoleto.entity.response.ResponseGeoCodeAPI;
import mieker.back_recoleto.repository.AddressRepository;
import mieker.back_recoleto.repository.CompanyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyService {
    private final ApplicationConfiguration appConfig;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final AddressService addressService;

    @Autowired
    private ModelMapper modelMapper;

    private UUID getCompanyId() {
        return appConfig.companyAuthenticator();
    }

    public CompanyService(ApplicationConfiguration appConfig, CompanyRepository companyRepository, PasswordEncoder passwordEncoder, AddressRepository addressRepository, AddressService addressService) {
        this.appConfig = appConfig;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
        this.addressService = addressService;
    }

    public CompanyDTO getCompany(UUID id) {
        UUID companyId = id;
        if (id == null) {
            companyId = this.getCompanyId();
        }
        Company company = companyRepository.findCompanyById(companyId);

        return getCompanyDTO(company);
    }

    public List<CompanyDTO> getAll() {
        return companyRepository.findAll()
                .stream()
                .map(company ->
                {
                    CompanyDTO companyDTO = modelMapper.map(company, CompanyDTO.class);
                    if (company.getAddress() != null) {
                        companyDTO.setCep(company.getAddress().getCep());
                        companyDTO.setStreet(company.getAddress().getStreet());
                        companyDTO.setNumber(company.getAddress().getNumber());
                    }
                    return companyDTO;
                })
                .toList(); // Use `collect(Collectors.toList())` for older Java versions
    }

    public CompanyDTO updateCompany(UpdateCompanyDTO input) {
        UUID userId = this.getCompanyId();
        Company company = companyRepository.findCompanyById(userId);

        if (input.getName() != null) {
            company.setName(input.getName());
        }

        Address address = company.getAddress();

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

        company.setAddress(address);

        companyRepository.save(company);

        return getCompanyDTO(company);
    }

    private CompanyDTO getCompanyDTO(Company company) {
        CompanyDTO companyDTO = this.modelMapper.map(company, CompanyDTO.class);

        if (company.getAddress() != null) {
            companyDTO.setStreet(company.getAddress().getStreet());
            companyDTO.setNumber(company.getAddress().getNumber());
            companyDTO.setCep(company.getAddress().getCep());
        }

        return companyDTO;
    }

    public String disableCompany() {
        UUID userId = this.getCompanyId();
        Company company = companyRepository.findCompanyById(userId);
        company.setStatus(false);
        companyRepository.save(company);
        return "Empresa foi desativada. Para reativar a conta entre em contato com o Administrador. \nEmail: admin@recoleto.com";
    }
}
