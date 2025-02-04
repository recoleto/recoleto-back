package mieker.back_recoleto.bootstrap;

import mieker.back_recoleto.entity.Enum.UrbanSolidWasteEnum;
import mieker.back_recoleto.entity.model.UrbanSolidWaste;
import mieker.back_recoleto.entity.model.User;
import mieker.back_recoleto.repository.UrbanSolidWasteRepository;
import mieker.back_recoleto.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UrbanSolidWasteSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final UrbanSolidWasteRepository urbanSolidWasteRepository;

    public UrbanSolidWasteSeeder(UserRepository userRepository, UrbanSolidWasteRepository urbanSolidWasteRepository) {
        this.userRepository = userRepository;
        this.urbanSolidWasteRepository = urbanSolidWasteRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createUSW();
    }

    private void createUSW() {
        Optional<User> userOptional = userRepository.findByEmail("admin@gmail.com");
        if (userOptional.isEmpty()) {
            System.out.println("Admin user not found. Skipping UrbanSolidWasteSeeder.");
            return;
        }

        UUID createdBy = userOptional.get().getId();

        List<UrbanSolidWaste> wastes = List.of(
                new UrbanSolidWaste("Pilha", UrbanSolidWasteEnum.LIXO_ELETRONICO, 15, createdBy),
                new UrbanSolidWaste("Bateria", UrbanSolidWasteEnum.LIXO_ELETRONICO, 10, createdBy),
                new UrbanSolidWaste("Seringa", UrbanSolidWasteEnum.RESIDUOS_CONTAMINANTES, 20, createdBy),
                new UrbanSolidWaste("Remédio", UrbanSolidWasteEnum.RESIDUOS_CONTAMINANTES, 10, createdBy),
                new UrbanSolidWaste("Celular", UrbanSolidWasteEnum.LIXO_ELETRONICO, 300, createdBy),
                new UrbanSolidWaste("Vidro", UrbanSolidWasteEnum.RESIDUOS_CORTANTES, 5, createdBy),
                new UrbanSolidWaste("Lâmina", UrbanSolidWasteEnum.RESIDUOS_CORTANTES, 10, createdBy),
                new UrbanSolidWaste("Eletrodoméstico", UrbanSolidWasteEnum.LIXO_ELETRONICO, 150, createdBy),

                // Resíduos CORTANTES E PERFURANTES
                new UrbanSolidWaste("Lâmina de barbear", UrbanSolidWasteEnum.RESIDUOS_CORTANTES, 15, createdBy),
                new UrbanSolidWaste("Agulha", UrbanSolidWasteEnum.RESIDUOS_CORTANTES, 20, createdBy),
                new UrbanSolidWaste("Caco de vidro", UrbanSolidWasteEnum.RESIDUOS_CORTANTES, 10, createdBy),
                new UrbanSolidWaste("Tesoura", UrbanSolidWasteEnum.RESIDUOS_CORTANTES, 12, createdBy),
                new UrbanSolidWaste("Serra", UrbanSolidWasteEnum.RESIDUOS_CORTANTES, 25, createdBy),

                // Resíduos CONTAMINANTES
                new UrbanSolidWaste("Máscara descartável", UrbanSolidWasteEnum.RESIDUOS_CONTAMINANTES, 5, createdBy),
                new UrbanSolidWaste("Luva hospitalar", UrbanSolidWasteEnum.RESIDUOS_CONTAMINANTES, 8, createdBy),
                new UrbanSolidWaste("Gaze suja", UrbanSolidWasteEnum.RESIDUOS_CONTAMINANTES, 7, createdBy),
                new UrbanSolidWaste("Algodão com sangue", UrbanSolidWasteEnum.RESIDUOS_CONTAMINANTES, 9, createdBy),
                new UrbanSolidWaste("Kit de hemodiálise", UrbanSolidWasteEnum.RESIDUOS_CONTAMINANTES, 30, createdBy),

                // Lixo ELETRÔNICO
                new UrbanSolidWaste("Placa-mãe", UrbanSolidWasteEnum.LIXO_ELETRONICO, 200, createdBy),
                new UrbanSolidWaste("Carregador", UrbanSolidWasteEnum.LIXO_ELETRONICO, 50, createdBy),
                new UrbanSolidWaste("HD/SSD", UrbanSolidWasteEnum.LIXO_ELETRONICO, 150, createdBy),
                new UrbanSolidWaste("Fonte de computador", UrbanSolidWasteEnum.LIXO_ELETRONICO, 180, createdBy),
                new UrbanSolidWaste("Teclado", UrbanSolidWasteEnum.LIXO_ELETRONICO, 80, createdBy),
                new UrbanSolidWaste("Mouse", UrbanSolidWasteEnum.LIXO_ELETRONICO, 40, createdBy),
                new UrbanSolidWaste("Impressora", UrbanSolidWasteEnum.LIXO_ELETRONICO, 250, createdBy),
                new UrbanSolidWaste("Monitor quebrado", UrbanSolidWasteEnum.LIXO_ELETRONICO, 300, createdBy),
                new UrbanSolidWaste("Cabo USB", UrbanSolidWasteEnum.LIXO_ELETRONICO, 30, createdBy),
                new UrbanSolidWaste("Fone de ouvido", UrbanSolidWasteEnum.LIXO_ELETRONICO, 60, createdBy)
        );

        for (UrbanSolidWaste waste : wastes) {
            if (urbanSolidWasteRepository.findByName(waste.getName()).isEmpty()) {
                urbanSolidWasteRepository.save(waste);
            }
        }
    }
}
