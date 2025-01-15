package adapters.outbound.rest.pricecheck.dto;

public class PriceResponse {

    private Integer id;
    private double price;
    private int available;

    // Leerer Konstruktor für JSON-B / Jackson
    public PriceResponse() {
    }

    public PriceResponse(Integer id, double price, int available) {
        this.id = id;
        this.price = price;
        this.available = available;
    }

    // Getter/Setter
    public Integer getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailable() {
        return available;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
