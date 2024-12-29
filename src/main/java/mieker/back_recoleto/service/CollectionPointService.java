package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.Enum.Role;
import mieker.back_recoleto.entity.dto.CollectionPointDTO;
import mieker.back_recoleto.entity.model.Address;
import mieker.back_recoleto.entity.model.CollectionPoint;
import mieker.back_recoleto.entity.model.Company;
import mieker.back_recoleto.entity.response.ResponseGeoCodeAPI;
import mieker.back_recoleto.repository.AddressRepository;
import mieker.back_recoleto.repository.CollectionPointRepository;
import mieker.back_recoleto.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CollectionPointService {
    private final CollectionPointRepository collectionPointRepository;
    private final ApplicationConfiguration appConfig;
    private final CompanyRepository companyRepository;
    private final AddressService addressService;
    private final AddressRepository addressRepository;

    public CollectionPointService(CollectionPointRepository collectionPointRepository, ApplicationConfiguration appConfig, CompanyRepository companyRepository, AddressService addressService, AddressRepository addressRepository) {
        this.collectionPointRepository = collectionPointRepository;
        this.appConfig = appConfig;
        this.companyRepository = companyRepository;
        this.addressService = addressService;
        this.addressRepository = addressRepository;
    }

    public String createCollectionPoint (CollectionPointDTO input) {
        UUID companyId = appConfig.companyAuthenticator();
        Company company = companyRepository.findCompanyById(companyId);

        if (company == null) {
            return "Empresa não encontrada.";
        }

        CollectionPoint collectionPoint = new CollectionPoint();
        collectionPoint.setCompany(company);
        collectionPoint.setName(input.getName());
        collectionPoint.setPhone(input.getPhone());
        collectionPoint.setUrbanSolidWaste(input.getUrbanSolidWaste());

        Address address = new Address();
        address.setRole(Role.PONTO_DE_COLETA);
        address.setStreet(input.getStreet());
        address.setNumber(input.getNumber());
        address.setCep(input.getCep());
        if (input.getCep() != null || input.getNumber() != null) {
            ResponseGeoCodeAPI responseGeoCodeAPI = addressService.getAddress(input.getCep(), input.getNumber());
            address.setLatitude(responseGeoCodeAPI.getLat());
            address.setLongitude(responseGeoCodeAPI.getLon());
        }
        addressRepository.save(address);

        collectionPoint.setAddress(address);

        return "Ponto de coleta cadastrado com sucesso.";
    }}
