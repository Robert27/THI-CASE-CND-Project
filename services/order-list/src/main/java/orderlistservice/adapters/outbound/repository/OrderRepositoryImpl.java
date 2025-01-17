package orderlistservice.adapters.outbound.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import orderlistservice.application.ports.outbound.OrderRepositoryPort;
import orderlistservice.domain.model.OrderObject;
import orderlistservice.adapters.outbound.repository.entity.OrderEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderRepositoryImpl implements OrderRepositoryPort {

    @Inject
    OrderPanacheRepository panacheRepo;

    @Override
    public Optional<OrderObject> findByItemIdAndUserAndCycleDate(Integer itemId, Integer userId, String cycleDate) {
        OrderEntity entity = panacheRepo.find(
                "itemId = ?1 AND userId = ?2 AND cycleDate = ?3",
                itemId, userId, cycleDate
        ).firstResult();
        return Optional.ofNullable(OrderMapper.toDomain(entity));
    }

    @Override
    public Optional<OrderObject> findByItemIdAndStatusOpenAndUserId(Integer itemId, Integer userId) {
        OrderEntity entity = panacheRepo.find(
                "itemId = ?1 AND userId = ?2 AND orderStatus = ?3",
                itemId, userId, "OPEN"
        ).firstResult();
        return Optional.ofNullable(OrderMapper.toDomain(entity));
    }

    @Override
    @Transactional
    public void save(OrderObject order) {
        OrderEntity entity = OrderMapper.toEntity(order);
        panacheRepo.persist(entity);
        order.setId(entity.getId());
    }

    @Override
    @Transactional
    public void update(OrderObject order) {
        if (order.getId() == null) {
            save(order);
            return;
        }
        OrderEntity entity = OrderMapper.toEntity(order);
        OrderEntity managed = panacheRepo.getEntityManager().merge(entity);
        order.setId(managed.getId());
    }

    @Override
    public List<OrderObject> findAllByStatusOpenAndUserId(Integer userId) {
        List<OrderEntity> entities = panacheRepo.find(
                "userId = ?1 AND orderStatus = ?2",
                userId, "OPEN"
        ).list();
        return entities.stream()
                .map(OrderMapper::toDomain)
                .collect(Collectors.toList());
    }
}