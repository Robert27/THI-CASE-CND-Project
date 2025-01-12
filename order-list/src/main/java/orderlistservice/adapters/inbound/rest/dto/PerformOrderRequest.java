package orderlistservice.adapters.inbound.rest.dto;

public class PerformOrderRequest {


    private Integer userId;
    private Integer itemId;
    private int quantity;
    private String authToken;

    public PerformOrderRequest() {
    }

    public PerformOrderRequest(int quantity, String authToken) {
        this.quantity = quantity;
        this.authToken = authToken;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
