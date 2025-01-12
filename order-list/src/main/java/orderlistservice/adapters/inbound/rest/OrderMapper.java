package orderlistservice.adapters.inbound.rest;

import orderlistservice.adapters.inbound.rest.dto.*;
import orderlistservice.domain.model.OrderObject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper, um OrderObject (Domain) in OrderDto (Controller/REST) zu übersetzen.
 */
public final class OrderMapper {

    private OrderMapper() {
        // Utility-Klasse, kein Instanzieren
    }

    public static GetOpenOrdersResponse toDto(OrderObject order) {
        if (order == null) return null;

        GetOpenOrdersResponse dto = new GetOpenOrdersResponse();
        dto.setItemId(order.getItemId());
        dto.setItemName(order.getItemName());
        dto.setUrl(order.getUrl());
        dto.setDescription(order.getDescription());
        dto.setPrice(order.getPrice());
        dto.setOrderQuantity(order.getOrderQuantity());
        dto.setAvailabilityQuantity(order.getAvailability());
        dto.setCycleDate(order.getCycleDate() != null ? order.getCycleDate().toString() : null);
        dto.setStatusMessage(order.getStatusMessage());
        return dto;
    }

    /**
     * Wandelt eine LISTE von Domain-Objekten in eine LISTE von OrderDto.
     */
    public static List<GetOpenOrdersResponse> toDtoList(List<OrderObject> orders) {
        if (orders == null) {
            return List.of();
        }
        return orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }
}
