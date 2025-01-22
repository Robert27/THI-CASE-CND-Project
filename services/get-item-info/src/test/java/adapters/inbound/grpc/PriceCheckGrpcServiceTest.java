package adapters.inbound.grpc;

import application.service.ItemPriceServiceImpl;
import domain.model.BulkCheckResult;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pricecheck.PriceCheckServiceGrpc;
import pricecheck.PriceCheckRequest;
import pricecheck.PriceCheckReply;
import pricecheck.PriceResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PriceCheckGrpcServiceTest {

    @Mock
    private ItemPriceServiceImpl itemPriceService;

    @InjectMocks
    private PriceCheckGrpcService priceCheckGrpcService;

    private ManagedChannel inProcessChannel;
    private io.grpc.Server inProcessServer;
    private static String serverName;

    @BeforeAll
    static void setupAll() {
        serverName = InProcessServerBuilder.generateName();
    }

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        inProcessServer = InProcessServerBuilder.forName(serverName)
                .addService(priceCheckGrpcService)
                .directExecutor()
                .build()
                .start();

        inProcessChannel = InProcessChannelBuilder.forName(serverName)
                .directExecutor()
                .build();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        inProcessChannel.shutdownNow();
        inProcessServer.shutdownNow();
    }

    // Test 1: checkPrices_Success
    @Test
    void testCheckPrices_Success() {
        // Vorbereitung der Mock-Daten
        List<Integer> itemIds = Arrays.asList(101, 102, 103);
        PriceCheckRequest request = PriceCheckRequest.newBuilder()
                .addAllItemIds(itemIds)
                .build();

        // Mock-Ergebnisse
        List<BulkCheckResult> mockResults = Arrays.asList(
                new BulkCheckResult(101, "SUCCESS", "", 1001, 29.99, 35),
                new BulkCheckResult(102, "SUCCESS", "", 1002, 49.99, 155),
                new BulkCheckResult(103, "FAILURE", "Item not found", 0, 0.0, 0)
        );

        when(itemPriceService.bulkCheckItems(itemIds)).thenReturn(mockResults);

        // Erstellung des gRPC-Clients
        PriceCheckServiceGrpc.PriceCheckServiceBlockingStub stub = PriceCheckServiceGrpc.newBlockingStub(inProcessChannel);

        // Ausführung des gRPC-Aufrufs
        PriceCheckReply reply = stub.checkPrices(request);

        // Assertions
        assertNotNull(reply);
        assertEquals(3, reply.getResultsCount());

        PriceResult res1 = reply.getResults(0);
        assertEquals(101, res1.getItemId());
        assertEquals("SUCCESS", res1.getStatus());
        assertEquals("", res1.getMessage());
        assertEquals(1001, res1.getLogId());
        assertEquals(29.99, res1.getPrice());
        assertEquals(35, res1.getAvailability());

        PriceResult res2 = reply.getResults(1);
        assertEquals(102, res2.getItemId());
        assertEquals("SUCCESS", res2.getStatus());
        assertEquals("", res2.getMessage());
        assertEquals(1002, res2.getLogId());
        assertEquals(49.99, res2.getPrice());
        assertEquals(155, res2.getAvailability());

        PriceResult res3 = reply.getResults(2);
        assertEquals(103, res3.getItemId());
        assertEquals("FAILURE", res3.getStatus());
        assertEquals("Item not found", res3.getMessage());
        assertEquals(0, res3.getLogId());
        assertEquals(0.0, res3.getPrice());
        assertEquals(0, res3.getAvailability());

        // Verifikation der Mock-Interaktionen
        verify(itemPriceService).bulkCheckItems(itemIds);
    }

    // Test 2: checkPrices_InternalError
    @Test
    void testCheckPrices_InternalError() {
        // Vorbereitung der Mock-Daten
        List<Integer> itemIds = Arrays.asList(101, 102, 103);
        PriceCheckRequest request = PriceCheckRequest.newBuilder()
                .addAllItemIds(itemIds)
                .build();

        // Simuliere einen internen Fehler
        when(itemPriceService.bulkCheckItems(itemIds))
                .thenThrow(new RuntimeException("Datenbankfehler"));

        // Erstellung des gRPC-Clients
        PriceCheckServiceGrpc.PriceCheckServiceBlockingStub stub = PriceCheckServiceGrpc.newBlockingStub(inProcessChannel);

        // Ausführung des gRPC-Aufrufs und Erwartung eines Fehlers
        Exception exception = assertThrows(io.grpc.StatusRuntimeException.class, () -> {
            stub.checkPrices(request);
        });

        // Assertions
        assertEquals(io.grpc.Status.INTERNAL.getCode(), ((io.grpc.StatusRuntimeException) exception).getStatus().getCode());
        assertTrue(exception.getMessage().contains("Internal server error: Datenbankfehler"));

        // Verifikation der Mock-Interaktionen
        verify(itemPriceService).bulkCheckItems(itemIds);
    }

}
