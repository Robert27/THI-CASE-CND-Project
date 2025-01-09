package dev.eggl.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageObject {
    private Integer id;
    private Integer userId;
    private String name;
    private String description;
    private Integer categoryId;
    private String reorderUrl;
    private Integer quantity;
    private Date createdAt;
    private Integer weekday;
}

