package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.Enum.Role;
import mieker.back_recoleto.entity.dto.CollectionPointCreateDTO;
import mieker.back_recoleto.entity.dto.CollectionPointDTO;
import mieker.back_recoleto.entity.dto.CompanyDTO;
import mieker.back_recoleto.entity.model.Address;
import mieker.back_recoleto.entity.model.CollectionPoint;
import mieker.back_recoleto.entity.model.Company;
import mieker.back_recoleto.entity.response.ResponseGeoCodeAPI;
import mieker.back_recoleto.repository.AddressRepository;
import mieker.back_recoleto.repository.CollectionPointRepository;
import mieker.back_recoleto.repository.CompanyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CollectionPointService {
    private final CollectionPointRepository collectionPointRepository;
    private final ApplicationConfiguration appConfig;
    private final CompanyRepository companyRepository;
    private final AddressService addressService;
    private final AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CollectionPointService(CollectionPointRepository collectionPointRepository, ApplicationConfiguration appConfig, CompanyRepository companyRepository, AddressService addressService, AddressRepository addressRepository) {
        this.collectionPointRepository = collectionPointRepository;
        this.appConfig = appConfig;
        this.companyRepository = companyRepository;
        this.addressService = addressService;
        this.addressRepository = addressRepository;
    }

    public String createCollectionPoint (CollectionPointCreateDTO input) {
        UUID companyId = appConfig.companyAuthenticator();
        Company company = companyRepository.findCompanyById(companyId);

        if (company == null) {
            return "Empresa não encontrada.";
        }

        CollectionPoint collectionPoint = new CollectionPoint();
        collectionPoint.setCompany(company);
        collectionPoint.setName(input.getName());
        collectionPoint.setPhone(input.getPhone());
        collectionPoint.setStatus(true);
        collectionPoint.setUrbanSolidWaste(input.getUrbanSolidWaste());
//        TODO:
//        fazer uma verificação se existe esse USW

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

        collectionPointRepository.save(collectionPoint);

        return "Ponto de coleta cadastrado com sucesso.";
    }

    public List<CollectionPointDTO> getAllCollectionPoints() {
//        UUID companyId = appConfig.companyAuthenticator();
//        Company company = companyRepository.findCompanyById(companyId);
//
//        if (company == null) {
//            return null;
//        }

        System.out.println(collectionPointRepository.findAll());

        return collectionPointRepository.findAll()
                .stream()
                .map(collectionPoint ->
                {
                    CollectionPointDTO pointDTO = modelMapper.map(collectionPoint, CollectionPointDTO.class);
                    if (collectionPoint.getAddress() != null) {
                        pointDTO.setCep(collectionPoint.getAddress().getCep());
                        pointDTO.setStreet(collectionPoint.getAddress().getStreet());
                        pointDTO.setNumber(collectionPoint.getAddress().getNumber());
                    }
                    pointDTO.setPointUUID(collectionPoint.getId());
                    pointDTO.setCompanyUUID(collectionPoint.getCompany().getId());
                    pointDTO.setCompanyName(collectionPoint.getCompany().getName());
                    return pointDTO;
                })
                .toList(); // Use `collect(Collectors.toList())` for older Java versions


//        List<CollectionPoint> collectionPointList = collectionPointRepository.findAllByCompany(company);
//
//        return CollectionPointDTO.convertCollectionPointListToDTO(collectionPointList);
    }
}
