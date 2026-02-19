package com.example.gardenapp.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VegetableSelection {
    private Integer vegetableId;
    private Integer quantity;
    private Integer rowsCount;
}
