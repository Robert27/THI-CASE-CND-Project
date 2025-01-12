package orderlistservice.domain.model;

import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import java.math.BigDecimal;

@Entity
@Table(name = "order_object")
public class OrderObject extends PanacheEntityBase {

    @Id
    private Integer id;

    private Integer itemId;
    private String cycleDate;   // statt cycleTime => Nur Datum
    private OrderStatus orderStatus;
    private boolean itemStatus;
    private Integer logId;
    private Integer userId;

    private String itemName;       // aus ItemDetails
    private String url;            // aus ItemDetails
    private String description;    // aus ItemDetails
    private int orderQuantity;     // aus ItemDetails
    private BigDecimal price;      // aus PriceResult
    private int availability;      // aus PriceResult
    private String statusMessage;

    public OrderObject() {
    }

    public OrderObject(Integer id, Integer itemId, String cycleDate,
                       OrderStatus orderStatus, boolean itemStatus, Integer logId) {
        this.id = id;
        this.itemId = itemId;
        this.cycleDate = cycleDate;
        this.orderStatus = orderStatus;
        this.itemStatus = itemStatus;
        this.logId = logId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getCycleDate() {
        return cycleDate;
    }

    public void setCycleDate(String cycleDate) {
        this.cycleDate = cycleDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderStatusFromString(String orderStatus) {
        try {
            this.orderStatus = OrderStatus.valueOf(orderStatus);
        } catch (IllegalArgumentException e) {
            this.orderStatus = null; // Oder ein Default-Wert wie OrderStatus.FAILED
        }
    }



    public boolean isItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(boolean itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void markItemUnavailable() {
        this.itemStatus = false;
    }

    public void markItemAvailable() {
        this.itemStatus = true;
    }
}
