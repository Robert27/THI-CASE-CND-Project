package orderlistservice.adapters.outbound.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import orderlistservice.adapters.outbound.repository.entity.OrderEntity;

import java.util.List;
/**
 * Panache-Repository für OrderEntity.
 */
@ApplicationScoped
public class OrderPanacheRepository implements PanacheRepository<OrderEntity> {

//    public List<OrderEntity> findAllByUserAndStatus(Integer userId, String status) {
//        return list("user_id = ?1 AND order_status = ?2", userId, status);
//    }
//    public List<OrderEntity> findAllByUser(Integer userId) {
//        return list("user_id", userId);
//    }

}
