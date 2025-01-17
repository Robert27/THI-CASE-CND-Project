package orderlistservice.adapters.inbound.rest.dto;

/**
 * DTO für die Antwort nach dem Abbrechen eines Orders.
 */
public class AbortOrderResponse {

    private Integer statusCode;
    private String message;

    public AbortOrderResponse() {
    }

    public AbortOrderResponse(Integer orderStatus, String message) {
        this.statusCode = orderStatus;
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
