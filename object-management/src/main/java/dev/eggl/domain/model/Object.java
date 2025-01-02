package dev.eggl.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Object {
    private Long id;
    private String name;
    private String description;
    private Category category; // New field
}
