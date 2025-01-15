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

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    void setUserId(Integer userId) {
        this.userId = userId;
    }

    void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    Integer getUserId() {
        return userId;
    }

    Date getCreatedAt() {
        return createdAt;
    }

}
