package adapters.inbound.rest.dto;

public class BulkCheckResultDTO {

    private Integer itemId;
    private String status;  // z.B. "OK", "INVALID_URL", "NOT_FOUND", "UNAVAILABLE" ... (später evtl als enum?)
    private String message; // optional: mehr Details
    private Integer logId;     // wenn du eine PriceLog-ID zurückliefern willst
    private double price;
    private int available;

    public BulkCheckResultDTO() {
    }

    public BulkCheckResultDTO(Integer itemId, String status, String message, Integer logId, double price, int available ) {
        this.itemId = itemId;
        this.status = status;
        this.message = message;
        this.logId = logId;
        this.available = available;
        this.price = price;
    }

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
    public double getPrice() {
        return price;
    }
    public int getAvailability() {
        return available;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setLogId(Integer logId) {
        this.logId = logId;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setAvailability(int available) {
        this.available = available;
    }

}
