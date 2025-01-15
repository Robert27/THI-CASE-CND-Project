package adapters.outbound.jpa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_price_log")
public class PriceLogEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;         // PK, autoincrement

    @Column(name = "item_id", nullable = false)
    public Integer itemId;   // FK, verknüpft auf item.item_id

    @Column(name = "price", nullable = false)
    public double price;

    @Column(name = "available", nullable = false)
    public int available;

    @Column(name = "checked_at", nullable = false)
    public LocalDateTime checkedAt;
}
