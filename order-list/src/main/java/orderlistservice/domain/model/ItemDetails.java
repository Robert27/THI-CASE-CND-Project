package orderlistservice.domain.model;

public class ItemDetails {
    private Integer itemId;
    private String name;
    private String description;
    private String url;
    private int quantity;

    public ItemDetails(Integer itemId, String name, String description, String url, int quantity) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.url = url;
        this.quantity = quantity;
    }

    public Integer getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public int getQuantity() {
        return quantity;
    }
}
