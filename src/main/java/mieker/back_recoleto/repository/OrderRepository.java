package mieker.back_recoleto.repository;

import mieker.back_recoleto.entity.model.Order;
import mieker.back_recoleto.entity.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByRequestId(UUID requestId);
}
