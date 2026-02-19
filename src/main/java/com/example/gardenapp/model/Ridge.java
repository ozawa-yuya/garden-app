package com.example.gardenapp.model;

import java.util.*;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Ridge extends Land {
    private Integer farmlandId;
    @NonNull
    private String name;
    @NonNull
    private Integer plantAreaGap = 0;
    @NonNull
    private List<Rectangle> freeSpaceList = new ArrayList<>();
    @NonNull
    private List<PlantArea> plantAreaList = new ArrayList<>();

    // コンストラクタ
    public Ridge(Integer width, Integer height, Integer x, Integer y) {
        super(width, height, x, y);
        this.freeSpaceList.add(new Rectangle(width, height, x, y));
    }

    // メソッド
    // 特定の空きスペースに野菜を配置し、残りのスペースを分割して空きスペースとして登録する
    public void splitSpace(Rectangle space, PlantArea plantArea) {
        locatePlantArea(space, plantArea);
        calculateFreeSpace(space, plantArea);
    }

    private void calculateFreeSpace(Rectangle space, PlantArea plantArea) {
        int rightFreeSpaceWidth = space.getWidth() - plantArea.getWidth() - this.plantAreaGap;
        if (rightFreeSpaceWidth > 0) {
            Rectangle rightFreeSpace = new Rectangle(rightFreeSpaceWidth, plantArea.getHeight(),
                    space.getX() + plantArea.getWidth() + this.plantAreaGap, space.getY());
            this.freeSpaceList.add(rightFreeSpace);
        }
        int bottomFreeSpaceHeight = space.getHeight() - plantArea.getHeight() - this.plantAreaGap;
        if (bottomFreeSpaceHeight > 0) {
            Rectangle bottomFreeSpace = new Rectangle(space.getWidth(), bottomFreeSpaceHeight, space.getX(),
                    space.getY() + plantArea.getHeight() + this.plantAreaGap);
            this.freeSpaceList.add(bottomFreeSpace);
        }
    }

    private void locatePlantArea(Rectangle space, PlantArea plantArea) {
        this.freeSpaceList.remove(space);
        // スペースの左上を基準に定植エリアを配置
        plantArea.setX(space.getX());
        plantArea.setY(space.getY());
        this.plantAreaList.add(plantArea);
    }

    public Rectangle findBestFitFreeSpace(PlantArea plantArea) {
        Rectangle bestFreeSpace = null;
        long minOverheadArea = Long.MAX_VALUE;
        for (Rectangle space : this.freeSpaceList) {
            if (isPlantAreaFitSpace(space, plantArea)) {
                long overheadArea = (long) space.getWidth() * space.getHeight()
                        - (long) plantArea.getWidth() * plantArea.getHeight();
                if (overheadArea < minOverheadArea) {
                    minOverheadArea = overheadArea;
                    bestFreeSpace = space;
                }
            }
        }
        return bestFreeSpace;
    }

    private boolean isPlantAreaFitSpace(Rectangle space, PlantArea plantArea) {
        boolean isFitWidth = space.getWidth() >= plantArea.getWidth();
        boolean isFitHeight = space.getHeight() >= plantArea.getHeight();
        return isFitWidth && isFitHeight;
    }
}