package orderlistservice.adapters.inbound.grpc.messages;

import java.util.List;

/**
 * DTO für das Generieren neuer Bestellobjekte
 */
public class GenerateOrderRequest {
    private List<Integer> itemIds;
    private Integer userId;
    private String cycleDate; // statt cycleTimestamp

    public GenerateOrderRequest() {}

    public GenerateOrderRequest(List<Integer> itemIds, Integer userId, String cycleDate) {
        this.itemIds = itemIds;
        this.cycleDate = cycleDate;
    }

    public List<Integer> getItemIds() {
        return itemIds;
    }
    public void setItemIds(List<Integer> itemIds) {
        this.itemIds = itemIds;
    }

    public String getCycleDate() {
        return cycleDate;
    }
    public void setCycleDate(String cycleDate) {
        this.cycleDate = cycleDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}