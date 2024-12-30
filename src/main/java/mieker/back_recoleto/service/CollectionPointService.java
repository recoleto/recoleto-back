package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.Enum.Role;
import mieker.back_recoleto.entity.Enum.UrbanSolidWaste;
import mieker.back_recoleto.entity.dto.CollectionPointCreateDTO;
import mieker.back_recoleto.entity.dto.CollectionPointDTO;
import mieker.back_recoleto.entity.dto.CompanyDTO;
import mieker.back_recoleto.entity.model.Address;
import mieker.back_recoleto.entity.model.CollectionPoint;
import mieker.back_recoleto.entity.model.Company;
import mieker.back_recoleto.entity.response.ResponseGeoCodeAPI;
import mieker.back_recoleto.exception.NotFoundException;
import mieker.back_recoleto.repository.AddressRepository;
import mieker.back_recoleto.repository.CollectionPointRepository;
import mieker.back_recoleto.repository.CompanyRepository;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    private CollectionPointDTO mapCollectionPointToDTO(CollectionPoint collectionPoint) {
        CollectionPointDTO pointDTO = modelMapper.map(collectionPoint, CollectionPointDTO.class);
        if (collectionPoint.getAddress() != null) {
            pointDTO.setCep(collectionPoint.getAddress().getCep());
            pointDTO.setStreet(collectionPoint.getAddress().getStreet());
            pointDTO.setNumber(collectionPoint.getAddress().getNumber());
            pointDTO.setLatitude(collectionPoint.getAddress().getLatitude());
            pointDTO.setLongitude(collectionPoint.getAddress().getLongitude());
        }
        pointDTO.setPointUUID(collectionPoint.getId());
        pointDTO.setCompanyUUID(collectionPoint.getCompany().getId());
        pointDTO.setCompanyName(collectionPoint.getCompany().getName());
        return pointDTO;
    }

    private void isCollectionPointActive(CollectionPoint collectionPoint) {
        if (!collectionPoint.getStatus()) {
            throw new NotFoundException("Ponto de coleta não encontrado.");
        }
    }

    private static UrbanSolidWaste getUSWType(String type) throws BadRequestException {
        try {
            return UrbanSolidWaste.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Tipo de Resíduo Sólido Urbano inválido: " + type);
        }
    }

    public String createCollectionPoint (CollectionPointCreateDTO input) throws BadRequestException {
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
        collectionPoint.setUrbanSolidWaste(getUSWType(input.getUrbanSolidWaste()));

        Address address = new Address();
        address.setRole(Role.PONTO_DE_COLETA);
        address.setStreet(input.getStreet());
        address.setNumber(input.getNumber());
        address.setCep(input.getCep());
        if (input.getCep() != null && input.getNumber() != null) {
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
        return collectionPointRepository.findAllByStatus(true)
                .stream()
                .map(this::mapCollectionPointToDTO)
                .toList(); // Use `collect(Collectors.toList())` for older Java versions

    }

    public List<CollectionPointDTO> getAllCollectionPointsByCompanyId(UUID companyId) {
        if (companyId == null) {
            companyId = appConfig.companyAuthenticator();
        }

        return collectionPointRepository.findAllByCompanyAndStatus(companyRepository.findCompanyById(companyId), true)
                .stream()
                .map(this::mapCollectionPointToDTO)
                .toList();
    }

    public CollectionPointDTO getCollectionPointById(UUID pointId) {
        CollectionPoint collectionPoint = collectionPointRepository.findById(pointId).orElseThrow(() -> new NotFoundException("Empresa não encontrada."));
        this.isCollectionPointActive(collectionPoint);
        return this.mapCollectionPointToDTO(collectionPoint);
    }

    public List<CollectionPointDTO> getAllCollectionPointsByUSW(UrbanSolidWaste usw) throws BadRequestException {
        usw = getUSWType(usw.toString());
        return collectionPointRepository.findCollectionPointsByUrbanSolidWasteAndStatus(usw, true)
                .stream()
                .map(this::mapCollectionPointToDTO)
                .toList();
    }

    public CollectionPointDTO updateCollectionPoint(UUID pointId, CollectionPointCreateDTO input) throws BadRequestException {
        CollectionPoint collectionPoint = collectionPointRepository.findById(pointId).orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));
        this.isCollectionPointActive(collectionPoint);

        if (input.getNumber() != null) {
            collectionPoint.setName(input.getName());
        }

        if (input.getPhone() != null) {
            collectionPoint.setPhone(input.getPhone());
        }

        if (input.getUrbanSolidWaste() != null) {
            collectionPoint.setUrbanSolidWaste(getUSWType(input.getUrbanSolidWaste()));
        }

        Address address = collectionPoint.getAddress();

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

        collectionPoint.setAddress(address);

        collectionPointRepository.save(collectionPoint);

        return this.mapCollectionPointToDTO(collectionPoint);
    }

    public String deleteCollectionPoint(UUID pointId) {
        CollectionPoint collectionPoint = collectionPointRepository.findById(pointId).orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));
        this.isCollectionPointActive(collectionPoint);
        collectionPoint.setStatus(false);
        collectionPointRepository.save(collectionPoint);
        return "Ponto de coleta deletado com sucesso.";
    }
}

// todo
// fazer uma rota para que os usuários possam ver os pontos de coleta que ele excluiu pra poder reativar

// todo
// fazer uma validação para ver se o id da empresa é o msm da empresa que está no banco
