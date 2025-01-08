package dev.eggl;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "intervals")
public class IntervalEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "intervals_id_gen")
    @SequenceGenerator(name = "intervals_id_gen", sequenceName = "intervals_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "\"interval\"", nullable = false)
    private Long interval;

    @Column(name = "start_time")
    private Instant startTime;

    public IntervalEntity(String name, int i, Long interval, Instant startTime) {
        this.name = name;
        this.userId = i;
        this.interval = interval;
        this.startTime = startTime;
    }

    public IntervalEntity() {

    }
}
