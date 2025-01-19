package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.Enum.RequestStatus;
import mieker.back_recoleto.entity.dto.RequestCreateDTO;
import mieker.back_recoleto.entity.model.*;
import mieker.back_recoleto.exception.NotFoundException;
import mieker.back_recoleto.repository.*;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    private final RequestRepository reqRepository;
    private final ApplicationConfiguration appConfig;
    private final UserRepository userRepository;
    private final CollectionPointRepository pointRepository;
    private final OrderRepository orderRepository;
    private final UrbanSolidWasteRepository wasteRepository;

    public RequestService(RequestRepository reqRepository, ApplicationConfiguration appConfig, UserRepository userRepository, CollectionPointRepository pointRepository, OrderRepository orderRepository, UrbanSolidWasteRepository wasteRepository) {
        this.reqRepository = reqRepository;
        this.appConfig = appConfig;
        this.userRepository = userRepository;
        this.pointRepository = pointRepository;
        this.orderRepository = orderRepository;
        this.wasteRepository = wasteRepository;
    }

    public String createRequest(RequestCreateDTO input) {
        System.out.println("aq?");
        User user = userRepository.findUserById(appConfig.userAuthenticator());
        CollectionPoint point = pointRepository.findById(input.getCollectionPointId()).orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));
        Long number = reqRepository.findLastNumber();
        if (number == null) {
            number = 0L;
        }

        Request request = new Request();

        request.setUser(user);
        request.setPoint(point);
        request.setNumber(number + 1);
        request.setStatus(RequestStatus.PENDENTE);

        reqRepository.save(request);

        // Itere sobre a lista de resíduos
        for (RequestCreateDTO.WasteDTO wasteDTO : input.getWaste()) {
            // Crie uma instância de Waste (entidade) para cada resíduo
            UrbanSolidWaste usw = wasteRepository.findByName(wasteDTO.getName()).orElseThrow(() -> new NotFoundException("Resíduo não encontrado."));

            Order order = new Order();
            order.setUsw(usw);
            order.setQuantity(wasteDTO.getQuantity());
            order.setRequest(request); // Relacione com a ordem
            order.setNumber(request.getNumber()); // Relacione com o número do pedido
            // Salve o resíduo no banco
            orderRepository.save(order);
        }

        return "Pedido de descarte solicitado com sucesso!";
    }
}
