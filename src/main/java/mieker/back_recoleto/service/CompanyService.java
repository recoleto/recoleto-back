package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.dto.CompanyDTO;
import mieker.back_recoleto.entity.dto.UpdateCompanyDTO;
import mieker.back_recoleto.entity.dto.UserDTO;
import mieker.back_recoleto.entity.model.Company;
import mieker.back_recoleto.repository.CompanyRepository;
import mieker.back_recoleto.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyService {
    private final ApplicationConfiguration appConfig;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    private UUID getUserId() {
        return appConfig.companyAuthenticator();
    }

    public CompanyService(ApplicationConfiguration appConfig, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.appConfig = appConfig;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CompanyDTO getCompany(UUID id) {
        UUID userId = id;
        if (id == null) {
            userId = this.getUserId();
        }
        Company company = companyRepository.findCompanyById(userId);
        return modelMapper.map(company, CompanyDTO.class);
    }

    public List<CompanyDTO> getAll() {
        return companyRepository.findAll()
                .stream()
                .map(company -> modelMapper.map(company, CompanyDTO.class))
                .toList(); // Use `collect(Collectors.toList())` for older Java versions
    }

    public CompanyDTO updateCompany(UpdateCompanyDTO input) {
        UUID userId = this.getUserId();
        appConfig.emailValidator(input.getEmail());
        Company company = companyRepository.findCompanyById(userId);
        company.setName(input.getName());
        company.setPhone(input.getPhone());
        company.setEmail(input.getEmail());
        company.setPassword(passwordEncoder.encode(input.getPassword()));
        companyRepository.save(company);
        return modelMapper.map(company, CompanyDTO.class);
    }

    public String disableCompany() {
        UUID userId = this.getUserId();
        Company company = companyRepository.findCompanyById(userId);
        company.setStatus(false);
        companyRepository.save(company);
        return "Empresa foi desativada. Para reativar a conta entre em contato com o Administrador. \nEmail: admin@recoleto.com";
    }
}
