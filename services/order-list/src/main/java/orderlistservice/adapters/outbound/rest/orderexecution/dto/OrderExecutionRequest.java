package orderlistservice.adapters.outbound.rest.orderexecution.dto;

public class OrderExecutionRequest {

    private int quantity;

    public OrderExecutionRequest() {}

    public OrderExecutionRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}