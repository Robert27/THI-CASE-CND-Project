package adapters.outbound.rest.pricecheck.dto;

public class PriceResponse {

    private double price;
    private int available;

    // Leerer Konstruktor für JSON-B / Jackson
    public PriceResponse() {
    }

    public PriceResponse(double price, int available) {
        this.price = price;
        this.available = available;
    }

    // Getter/Setter
    public double getPrice() {
        return price;
    }

    public int getAvailable() {
        return available;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}