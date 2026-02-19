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
    public Farmland plan(List<PlantArea> plantAreaList, Farmland farmland) {
        // 日当たりを考慮し、背の高い野菜を北側（Y座標の小さい畝）から優先配置するためのソート（背の高さ > 面積 > 奥行）
        List<Ridge> ridges = farmland.getRidgeList();
        plantAreaList.sort(new Comparator<PlantArea>() {
            @Override
            public int compare(PlantArea a, PlantArea b) {
                // 背の高い野菜を優先 (true が先)
                if (a.getVegetable().getIsTall() != b.getVegetable().getIsTall()) {
                    return a.getVegetable().getIsTall() ? -1 : 1;
                }
                // 面積の大きい順
                int areaA = a.getWidth() * a.getHeight();
                int areaB = b.getWidth() * b.getHeight();
                if (areaB != areaA)
                    return areaB - areaA;

                // 奥行の大きい順
                return b.getHeight() - a.getHeight();
            }
        });

        // 畝のソート（北側＝Y座標が小さい順）
        ridges.sort(Comparator.comparingInt(Ridge::getY));

        // 作付エリアを1つずつ最適な空きスペースに配置する
        for (PlantArea area : plantAreaList) {
            Rectangle bestOverallSpace = null;
            Ridge bestOverallRidge = null;
            long minOverheadArea = Long.MAX_VALUE;

            // すべての畝をループし、全畝でのベスト配置を探索する
            for (Ridge ridge : ridges) {
                // 畝の中でのベスト候補を探す
                Rectangle bestInRidge = ridge.findBestFitFreeSpace(area);
                if (bestInRidge != null) {
                    // 背の高い野菜の場合、最初に見つかった（＝より北にある）畝に配置してループを抜ける
                    if (area.getVegetable().getIsTall()) {
                        bestOverallSpace = bestInRidge;
                        bestOverallRidge = ridge;
                        break;
                    }
                    // 畝の中でのベスト候補があれば、その余剰面積を計算
                    long overhead = (long) bestInRidge.getWidth() * bestInRidge.getHeight()
                            - (long) area.getWidth() * area.getHeight();
                    // これまでの畝の中でのベストよりも余剰面積が小さいならその畝と空きスペースをキープ
                    if (overhead < minOverheadArea) {
                        minOverheadArea = overhead;
                        bestOverallSpace = bestInRidge;
                        bestOverallRidge = ridge;
                    }
                }
            }
            // 全畝の中で最も最適な場所に配置を確定させる
            if (bestOverallRidge != null && bestOverallSpace != null) {
                bestOverallRidge.splitSpace(bestOverallSpace, area);
                System.out.println(area.getVegetable().getName() + " を 畝ID:" + bestOverallRidge.getId() + " に配置しました。");
            } else {
                System.out.println(area.getVegetable().getName() + " はどの畝にも入りませんでした。");
            }
        }
        return farmland;
    }
}
