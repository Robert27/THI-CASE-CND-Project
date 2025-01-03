package dev.eggl.adapter.persistence.jpa.storageObject;

import dev.eggl.adapter.persistence.jpa.category.CategoryJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "objects")
@Getter
@Setter
public class StorageObjectJpaEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Integer id;

        @Column(name = "name")
        private String name;

        @Column(name = "description")
        private String description;

        @Column(name = "category_id")
        private Integer categoryId;

        @Column(name = "reorder_url")
        private String reorderUrl;
}
