package dev.eggl.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntervalObject {
    private Integer id;
    private Integer objectId;
    private Integer interval;
}
