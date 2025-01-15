package adapters.inbound.rest.dto;

import java.util.List;

public class BulkCheckRequest {
    private List<Integer> itemIds;

    public BulkCheckRequest() {
    }

    public BulkCheckRequest(List<Integer> itemIds) {
        this.itemIds = itemIds;
    }

    public List<Integer> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Integer> itemIds) {
        this.itemIds = itemIds;
    }
}
