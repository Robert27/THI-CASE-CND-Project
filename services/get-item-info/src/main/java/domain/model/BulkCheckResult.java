package domain.model;

/**
 * Repräsentiert das Ergebnis eines Checks pro Item,
 * z. B. bei einem Bulk-Check, mit Status & Message.
 */
public class BulkCheckResult {

    private Integer itemId;
    private String status;   // "OK", "UNAVAILABLE", "INVALID_URL"...
    private String message;  // menschliche Erklärung
    private Integer logId;      // ID des gespeicherten PriceLog
    private double price;
    private int availability;

    public BulkCheckResult() {
    }

    public BulkCheckResult(Integer itemId, String status, String message,
                           Integer logId, double price, int availability) {
        this.itemId = itemId;
        this.status = status;
        this.message = message;
        this.logId = logId;
        this.price = price;
        this.availability = availability;
    }

    // Getter/Setter
    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getLogId() { return logId; }
    public void setLogId(Integer logId) { this.logId = logId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getAvailability() { return availability; }
    public void setAvailability(int availability) { this.availability = availability; }
}
