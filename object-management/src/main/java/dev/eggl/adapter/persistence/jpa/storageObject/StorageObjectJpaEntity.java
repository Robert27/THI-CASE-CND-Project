package dev.eggl.adapter.persistence.jpa.storageObject;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "objects")
@Getter
@Setter
public class StorageObjectJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "reorder_url")
    private String reorderUrl;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "interval_min")
    private Integer interval;

    @Column(name = "created_at")
    private Date createdAt;
}
