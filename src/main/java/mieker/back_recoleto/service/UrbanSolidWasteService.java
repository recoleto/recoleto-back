package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.dto.UrbanSolidWasteCreateDTO;
import mieker.back_recoleto.entity.model.UrbanSolidWaste;
import mieker.back_recoleto.repository.UrbanSolidWasteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
}
