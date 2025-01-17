package orderlistservice.adapters.inbound.rest.dto;

public class PerformOrderRequest {


    private Integer itemId;
    private int quantity;


    public PerformOrderRequest() {
    }

    public PerformOrderRequest(Integer itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
