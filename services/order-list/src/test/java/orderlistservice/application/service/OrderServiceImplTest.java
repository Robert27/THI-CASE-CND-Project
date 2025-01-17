package orderlistservice.application.service;

import orderlistservice.application.ports.outbound.*;
import orderlistservice.domain.model.*;
import orderlistservice.domain.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    OrderRepositoryPort orderRepository;

    @Mock
    PriceMonitoringPort priceMonitoring;

    @Mock
    ObjectManagementPort objectManagement;

    @Mock
    OrderExecutionPort orderExecution;

    OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl();
        // Per Reflection oder Konstruktor die Mocks injizieren, je nach Setup:
        ((OrderServiceImpl) orderService).orderRepository = orderRepository;
        ((OrderServiceImpl) orderService).priceMonitoring = priceMonitoring;
        ((OrderServiceImpl) orderService).objectManagement = objectManagement;
        ((OrderServiceImpl) orderService).orderExecution = orderExecution;
    }

    @Test
    void testGenerateOrderList_success() {
        // Given
        Integer userId = 42;
        List<Integer> itemIds = Arrays.asList(1001, 1002);
        String cycleDate = "2025-01-01";

        // Mock: findByItemIdAndUserAndCycleDate -> return empty to simulate no existing order
        when(orderRepository.findByItemIdAndUserAndCycleDate(anyInt(), eq(userId), eq(cycleDate)))
                .thenReturn(Optional.empty());

        // Mock: findByItemIdAndStatusOpenAndUserId -> return empty to simulate no open order
        when(orderRepository.findByItemIdAndStatusOpenAndUserId(anyInt(), eq(userId)))
                .thenReturn(Optional.empty());

        // When
        int resultCode = orderService.generateOrderList(userId, itemIds, cycleDate);

        // Then
        assertEquals(200, resultCode);
        // verify that new orders were saved
        verify(orderRepository, times(2)).save(any(OrderObject.class));
    }

    @Test
    void testGenerateOrderList_existingOpenOrder() {
        // Given
        Integer userId = 42;
        List<Integer> itemIds = Collections.singletonList(1001);
        String cycleDate = "2025-01-01";

        // Wenn es bereits eine offene Bestellung gibt, soll kein neues Objekt angelegt werden
        OrderObject existingOrder = new OrderObject();
        existingOrder.setOrderStatus(OrderStatus.OPEN);

        when(orderRepository.findByItemIdAndUserAndCycleDate(anyInt(), eq(userId), eq(cycleDate)))
                .thenReturn(Optional.empty()); // Kein Duplikat für dieses Datum
        when(orderRepository.findByItemIdAndStatusOpenAndUserId(anyInt(), eq(userId)))
                .thenReturn(Optional.of(existingOrder)); // Offene Bestellung vorhanden

        // When
        int status = orderService.generateOrderList(userId, itemIds, cycleDate);

        // Then
        // Hier könnte man davon ausgehen, dass einfach keine neue Bestellung gespeichert wird,
        // da eine offene bereits existiert.
        assertEquals(200, status);
        verify(orderRepository, never()).save(any(OrderObject.class));
    }

    @Test
    void testGetOpenOrders_noItems() {
        // Given
        Integer userId = 42;
        when(orderRepository.findAllByStatusOpenAndUserId(userId)).thenReturn(Collections.emptyList());

        // When
        List<OrderObject> result = orderService.getOpenOrders(userId);

        // Then
        assertTrue(result.isEmpty());
        verify(objectManagement, never()).getItemDetails(anyList());
        verify(priceMonitoring, never()).checkPrices(anyList());
    }

    @Test
    void testGetOpenOrders_withItems() {
        // Given
        Integer userId = 42;
        OrderObject order1 = new OrderObject();
        order1.setItemId(1001);
        order1.setOrderStatus(OrderStatus.OPEN);
        OrderObject order2 = new OrderObject();
        order2.setItemId(1002);
        order2.setOrderStatus(OrderStatus.OPEN);
        List<OrderObject> openOrders = Arrays.asList(order1, order2);

        when(orderRepository.findAllByStatusOpenAndUserId(userId)).thenReturn(openOrders);

        // Mock ItemDetails
        ItemDetails details1 = new ItemDetails(1001, "Item A", "Desc A", "http://urlA", 10);
        ItemDetails details2 = new ItemDetails(1002, "Item B", "Desc B", "http://urlB", 5);
        when(objectManagement.getItemDetails(anyList())).thenReturn(Arrays.asList(details1, details2));

        // Mock PriceResults
        PriceResult price1 = new PriceResult(1001, "OK", "All good", 1111, BigDecimal.valueOf(12.99), 50);
        PriceResult price2 = new PriceResult(1002, "OK", "All good", 2222, BigDecimal.valueOf(8.49), 20);
        when(priceMonitoring.checkPrices(anyList())).thenReturn(Arrays.asList(price1, price2));

        // When
        List<OrderObject> result = orderService.getOpenOrders(userId);

        // Then
        assertEquals(2, result.size());
        assertEquals("Item A", result.get(0).getItemName());
        assertEquals(BigDecimal.valueOf(12.99), result.get(0).getPrice());
        assertEquals("Item B", result.get(1).getItemName());
        assertEquals(BigDecimal.valueOf(8.49), result.get(1).getPrice());
    }

    @Test
    void testPerformOrder_success() {
        // Given
        Integer userId = 42;
        Integer itemId = 1001;
        int quantity = 5;

        OrderObject existingOrder = new OrderObject();
        existingOrder.setItemId(itemId);
        existingOrder.setOrderStatus(OrderStatus.OPEN);

        // Mock repository
        when(orderRepository.findByItemIdAndStatusOpenAndUserId(itemId, userId))
                .thenReturn(Optional.of(existingOrder));

        // Mock objectManagement
        ItemDetails itemDetails = new ItemDetails(itemId, "Test Item", "Desc", "http://validUrl", 10);
        when(objectManagement.getItemDetails(anyList())).thenReturn(Collections.singletonList(itemDetails));

        // Mock orderExecution
        // Wir tun so, als ob der externe Service 200 zurückgibt
        jakarta.ws.rs.core.Response externalResponse = mock(jakarta.ws.rs.core.Response.class);
        when(externalResponse.getStatus()).thenReturn(200);
        when(orderExecution.executeOrder(eq("http://validUrl"), eq(quantity))).thenReturn(externalResponse);

        // When
        PerformOrderResult result = orderService.performOrder(userId, itemId, quantity, "mockToken");

        // Then
        assertEquals(200, result.getStatusCode());
        assertEquals("Order successfully executed", result.getMessage());
        assertEquals(OrderStatus.DONE, existingOrder.getOrderStatus());
        verify(orderRepository).update(existingOrder);
    }

    @Test
    void testPerformOrder_failed() {
        // Given
        Integer userId = 42;
        Integer itemId = 1001;
        int quantity = 5;

        OrderObject existingOrder = new OrderObject();
        existingOrder.setItemId(itemId);
        existingOrder.setOrderStatus(OrderStatus.OPEN);

        when(orderRepository.findByItemIdAndStatusOpenAndUserId(itemId, userId))
                .thenReturn(Optional.of(existingOrder));

        // Mock objectManagement
        ItemDetails itemDetails = new ItemDetails(itemId, "Test Item", "Desc", "http://validUrl", 10);
        when(objectManagement.getItemDetails(anyList())).thenReturn(Collections.singletonList(itemDetails));

        // Mock orderExecution (geben wir 500 zurück)
        jakarta.ws.rs.core.Response externalResponse = mock(jakarta.ws.rs.core.Response.class);
        when(externalResponse.getStatus()).thenReturn(500);
        when(orderExecution.executeOrder(eq("http://validUrl"), eq(quantity))).thenReturn(externalResponse);

        // When
        PerformOrderResult result = orderService.performOrder(userId, itemId, quantity, "mockToken");

        // Then
        assertEquals(500, result.getStatusCode());
        assertEquals("Order execution failed", result.getMessage());
        assertEquals(OrderStatus.FAILED, existingOrder.getOrderStatus());
        verify(orderRepository).update(existingOrder);
    }

    @Test
    void testAbortOrder() {
        // Given
        Integer userId = 42;
        Integer itemId = 999;
        OrderObject existingOrder = new OrderObject();
        existingOrder.setItemId(itemId);
        existingOrder.setOrderStatus(OrderStatus.OPEN);

        when(orderRepository.findByItemIdAndStatusOpenAndUserId(itemId, userId))
                .thenReturn(Optional.of(existingOrder));

        // When
        orderService.abortOrder(userId, itemId);

        // Then
        assertEquals(OrderStatus.ABORTED, existingOrder.getOrderStatus());
        verify(orderRepository).update(existingOrder);
    }
}
