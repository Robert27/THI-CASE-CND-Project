package orderlistservice.adapters.inbound.rest.dto;

import java.math.BigDecimal;

public class GetOpenOrdersResponse {
    private Integer itemId;
    private String itemName;
    private String url;
    private String description;
    private BigDecimal price;
    private int orderQuantity;
    private int availabilityQuantity;
    private String cycleDate;
    private String statusMessage;

    // Konstruktoren

    // Für erfolgreiche Bestellungen
    public GetOpenOrdersResponse(Integer itemId, String itemName, String url, String description, BigDecimal price, int orderQuantity,
                                 int availabilityQuantity, String cycleDate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.url = url;
        this.description = description;
        this.price = price;
        this.orderQuantity = orderQuantity;
        this.availabilityQuantity = availabilityQuantity;
        this.cycleDate = cycleDate;
    }

    // Für fehlgeschlagene Bestellungen
    public GetOpenOrdersResponse(Integer itemId, String itemName, String url, String description, String cycleDate, String statusMessage) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.url = url;
        this.description = description;
        this.cycleDate = cycleDate;
        this.statusMessage = statusMessage;
    }


    public GetOpenOrdersResponse() {}
    // Getter und Setter

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public int getAvailabilityQuantity() {
        return availabilityQuantity;
    }

    public void setAvailabilityQuantity(int availabilityQuantity) {
        this.availabilityQuantity = availabilityQuantity;
    }

    public String getCycleDate() {
        return cycleDate;
    }

    public void setCycleDate(String cycleDate) {
        this.cycleDate = cycleDate;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
