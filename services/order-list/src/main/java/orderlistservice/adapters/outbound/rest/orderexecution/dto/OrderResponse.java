package orderlistservice.adapters.outbound.rest.orderexecution.dto;

/**
 * DTO für die Antwort nach dem Ausführen einer Bestellung.
 */
public class OrderResponse {
    private int statusCode;
    private String message;

    public OrderResponse() {}

    public OrderResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    // Getter und Setter

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
