package mieker.back_recoleto.service;

import mieker.back_recoleto.config.ApplicationConfiguration;
import mieker.back_recoleto.entity.Enum.RequestStatus;
import mieker.back_recoleto.entity.Enum.Role;
import mieker.back_recoleto.entity.dto.request.RequestCreateDTO;
import mieker.back_recoleto.entity.dto.request.RequestDTO;
import mieker.back_recoleto.entity.model.*;
import mieker.back_recoleto.exception.NotAuthorized;
import mieker.back_recoleto.exception.NotFoundException;
import mieker.back_recoleto.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RequestService {
    private final RequestRepository reqRepository;
    private final ApplicationConfiguration appConfig;
    private final UserRepository userRepository;
    private final CollectionPointRepository pointRepository;
    private final OrderRepository orderRepository;
    private final UrbanSolidWasteRepository wasteRepository;
    private final CompanyRepository companyRepository;
    private final NotificationRepository notificationRepository;

    public RequestService(RequestRepository reqRepository, ApplicationConfiguration appConfig, UserRepository userRepository, CollectionPointRepository pointRepository, OrderRepository orderRepository, UrbanSolidWasteRepository wasteRepository, CompanyRepository companyRepository, NotificationRepository notificationRepository) {
        this.reqRepository = reqRepository;
        this.appConfig = appConfig;
        this.userRepository = userRepository;
        this.pointRepository = pointRepository;
        this.orderRepository = orderRepository;
        this.wasteRepository = wasteRepository;
        this.companyRepository = companyRepository;
        this.notificationRepository = notificationRepository;
    }
    protected Integer getPoints(UUID userId) {
        // Default to the authenticated user if no userId is provided
        if (userId == null) {
            userId = appConfig.userAuthenticator();
        }

        // Fetch the user and handle if not found
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("User not found.");
        }

        // Fetch the list of received requests
        List<Request> requestList = reqRepository.findByUserIdAndStatus(user.getId(), RequestStatus.RECEBIDO);

        // Debugging or Logging: Log the requestList if needed (better than System.out.println)
        if (requestList.isEmpty()) {
            System.out.println("No received requests found for the user.");
        } else {
            requestList.forEach(request -> System.out.println("Request ID: " + request.getId() + ", Points: " + request.getPoints()));
        }

        // Sum up the points for all requests
        int totalPoints = requestList.stream()
                .mapToInt(this::calculatePointsForRequest)
                .sum();

        return totalPoints;
    }

    // Helper method to calculate points for a request
    private int calculatePointsForRequest(Request request) {
        return orderRepository.findByRequestId(request.getId()).stream()
                .mapToInt(order -> order.getUsw().getPoints() * order.getQuantity())
                .sum();
    }



    private RequestDTO mapRequestToDTO(Request request) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setRequestId(request.getId());
        requestDTO.setSolicitationNumber(request.getNumber());
        requestDTO.setStatus(request.getStatus());
        requestDTO.setUserId(request.getUser().getId());
        requestDTO.setUserName(request.getUser().getName());

        // Extract Collection Point and Company details for clarity
        CollectionPoint point = request.getPoint();
        Company company = point.getCompany();

        requestDTO.setCollectionPointId(point.getId());
        requestDTO.setCollectionPointName(point.getName());
        requestDTO.setCompanyName(company.getName());
        requestDTO.setCompanyId(company.getId());

        // Map waste list and calculate total points
        List<RequestDTO.Waste> wasteList = orderRepository.findByRequestId(request.getId())
                .stream()
                .map(order -> {
                    RequestDTO.Waste wasteDTO = new RequestDTO.Waste();
                    wasteDTO.setName(order.getUsw().getName());
                    wasteDTO.setQuantity(order.getQuantity());
                    return wasteDTO;
                })
                .toList();

        int totalPoints = orderRepository.findByRequestId(request.getId())
                .stream()
                .mapToInt(order -> order.getUsw().getPoints() * order.getQuantity())
                .sum();

        requestDTO.setWaste(wasteList);
        requestDTO.setPoints(totalPoints);

        return requestDTO;
    }


    public String createRequest(RequestCreateDTO input, UUID pointId) {
        User user = userRepository.findUserById(appConfig.userAuthenticator());
        CollectionPoint point = pointRepository.findById(pointId).orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));
        Long lastRequestNumber = reqRepository.findLastNumber();
        long newRequestNumber = (lastRequestNumber == null) ? 1L : lastRequestNumber + 1;

        Request request = new Request();

        request.setUser(user);
        request.setPoint(point);
        request.setNumber(newRequestNumber);
        request.setStatus(RequestStatus.PENDENTE);

        for (RequestCreateDTO.WasteDTO wasteDTO : input.getWaste()) {
            UrbanSolidWaste usw0 = wasteRepository.findByName(wasteDTO.getName()).orElseThrow(() -> new NotFoundException("Resíduo não encontrado."));
            if (!usw0.getType().equals(point.getUrbanSolidWasteEnum())) {
                throw new DataIntegrityViolationException("Este ponto de coleta não aceita resíduos do tipo " + usw0.getType());
            }
        }

        reqRepository.save(request);

        int totalPoints = 0;
        // Itere sobre a lista de resíduos
        for (RequestCreateDTO.WasteDTO wasteDTO : input.getWaste()) {
            // Crie uma instância de Waste (entidade) para cada resíduo
            UrbanSolidWaste usw = wasteRepository.findByName(wasteDTO.getName()).orElseThrow(() -> new NotFoundException("Resíduo não encontrado."));


            Order order = new Order();
            order.setUsw(usw);
            order.setQuantity(wasteDTO.getQuantity());
            order.setRequest(request); // Relacione com a ordem
            order.setNumber(request.getNumber()); // Relacione com o número do pedido
            totalPoints += usw.getPoints() * wasteDTO.getQuantity(); // Calcule o total de pontos
            // Salve o resíduo no banco
            orderRepository.save(order);
        }

        // 🚀 Criar e salvar a notificação no banco
        Notification notification = new Notification();
        notification.setCompany(point.getCompany());
        notification.setUser(user);
//        System.out.println("Pontos: " + totalPoints);
        notification.setPoints(totalPoints);
        notification.setRequestNumber(request.getNumber());
        notification.setRequest(request);
        notification.setStatus(RequestStatus.PENDENTE);
        notificationRepository.save(notification);

        return "Pedido de descarte solicitado com sucesso!";
    }

    public List<RequestDTO> getAllRequests() {
        List<Request> requestList = reqRepository.findAll();
        return requestList.stream().map(this::mapRequestToDTO).toList();
    }

    public List<RequestDTO> getAllRequestsByPoint(UUID pointId) {
        CollectionPoint point = pointRepository.findById(pointId).orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));
        if (!point.getCompany().getId().equals(appConfig.companyAuthenticator())) {
            throw new NotAuthorized("Você não tem permissão para acessar os pedidos deste ponto de coleta.");
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

    public List<RequestDTO> getAllRequestsByCompany() {
        Company company = companyRepository.findCompanyById(appConfig.companyAuthenticator());

        List<CollectionPoint> pointList = pointRepository.findByCompanyId(company.getId());
        List<Request> requestList = new ArrayList<>();
        for (CollectionPoint point : pointList) {
            List<Request> requestsForPoint = reqRepository.findByPointId(point.getId());
            requestList.addAll(requestsForPoint); // Adiciona à lista acumuladora
        }
        return requestList.stream().map(this::mapRequestToDTO).toList();
    }

    public RequestDTO updateStatusRequest(RequestStatus status, UUID requestId, Role role) {
        Request request = reqRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Pedido de descarte não encontrado."));
        if (role.equals(Role.USUARIO) && !request.getUser().getId().equals(appConfig.userAuthenticator())) {
            throw new NotAuthorized("Você não tem permissão para alterar o status deste pedido.");
        } else if (role.equals(Role.EMPRESA) && !request.getPoint().getCompany().getId().equals(appConfig.companyAuthenticator())) {
            throw new NotAuthorized("Você não tem permissão para alterar o status deste pedido.");
        }

        if (request.getStatus().equals(RequestStatus.RECEBIDO) ||
                request.getStatus().equals(RequestStatus.CANCELADO) ||
                request.getStatus().equals(RequestStatus.REPROVADO)) {
            throw new DataIntegrityViolationException("Este pedido de descarte não pode ser mais alterado.");
        }

        if (status.equals(RequestStatus.RECEBIDO) && role.equals(Role.USUARIO) ||
                status.equals(RequestStatus.REPROVADO) && role.equals(Role.USUARIO) ||
                status.equals(RequestStatus.APROVADO) && role.equals(Role.USUARIO)) {
            throw new DataIntegrityViolationException("Você não pode alterar o status do pedido para " + status);
        }

        if (status.equals(RequestStatus.RECEBIDO) && role.equals(Role.EMPRESA) && request.getStatus().equals(RequestStatus.PENDENTE)) {
            throw new DataIntegrityViolationException("Você não pode alterar o status do pedido para " + status);
        }
//        adicionar validação que o único update q o usuário pode fazer é cancelar o pedido
//        empresa pode aceitar (aprovado) ou recusar (recusado), dps de aprovado ou é (recebido) ou (cancelado)
        request.setStatus(status);
        reqRepository.save(request);

        List<Notification> newNotification = notificationRepository.findByRequestId(requestId);
        int totalPoints;
        if (newNotification.isEmpty()) {
            totalPoints = 0;
        } else {
            Notification newNotif = newNotification.get(0);
            totalPoints = newNotif.getPoints();
        }
        // 🚀 Criar e salvar a notificação no banco
        Notification notification = new Notification();
        notification.setCompany(request.getPoint().getCompany());
        notification.setUser(request.getUser());
//        System.out.println("Pontos: " + totalPoints);
        notification.setPoints(totalPoints);
        notification.setRequestNumber(request.getNumber());
        notification.setRequest(request);
        notification.setStatus(status);
        notificationRepository.save(notification);

        return this.mapRequestToDTO(request);
    }

    public List<RequestDTO> getCollectionPointRequests(UUID pointId) {
        CollectionPoint point = pointRepository.findById(pointId).orElseThrow(() -> new NotFoundException("Ponto de coleta não encontrado."));
        List<Request> requestList = reqRepository.findByPointId(point.getId());
        return requestList.stream().map(this::mapRequestToDTO).toList();
    }

    public List<RequestDTO> getRequestsByStatus(RequestStatus status, Role role) {
        User user;
        Company company;
        List<Request> requestList = new ArrayList<>();
        if (role.equals(Role.USUARIO)) {
            user = userRepository.findUserById(appConfig.userAuthenticator());
            requestList = reqRepository.findByUserIdAndStatus(user.getId(), status);
        } else if (role.equals(Role.EMPRESA)) {
            company = companyRepository.findCompanyById(appConfig.companyAuthenticator());
            List<CollectionPoint> pointList = pointRepository.findByCompanyId(company.getId());
            for (CollectionPoint point : pointList) {
                List<Request> requestsForPoint = reqRepository.findByPointIdAndStatus(point.getId(), status);
                requestList.addAll(requestsForPoint); // Adiciona à lista acumuladora
            }
        }
        return requestList.stream().map(this::mapRequestToDTO).toList();
    }
}
