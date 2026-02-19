package com.example.gardenapp.model;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public abstract class Land {
    private Integer id;
    @NonNull
    private Integer width;
    @NonNull
    private Integer height;
    @NonNull
    private Integer x;
    @NonNull
    private Integer y;
}