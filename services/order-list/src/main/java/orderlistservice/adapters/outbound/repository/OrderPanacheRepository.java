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


}
