package orderlistservice.adapters.outbound.repository;

import orderlistservice.domain.model.OrderObject;
import orderlistservice.adapters.outbound.repository.entity.OrderEntity;

public final class OrderMapper {

    private OrderMapper() { }

    /**
     * Wandelt Domain-Objekt -> JPA-Entity
     */
    public static OrderEntity toEntity(OrderObject domain) {
        if (domain == null) {
            return null;
        }
        OrderEntity entity = new OrderEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setItemId(domain.getItemId());
        entity.setCycleDate(domain.getCycleDate());
        entity.setOrderStatus(domain.getOrderStatus().name());
        entity.setItemStatus(domain.isItemStatus());
        entity.setLogId(domain.getLogId());
        return entity;
    }

    /**
     * Wandelt JPA-Entity -> Domain-Objekt
     */
    public static OrderObject toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }
        OrderObject domain = new OrderObject();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setItemId(entity.getItemId());
        domain.setCycleDate(entity.getCycleDate());
        domain.setOrderStatusFromString(entity.getOrderStatus());
        domain.setItemStatus(entity.isItemStatus());
        domain.setLogId(entity.getLogId());
        return domain;
    }
}
