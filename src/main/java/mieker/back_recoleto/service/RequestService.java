package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.Enum.RequestStatus;
import mieker.back_recoleto.entity.Enum.Role;
import mieker.back_recoleto.entity.dto.RequestCreateDTO;
import mieker.back_recoleto.entity.dto.RequestDTO;
import mieker.back_recoleto.entity.model.*;
import mieker.back_recoleto.exception.AuthorizationDeniedException;
import mieker.back_recoleto.exception.NotFoundException;
import mieker.back_recoleto.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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

    private RequestDTO mapRequestToDTO(Request request) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setRequestId(request.getId());
        requestDTO.setSolicitationNumber(request.getNumber());
        requestDTO.setStatus(request.getStatus());
        requestDTO.setUserId(request.getUser().getId());
        requestDTO.setUserName(request.getUser().getName());
        requestDTO.setCollectionPointId(request.getPoint().getId());
        requestDTO.setCollectionPointName(request.getPoint().getName());
        requestDTO.setCompanyName(request.getPoint().getCompany().getName());
        requestDTO.setCompanyId(request.getPoint().getCompany().getId());

        AtomicReference<Integer> points = new AtomicReference<>(0);
        requestDTO.setWaste(orderRepository.findByRequestId(request.getId()).stream().map(order -> {
            RequestDTO.Waste wasteDTO = new RequestDTO.Waste();
            wasteDTO.setName(order.getUsw().getName());
            wasteDTO.setQuantity(order.getQuantity());
            points.updateAndGet(v -> v + order.getUsw().getPoints() * order.getQuantity());
            return wasteDTO;
        }).toList());

        requestDTO.setPoints(points.get());
        return requestDTO;
    }

    public String createRequest(RequestCreateDTO input) {
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

    public List<RequestDTO> getAllRequests() {
        List<Request> requestList = reqRepository.findAll();
        return requestList.stream().map(this::mapRequestToDTO).toList();
    }

    public List<RequestDTO> getAllRequestsByPoint(UUID pointId) {
        CollectionPoint point = pointRepository.findById(pointId).orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));
        if (!point.getCompany().getId().equals(appConfig.companyAuthenticator())) {
            throw new AuthorizationDeniedException("Você não tem permissão para acessar os pedidos deste ponto de coleta.");
        }
        List<Request> requestList = reqRepository.findByPointId(point.getId());
        return requestList.stream().map(this::mapRequestToDTO).toList();
    }

    public RequestDTO getRequestById(UUID requestId) {
        Request request = reqRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Pedido de descarte não encontrado."));
        return this.mapRequestToDTO(request);
    }

    public List<RequestDTO> getAllRequestsByUser() {
        User user = userRepository.findUserById(appConfig.userAuthenticator());
        List<Request> requestList = reqRepository.findByUserId(user.getId());
        return requestList.stream().map(this::mapRequestToDTO).toList();
    }
}
