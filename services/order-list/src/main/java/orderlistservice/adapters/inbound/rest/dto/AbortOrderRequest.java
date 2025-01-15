package orderlistservice.adapters.inbound.rest.dto;

/**
 * DTO für das Abbrechen einer Bestellung.
 */
public class AbortOrderRequest {
    private String itemId;

    // Getter und Setter

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
