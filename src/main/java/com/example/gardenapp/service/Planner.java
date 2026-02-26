package com.example.gardenapp.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gardenapp.model.Farmland;
import com.example.gardenapp.model.PlantArea;
import com.example.gardenapp.model.Rectangle;
import com.example.gardenapp.model.Ridge;

@Service
public class Planner {

    /**
     * 野菜の作付計画を実行する。
     * 日当たりを考慮し、背の高い野菜から順に、北側（図の上側）の畝に配置する。
     */
    public Farmland plan(List<PlantArea> plantAreaList, Farmland farmland) {

        // 1. 野菜リストのソート
        // 優先順位：草丈（降順） > 面積（降順） > 奥行（降順）
        plantAreaList.sort(
                Comparator.comparingInt((PlantArea pa) -> pa.getVegetable().getHeight())
                        .thenComparingInt(pa -> pa.getWidth() * pa.getHeight())
                        .thenComparingInt(PlantArea::getHeight)
                        .reversed());

        // 2. 畝のソート（北側＝Y座標が小さい順）
        List<Ridge> ridges = farmland.getRidgeList();
        ridges.sort(Comparator.comparingInt(Ridge::getY));

        // 3. 配置シミュレーションの実行
        for (PlantArea area : plantAreaList) {
            Rectangle bestOverallSpace = null;
            Ridge bestOverallRidge = null;
            long minOverheadArea = Long.MAX_VALUE;

            for (Ridge ridge : ridges) {
                // 畝の中の空きスペースから最適な場所を探す
                Rectangle bestInRidge = ridge.findBestFitFreeSpace(area);

                if (bestInRidge != null) {
                    // 【実益ロジック】草丈が100cm以上の高い野菜は、
                    // 面積効率よりも「より北側の畝であること」を最優先して即座に確定
                    if (area.getVegetable().getHeight() >= 100) {
                        bestOverallSpace = bestInRidge;
                        bestOverallRidge = ridge;
                        break;
                    }

                    // それ以外の野菜は、最も「余白（Overhead）」が少なくなる効率的な畝を選択
                    long overhead = (long) bestInRidge.getWidth() * bestInRidge.getHeight()
                            - (long) area.getWidth() * area.getHeight();

                    if (overhead < minOverheadArea) {
                        minOverheadArea = overhead;
                        bestOverallSpace = bestInRidge;
                        bestOverallRidge = ridge;
                    }
                }
            }

            // 確定した場所に配置し、空きスペースを分割更新する
            if (bestOverallRidge != null && bestOverallSpace != null) {
                bestOverallRidge.splitSpace(bestOverallSpace, area);
                // 開発時確認用ログ
                System.out.println(String.format("%s (高さ:%dcm) を 畝:%s に配置",
                        area.getVegetable().getName(), area.getVegetable().getHeight(), bestOverallRidge.getName()));
            } else {
                System.out.println(area.getVegetable().getName() + " は配置可能なスペースがありませんでした。");
            }
        }

        return farmland;
    }
}
