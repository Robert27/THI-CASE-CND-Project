package orderlistservice.domain.model;

import java.math.BigDecimal;

public class PriceResult {
    private final Integer itemId;
    private final String status;
    private final String message;
    private final Integer logId;
    private final BigDecimal price;
    private final int availability;

    public PriceResult(Integer itemId,
                       String status,
                       String message,
                       Integer logId,
                       BigDecimal price,
                       int availability) {
        this.itemId = itemId;
        this.status = status;
        this.message = message;
        this.logId = logId;
        this.price = price;
        this.availability = availability;
    }

    // Getter, keine Setter (immutable)
    public Integer getItemId() {
        return itemId;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Integer getLogId() {
        return logId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getAvailability() {
        return availability;
    }


    public boolean isOk() {
        return "OK".equalsIgnoreCase(status);
    }
}
