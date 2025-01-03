package dev.eggl.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageObject {
    private Integer id;
    private String name;
    private String description;
    private Integer categoryId;
    private String reorderUrl;
}
