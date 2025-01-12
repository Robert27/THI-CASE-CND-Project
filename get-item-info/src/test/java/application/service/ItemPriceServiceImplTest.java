
package application.service;

import application.port.ObjectManagementPort;
import application.port.PriceCheckPort;
import application.port.PriceLogRepository;
import application.port.URLValidationPort;
import domain.model.PriceLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemPriceServiceImplTest {

    @InjectMocks
    private ItemPriceServiceImpl itemPriceService;

    @Mock
    private ObjectManagementPort objectManagementPort;

    @Mock
    private URLValidationPort urlValidationPort;

    @Mock
    private PriceCheckPort priceCheckPort;

    @Mock
    private PriceLogRepository priceLogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 1. Test: checkPriceByUrl (Valid URL)
    @Test
    void testCheckPriceByUrl_ValidUrl() {
        String url = "http://example.com/item";
        PriceLog mockPriceLog = new PriceLog(1, 123, 19.99, 10, LocalDateTime.now());

        when(urlValidationPort.validateUrl(url)).thenReturn(true);
        when(priceCheckPort.checkPrice(0, url)).thenReturn(mockPriceLog);

        PriceLog result = itemPriceService.checkPriceByUrl(url);

        assertNotNull(result);
        assertEquals(19.99, result.getPrice());
        assertEquals(10, result.getAvailability());

        verify(urlValidationPort).validateUrl(url);
        verify(priceCheckPort).checkPrice(0, url);
    }

    // 2. Test: checkPriceByUrl (Invalid URL)
    @Test
    void testCheckPriceByUrl_InvalidUrl() {
        String url = "http://example.com/item";

        when(urlValidationPort.validateUrl(url)).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            itemPriceService.checkPriceByUrl(url);
        });

        assertEquals("URL ist ungültig oder nicht erreichbar: " + url, exception.getMessage());
        verify(urlValidationPort).validateUrl(url);
        verifyNoInteractions(priceCheckPort);
    }

    // 3. Test: checkPriceById (Valid ID)
    @Test
    void testCheckPriceById_ValidId() {
        Integer itemId = 123;
        String url = "http://example.com/item";
        PriceLog mockPriceLog = new PriceLog(1, itemId, 19.99, 10, LocalDateTime.now());

        when(objectManagementPort.getReorderUrlsByIds(List.of(itemId))).thenReturn(Map.of(itemId, url));
        when(urlValidationPort.validateUrl(url)).thenReturn(true);
        when(priceCheckPort.checkPrice(itemId, url)).thenReturn(mockPriceLog);

        PriceLog result = itemPriceService.checkPriceById(itemId);

        assertNotNull(result);
        assertEquals(19.99, result.getPrice());
        assertEquals(10, result.getAvailability());

        verify(objectManagementPort).getReorderUrlsByIds(List.of(itemId));
        verify(urlValidationPort).validateUrl(url);
        verify(priceCheckPort).checkPrice(itemId, url);
    }

    // 4. Test: checkPriceById (URL Not Found)
    @Test
    void testCheckPriceById_UrlNotFound() {
        Integer itemId = 123;

        when(objectManagementPort.getReorderUrlsByIds(List.of(itemId))).thenReturn(Map.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemPriceService.checkPriceById(itemId);
        });

        assertEquals("Keine URL gefunden für Item ID: " + itemId, exception.getMessage());
        verify(objectManagementPort).getReorderUrlsByIds(List.of(itemId));
        verifyNoInteractions(urlValidationPort, priceCheckPort);
    }

    // 5. Test: getLogById (Log Found)
    @Test
    void testGetLogById_LogFound() {
        Integer logId = 1;
        PriceLog mockPriceLog = new PriceLog(logId, 123, 19.99, 10, LocalDateTime.now());

        when(priceLogRepository.findById(logId)).thenReturn(Optional.of(mockPriceLog));

        PriceLog result = itemPriceService.getLogById(logId);

        assertNotNull(result);
        assertEquals(19.99, result.getPrice());
        assertEquals(10, result.getAvailability());

        verify(priceLogRepository).findById(logId);
    }

    // 6. Test: getLogById (Log Not Found)
    @Test
    void testGetLogById_LogNotFound() {
        Integer logId = 1;

        when(priceLogRepository.findById(logId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemPriceService.getLogById(logId);
        });

        assertEquals("Kein Log mit ID " + logId + " gefunden.", exception.getMessage());
        verify(priceLogRepository).findById(logId);
    }
}

