package orderlistservice.adapters.outbound.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

/**
 * JPA-Entity für die Tabelle "orders"
 */
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private Integer itemId;
    private String cycleDate;

    private String orderStatus;

    private boolean itemStatus;

    private Integer logId;

    // Getter/Setter
    public Integer getId() {
        return id;
    }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getItemId() {
        return itemId;
    }

    public String getCycleDate() { return cycleDate; }

    public void setCycleDate(String cycleDate) { this.cycleDate = cycleDate; }

    public String getOrderStatus() {
        return orderStatus;
    }

    public boolean isItemStatus() {
        return itemStatus;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setItemStatus(boolean itemStatus) {
        this.itemStatus = itemStatus;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }
}
