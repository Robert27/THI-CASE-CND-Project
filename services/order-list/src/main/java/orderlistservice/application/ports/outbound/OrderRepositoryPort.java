package orderlistservice.application.ports.outbound;

import orderlistservice.domain.model.OrderObject;

import java.util.List;
import java.util.Optional;

/**
 * Outbound Port für das Order Repository.
 */
public interface OrderRepositoryPort {
    Optional<OrderObject> findByItemIdAndUserAndCycleDate(Integer itemId, Integer userId, String cycleDate);
    Optional<OrderObject> findByItemIdAndStatusOpenAndUserId(Integer itemId, Integer userId);
    List<OrderObject> findAllByStatusOpenAndUserId(Integer userId);
    void save(OrderObject orderObject);
    void update(OrderObject orderObject);
}
