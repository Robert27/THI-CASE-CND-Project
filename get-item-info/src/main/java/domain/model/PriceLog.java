package domain.model;

import java.time.LocalDateTime;

public class PriceLog {

    private Integer id;       // DB-PK
    private Integer itemId;         // das zugehörige Item
    private double price;          // z. B. 12.99
    private int availability;     // true/false
    private LocalDateTime checkedAt; // Zeitstempel

    public PriceLog(Integer id, Integer itemId, double price, int availability, LocalDateTime checkedAt) {
        this.id = id;
        this.itemId = itemId;
        this.price = price;
        this.availability = availability;
        this.checkedAt = checkedAt;
    }
    public PriceLog(Integer itemId, double price, int availability, LocalDateTime checkedAt) {
        this(null, itemId, price, availability, checkedAt);
    }

    public Integer getId() {
        return id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailability() {
        return availability;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }
}
