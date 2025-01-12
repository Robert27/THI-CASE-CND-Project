package orderlistservice.application.service;

import orderlistservice.application.ports.outbound.ObjectManagementPort;
import orderlistservice.application.ports.outbound.OrderExecutionPort;
import orderlistservice.application.ports.outbound.OrderRepositoryPort;
import orderlistservice.application.ports.outbound.PriceMonitoringPort;
import orderlistservice.domain.model.OrderObject;
import orderlistservice.domain.model.OrderStatus;
import orderlistservice.domain.model.ItemDetails;
import orderlistservice.domain.model.PriceResult;
import orderlistservice.domain.model.PerformOrderResult;
import orderlistservice.domain.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Inject
    OrderRepositoryPort orderRepository;

    @Inject
    PriceMonitoringPort priceMonitoring;

    @Inject
    ObjectManagementPort objectManagement;

    @Inject
    OrderExecutionPort orderExecution;

    @Override
    @Transactional
    public int generateOrderList(Integer userId, List<Integer> itemIds, String cycleDate) {
        try {
            for (Integer itemId : itemIds) {

                // Prüfen, ob es bereits einen Eintrag (egal welcher Status) mit derselben itemId + cycleDate + userId gibt

                Optional<OrderObject> sameDayOrder = orderRepository.findByItemIdAndUserAndCycleDate(itemId, userId, cycleDate);
                if (sameDayOrder.isPresent()) {
                    LOGGER.info("Bestellung existiert bereits (itemId {}, userId {}, cycleDate {}).", itemId, userId, cycleDate);
                    continue;
                }

                // (b) Prüfen, ob es eine offene Bestellung gibt
                Optional<OrderObject> existingOpen = orderRepository.findByItemIdAndStatusOpenAndUserId(itemId, userId);
                if (existingOpen.isPresent()) {
                    LOGGER.info("Offene Bestellung für Artikel-ID {} bereits vorhanden (User {}).", itemId, userId);
                    continue;
                }

                // Anlegen:
                OrderObject newOrder = new OrderObject();
                newOrder.setUserId(userId);
                newOrder.setItemId(itemId);
                newOrder.setCycleDate(cycleDate);
                newOrder.setOrderStatus(OrderStatus.OPEN);
                newOrder.setItemStatus(true);
                orderRepository.save(newOrder);
                LOGGER.info("Neue Bestellung für Artikel-ID {} (User {}, Datum {}) erstellt.", itemId, userId, cycleDate);

            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return 500;
        }
        return 200;
    }

    @Override
    @Transactional
    public List<OrderObject> getOpenOrders(Integer userId) {
        // Lade alle offenen Bestellungen
        List<OrderObject> openOrders = orderRepository.findAllByStatusOpenAndUserId(userId);
        if (openOrders.isEmpty()) {
            LOGGER.info("Keine offenen Bestellungen für userId={} gefunden.", userId);
            return Collections.emptyList();
        }

        // Sammle alle ItemIds
        List<Integer> itemIds = openOrders.stream()
                .map(OrderObject::getItemId)
                .distinct()
                .collect(Collectors.toList());

        // Hole Item-Details Name, URL, Beschreibung, quantity, etc.
        List<ItemDetails> itemDetailsList;
        try {
            itemDetailsList = objectManagement.getItemDetails(itemIds);
            LOGGER.info("ItemDetails erfolgreich abgerufen: {}", itemDetailsList.size());
        } catch (Exception e) {
            LOGGER.error("Fehler beim Abrufen der ItemDetails:", e);
            return Collections.emptyList();
        }
        Map<Integer, ItemDetails> itemDetailsMap = itemDetailsList.stream()
                .collect(Collectors.toMap(ItemDetails::getItemId, d -> d));


        // Hole Preisdaten vom PriceMonitoring
        List<PriceResult> priceResults;
        try {
            priceResults = priceMonitoring.checkPrices(itemIds);
            LOGGER.info("Preis-Daten erfolgreich abgerufen: {}", priceResults.size());
        } catch (Exception e) {
            LOGGER.error("Fehler beim Abrufen der Preisdaten:", e);
            // Optional: Alle Orders auf itemStatus=false setzen?
            for (OrderObject order : openOrders) {
                order.markItemUnavailable();
            }
            return openOrders;
        }
        Map<Integer, PriceResult> priceMap = priceResults.stream()
                .collect(Collectors.toMap(PriceResult::getItemId, p -> p));

        // Anreichern der OrderObjects mit ItemDetails und PriceResult
        for (OrderObject order : openOrders) {
            Integer itemId = order.getItemId();
            ItemDetails details = itemDetailsMap.get(itemId);
            if (details != null) {
                order.setItemName(details.getName());
                order.setUrl(details.getUrl());
                order.setDescription(details.getDescription());
                order.setOrderQuantity(details.getQuantity());
            }
            PriceResult priceResult = priceMap.get(itemId);
            if (priceResult == null) {
                order.markItemUnavailable();
                order.setStatusMessage("Error contacting Price Check Service");
            } else if (!priceResult.isOk()) {
                order.markItemUnavailable();
                order.setStatusMessage(priceResult.getMessage());  // Bsp: "Item discontinued"
            } else {
                order.markItemAvailable();
                order.setPrice(priceResult.getPrice() != null ? priceResult.getPrice() : BigDecimal.ZERO);
                order.setAvailability(priceResult.getAvailability());
                order.setLogId(priceResult.getLogId());
                order.setStatusMessage("OK");
            }
        }
        return openOrders;
    }

    @Override
    @Transactional
    public PerformOrderResult performOrder(Integer userId, Integer itemId, int quantity, String authToken) {
        // 1) OrderObject suchen
        Optional<OrderObject> orderOpt = orderRepository.findByItemIdAndStatusOpenAndUserId(itemId, userId);
        if (orderOpt.isEmpty()) {
            return new PerformOrderResult(404, "No open order found for itemId " + itemId);
        }
        OrderObject order = orderOpt.get();

        // 2) Hole Item-Details (z.B. URL) aus Domain-Port
        List<ItemDetails> itemDetailsList = objectManagement.getItemDetails(Collections.singletonList(itemId));
        ItemDetails itemDetails = itemDetailsList.stream()
                .filter(d -> d.getItemId().equals(itemId))
                .findFirst()
                .orElse(null);

        if (itemDetails == null || itemDetails.getUrl() == null || itemDetails.getUrl().isEmpty()) {
            return new PerformOrderResult(400, "Invalid item URL for itemId " + itemId);
        }

        // Externen Bestellservice aufrufen
        try {
            // Baue Request-Objekt (Outbound-Adapter), übergib quantity/auth
            var externalResponse = orderExecution.executeOrder(itemDetails.getUrl(), quantity);

            if (externalResponse.getStatus() >= 200 && externalResponse.getStatus() < 300) {
                order.setOrderStatus(OrderStatus.DONE);
                orderRepository.update(order);
                return new PerformOrderResult(externalResponse.getStatus(), "Order successfully executed");
            } else {
                order.setOrderStatus(OrderStatus.FAILED);
                orderRepository.update(order);
                return new PerformOrderResult(externalResponse.getStatus(), "Order execution failed");
            }

        } catch (Exception e) {
            LOGGER.error("Fehler bei der Ausführung der Bestellung für Artikel-ID {}: ", itemId, e);
            order.setOrderStatus(OrderStatus.FAILED);
            orderRepository.update(order);
            return new PerformOrderResult(500, "Order execution failed due to an exception");
        }
    }


    @Override
    @Transactional
    public void abortOrder(Integer userId, Integer itemId) {
        // Nur Orders des eingeloggten Users
        Optional<OrderObject> orderOpt = orderRepository.findByItemIdAndStatusOpenAndUserId(itemId, userId);
        if (orderOpt.isPresent()) {
            OrderObject order = orderOpt.get();
            order.setOrderStatus(OrderStatus.ABORTED);
            orderRepository.update(order);
            LOGGER.info("Bestellung (itemId={}, userId={}) abgebrochen.", itemId, userId);
        } else {
            LOGGER.warn("Keine offene Bestellung gefunden (itemId={}, userId={}).", itemId, userId);
        }
    }

}