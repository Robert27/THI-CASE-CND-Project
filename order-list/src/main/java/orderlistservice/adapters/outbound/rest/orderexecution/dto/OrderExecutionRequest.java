package orderlistservice.adapters.outbound.rest.orderexecution.dto;

public class OrderExecutionRequest {
    private int quantity;
    private String authToken;

    public OrderExecutionRequest() {}

    public OrderExecutionRequest(int quantity, String authToken) {
        this.quantity = quantity;
        this.authToken = authToken;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
