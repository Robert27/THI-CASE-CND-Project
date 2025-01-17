package dev.eggl.persistance;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "interval_status")
public class IntervalStatus extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interval_status_id_gen")
    @SequenceGenerator(name = "interval_status_id_gen", sequenceName = "interval_status_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "object_id", nullable = false)
    private Integer objectId;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    Integer getObjectId() {
        return objectId;
    }

    Date getCreatedAt() {
        return createdAt;
    }

}
