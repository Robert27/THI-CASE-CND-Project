package orderlistservice.application.service;

import jakarta.ws.rs.core.Response;
import orderlistservice.application.ports.outbound.ObjectManagementPort;
import orderlistservice.application.ports.outbound.OrderExecutionPort;
import orderlistservice.application.ports.outbound.OrderRepositoryPort;
import orderlistservice.application.ports.outbound.PriceMonitoringPort;
import orderlistservice.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private PriceMonitoringPort priceMonitoring;

    @Mock
    private ObjectManagementPort objectManagement;

    @Mock
    private OrderExecutionPort orderExecution;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateOrderList_Success() {
        Integer userId = 1;
        List<Integer> itemIds = Arrays.asList(101, 102);
        String cycleDate = "2025-01-11";

        // Mock existing data
        when(orderRepository.findByItemIdAndUserAndCycleDate(anyInt(), eq(userId), eq(cycleDate)))
                .thenReturn(Optional.empty());
        when(orderRepository.findByItemIdAndStatusOpenAndUserId(anyInt(), eq(userId)))
                .thenReturn(Optional.empty());

        doNothing().when(orderRepository).save(any(OrderObject.class));

        // Execute
        int result = orderService.generateOrderList(userId, itemIds, cycleDate);

        // Verify
        assertEquals(200, result);
        verify(orderRepository, times(2)).save(any(OrderObject.class));
    }

    @Test
    void testGenerateOrderList_ExistingOrder() {
        Integer userId = 1;
        List<Integer> itemIds = Collections.singletonList(101);
        String cycleDate = "2025-01-11";

        // Mock existing data
        when(orderRepository.findByItemIdAndUserAndCycleDate(101, userId, cycleDate))
                .thenReturn(Optional.of(new OrderObject()));

        // Execute
        int result = orderService.generateOrderList(userId, itemIds, cycleDate);

        // Verify
        assertEquals(200, result);
        verify(orderRepository, never()).save(any(OrderObject.class));
    }

    @Test
    void testGetOpenOrders_Success() {
        Integer userId = 1;
        List<OrderObject> mockOrders = List.of(
                new OrderObject(1, 101, "2025-01-11", OrderStatus.OPEN, true, null)
        );

        when(orderRepository.findAllByStatusOpenAndUserId(userId)).thenReturn(mockOrders);
        when(objectManagement.getItemDetails(anyList()))
                .thenReturn(List.of(new ItemDetails(101, "Item1", "Description1", "url", 10)));
        when(priceMonitoring.checkPrices(anyList()))
                .thenReturn(List.of(new PriceResult(101, "OK", null, 1, BigDecimal.TEN, 100)));

        // Execute
        List<OrderObject> result = orderService.getOpenOrders(userId);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Item1", result.get(0).getItemName());
        assertEquals(OrderStatus.OPEN, result.get(0).getOrderStatus());
    }

    @Test
    void testPerformOrder_Success() {
        Integer userId = 1;
        Integer itemId = 101;
        int quantity = 2;
        String authToken = "test-token";

        OrderObject mockOrder = new OrderObject();
        mockOrder.setItemId(itemId);
        mockOrder.setUserId(userId);
        mockOrder.setOrderStatus(OrderStatus.OPEN);

        // Mocking der Methoden
        when(orderRepository.findByItemIdAndStatusOpenAndUserId(itemId, userId))
                .thenReturn(Optional.of(mockOrder));
        when(objectManagement.getItemDetails(anyList()))
                .thenReturn(List.of(new ItemDetails(itemId, "Item1", "Description1", "url", 10)));

        // Mock für Response erstellen
        Response mockResponse = Response.status(200).entity("Order successfully executed").build();
        when(orderExecution.executeOrder(anyString(), eq(quantity)))
                .thenReturn(mockResponse);

        // Ausführung des Tests
        PerformOrderResult result = orderService.performOrder(userId, itemId, quantity, authToken);

        // Überprüfen der Ergebnisse
        assertNotNull(result);
        assertEquals(200, result.getStatusCode());
        assertEquals("Order successfully executed", result.getMessage());
        verify(orderRepository, times(1)).update(mockOrder);
    }

    @Test
    void testAbortOrder_Success() {
        Integer userId = 1;
        Integer itemId = 101;

        OrderObject mockOrder = new OrderObject();
        mockOrder.setItemId(itemId);
        mockOrder.setUserId(userId);
        mockOrder.setOrderStatus(OrderStatus.OPEN);

        when(orderRepository.findByItemIdAndStatusOpenAndUserId(itemId, userId))
                .thenReturn(Optional.of(mockOrder));

        // Execute
        orderService.abortOrder(userId, itemId);

        // Verify
        assertEquals(OrderStatus.ABORTED, mockOrder.getOrderStatus());
        verify(orderRepository, times(1)).update(mockOrder);
    }
}
