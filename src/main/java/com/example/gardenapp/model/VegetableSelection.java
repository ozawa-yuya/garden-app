package com.example.gardenapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VegetableSelection {
    private Integer vegetableId;
    private Integer quantity;
}
