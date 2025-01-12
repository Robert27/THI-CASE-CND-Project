package orderlistservice.domain.model;

/**
 * Einfaches Domain-Objekt, das den Ergebnisstatus
 * (z. B. HTTP-Code) und eine Nachricht enthält.
 */
public class PerformOrderResult {
    private final int statusCode;
    private final String message;

    public PerformOrderResult(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }
    public String getMessage() {
        return message;
    }
}
