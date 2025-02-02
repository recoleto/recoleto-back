package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;
import mieker.back_recoleto.entity.dto.urbanSolidWaste.UrbanSolidWasteCreateDTO;
import mieker.back_recoleto.entity.dto.urbanSolidWaste.UrbanSolidWasteDTO;
import mieker.back_recoleto.entity.dto.urbanSolidWaste.UrbanSolidWasteUpdateDTO;
import mieker.back_recoleto.entity.model.UrbanSolidWaste;
import mieker.back_recoleto.exception.NotFoundException;
import mieker.back_recoleto.repository.UrbanSolidWasteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UrbanSolidWasteService {
    private final UrbanSolidWasteRepository urbanSolidWasteRepository;
    private final ApplicationConfiguration appConfig;

    @Autowired
    private ModelMapper modelMapper;
    public UrbanSolidWasteService(UrbanSolidWasteRepository urbanSolidWasteRepository, ApplicationConfiguration appConfig) {
        this.urbanSolidWasteRepository = urbanSolidWasteRepository;
        this.appConfig = appConfig;
    }

    public String createUrbanSolidWaste(UrbanSolidWasteCreateDTO input, String user) {
        if (urbanSolidWasteRepository.existsByName(input.getName())) {
            throw new DataIntegrityViolationException("O resíduo " + input.getName() + " já está cadastrado no sistema. Por favor, consulte a lista de resíduos cadastrados.");
        }
        UrbanSolidWaste usw = modelMapper.map(input, UrbanSolidWaste.class);
        if (user.equals("admin")) {
            usw.setCreatedBy(appConfig.userAuthenticator());
        } else {
            usw.setCreatedBy(appConfig.companyAuthenticator());
        }
        urbanSolidWasteRepository.save(usw);
        return "Resíduo sólido urbano criado com sucesso!";
    }

    public List<UrbanSolidWasteDTO> getUrbanSolidWasteList() {
        List<UrbanSolidWaste> uswList = urbanSolidWasteRepository.findAll();
        return uswList.stream().map(usw -> modelMapper.map(usw, UrbanSolidWasteDTO.class)).toList();
    }

    public UrbanSolidWasteDTO getUrbanSolidWasteById(UUID id) {
        UrbanSolidWaste usw = urbanSolidWasteRepository.findById(id).orElseThrow(() -> new NotFoundException("Resíduo sólido urbano não encontrado."));
        return modelMapper.map(usw, UrbanSolidWasteDTO.class);
    }

    public UrbanSolidWasteDTO getUrbanSolidWasteByName(String name) {
        UrbanSolidWaste usw = urbanSolidWasteRepository.findByName(name).orElseThrow(() -> new NotFoundException("Resíduo sólido urbano não castrado."));
        return modelMapper.map(usw, UrbanSolidWasteDTO.class);
    }

    public List<UrbanSolidWasteDTO> getUrbanSolidWasteByType(UrbanSolidWasteEnum type) {
        List<UrbanSolidWaste> uswList = urbanSolidWasteRepository.findByType(type);
        return uswList.stream().map(usw -> modelMapper.map(usw, UrbanSolidWasteDTO.class)).toList();
    }

    public UrbanSolidWasteDTO updateUrbanSolidWaste(UUID id, UrbanSolidWasteUpdateDTO input) {
        UrbanSolidWaste usw = urbanSolidWasteRepository.findById(id).orElseThrow(() -> new NotFoundException("Resíduo sólido urbano não encontrado."));

        if (input.getPoints() != null) {
            usw.setPoints(input.getPoints());
        }
        if (input.getName() != null && !input.getName().equals(usw.getName())) {
            usw.setName(input.getName());
            if (urbanSolidWasteRepository.existsByName(input.getName())) {
                throw new DataIntegrityViolationException("O resíduo " + input.getName() + " já está cadastrado no sistema. Por favor, consulte a lista de resíduos cadastrados.");
            }
        }
        urbanSolidWasteRepository.save(usw);
        return modelMapper.map(usw, UrbanSolidWasteDTO.class);
    }
}
