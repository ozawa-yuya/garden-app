package com.example.gardenapp.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vegetable {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String family;
    @NonNull
    private Integer unitWidth;
    @NonNull
    private Integer unitHeight;
    @NonNull
    private Boolean isTall;
}
