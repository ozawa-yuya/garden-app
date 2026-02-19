package com.example.gardenapp.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Rectangle extends Land {
    public Rectangle(Integer width, Integer height, Integer x, Integer y) {
        super(width, height, x, y);
    }
}