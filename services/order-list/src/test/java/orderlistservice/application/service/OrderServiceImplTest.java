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
        // Per Reflection oder direktem Setter die Mocks "injizieren"
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

        // simulate no existing order for the same itemId, userId, cycleDate
        when(orderRepository.findByItemIdAndUserAndCycleDate(anyInt(), eq(userId), eq(cycleDate)))
                .thenReturn(Optional.empty());

        // simulate no open order for those itemIds
        when(orderRepository.findByItemIdAndStatusOpenAndUserId(anyInt(), eq(userId)))
                .thenReturn(Optional.empty());

        // When
        int resultCode = orderService.generateOrderList(userId, itemIds, cycleDate);

        // Then
        assertEquals(200, resultCode);
        // verify that new orders were saved for each itemId
        verify(orderRepository, times(2)).save(any(OrderObject.class));
    }

    @Test
    void testGenerateOrderList_existingOpenOrder() {
        // Given
        Integer userId = 42;
        List<Integer> itemIds = Collections.singletonList(1001);
        String cycleDate = "2025-01-01";

        // Vorhandene offene Bestellung
        OrderObject existingOrder = new OrderObject();
        existingOrder.setOrderStatus(OrderStatus.OPEN);

        // Kein Duplikat an demselben Tag
        when(orderRepository.findByItemIdAndUserAndCycleDate(anyInt(), eq(userId), eq(cycleDate)))
                .thenReturn(Optional.empty());

        // Es gibt aber bereits eine offene Bestellung
        when(orderRepository.findByItemIdAndStatusOpenAndUserId(anyInt(), eq(userId)))
                .thenReturn(Optional.of(existingOrder));

        // When
        int status = orderService.generateOrderList(userId, itemIds, cycleDate);

        // Then
        // Es wird keine neue Bestellung gespeichert
        assertEquals(200, status);
        verify(orderRepository, never()).save(any(OrderObject.class));
    }

    @Test
    void testGetOpenOrders_noItems() {
        // Given
        Integer userId = 42;
        when(orderRepository.findAllByStatusOpenAndUserId(userId))
                .thenReturn(Collections.emptyList());

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

        // Mock: vorhandene offene Order
        when(orderRepository.findByItemIdAndStatusOpenAndUserId(itemId, userId))
                .thenReturn(Optional.of(existingOrder));

        // Mock: objectManagement gibt valides ItemDetails zurück
        ItemDetails itemDetails = new ItemDetails(itemId, "Test Item", "Desc", "http://validUrl", 10);
        when(objectManagement.getItemDetails(anyList()))
                .thenReturn(Collections.singletonList(itemDetails));

        // Mock: orderExecution => true (Erfolg)
        when(orderExecution.executeOrder("http://validUrl", quantity)).thenReturn(true);

        // When
        // NEUE Signatur (ohne authToken)
        PerformOrderResult result = orderService.performOrder(userId, itemId, quantity);

        // Then
        // Prüfen: Domain-Result => 200
        assertEquals(200, result.getStatusCode());
        assertEquals("Order successfully executed", result.getMessage());

        // Bestellung sollte auf DONE gesetzt werden
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

        // Mock: objectManagement
        ItemDetails itemDetails = new ItemDetails(itemId, "Test Item", "Desc", "http://validUrl", 10);
        when(objectManagement.getItemDetails(anyList()))
                .thenReturn(Collections.singletonList(itemDetails));

        // Mock: orderExecution => false (Fehler)
        when(orderExecution.executeOrder("http://validUrl", quantity)).thenReturn(false);

        // When
        PerformOrderResult result = orderService.performOrder(userId, itemId, quantity);

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
