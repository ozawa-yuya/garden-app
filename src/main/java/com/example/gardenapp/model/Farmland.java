package com.example.gardenapp.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Farmland extends Land {
    @NonNull
    private List<Ridge> ridgeList = new ArrayList<>();
    @NonNull
    private Integer gap;
    @NonNull
    private Integer offset;
    private int ridgeCount;
    private int remainingRidgeCount;
    private static final int MIN_RIDGE_SIZE = 30;

    public Farmland(Integer width, Integer height, Integer x, Integer y, Integer gap, Integer offset) {
        super(width, height, x, y);
        this.gap = gap;
        this.offset = offset;
    }

    public void generateRidges(int ridgeWidth, int ridgeHeight) {
        this.ridgeList.clear();
        this.ridgeCount = 0;
        this.remainingRidgeCount = 0;

        int y = this.offset;

        while (isRidgeWithinHeightLimit(y, ridgeHeight)) {
            generateRow(y, ridgeWidth, ridgeHeight);
            y += ridgeHeight + this.gap;
        }

        int remainingHeight = this.getHeight() - y - this.offset;
        if (remainingHeight >= MIN_RIDGE_SIZE) {
            generateRow(y, ridgeWidth, remainingHeight);
        }
    }

    private void generateRidge(int ridgeWidth, int ridgeHeight, int x, int y, String prefix, int count) {
        Ridge ridge = new Ridge(ridgeWidth, ridgeHeight, x + this.getX(), y + this.getY());
        ridge.setName(prefix + count);
        this.ridgeList.add(ridge);
    }

    private void generateRow(int y, int ridgeWidth, int ridgeHeight) {
        int x = this.offset;
        while (isRidgeWithinWidthLimit(x, ridgeWidth)) {
            generateRidge(ridgeWidth, ridgeHeight, x, y, "畝-", ++this.ridgeCount);
            x += ridgeWidth + this.gap;
        }
        int remainingWidth = this.getWidth() - x - this.offset;
        if (remainingWidth >= MIN_RIDGE_SIZE) {
            generateRidge(remainingWidth, ridgeHeight, x, y, "余り畝-", ++this.remainingRidgeCount);
        }
    }

    private boolean isRidgeWithinHeightLimit(int currentY, int ridgeHeight) {
        return currentY + ridgeHeight + this.offset <= this.getHeight();
    }

    private boolean isRidgeWithinWidthLimit(int currentX, int ridgeWidth) {
        return currentX + ridgeWidth + this.offset <= this.getWidth();
    }
}
