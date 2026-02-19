package com.example.gardenapp.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlantArea extends Land {
    private Integer ridgeId;
    @NonNull
    private Vegetable vegetable;
    @NonNull
    private Integer quantity;
    @NonNull
    private Integer rowsCount;

    // コンストラクタ
    public PlantArea(Integer width, Integer height, Integer x, Integer y, Vegetable vegetable, Integer quantity,
            Integer rowsCount) {
        super(width, height, x, y);
        this.vegetable = vegetable;
        this.quantity = quantity;
        this.rowsCount = rowsCount;
    }

    public PlantArea(Vegetable vegetable, Integer quantity, Integer rowsCount) {
        super(0, 0, 0, 0);
        this.vegetable = vegetable;
        this.quantity = quantity;
        this.rowsCount = rowsCount;
    }

    // 作付面積を計算するメソッド
    public void calcArea() {
        // 1条あたりの株数（切り上げ）
        int countPerRow = (int) Math.ceil((double) quantity / rowsCount);
        // エリア全体のサイズを計算してセット
        this.setWidth(countPerRow * this.vegetable.getUnitWidth());
        this.setHeight(rowsCount * this.vegetable.getUnitHeight());
    }
}