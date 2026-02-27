package com.example.gardenapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gardenapp.form.InputForm;
import com.example.gardenapp.mapper.VegetableMapper;
import com.example.gardenapp.model.PlantArea;
import com.example.gardenapp.model.Vegetable;
import com.example.gardenapp.model.VegetableSelection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlantAreaService {
    final VegetableMapper vegetableMapper;

    public List<PlantArea> creatPlantAreaList(InputForm form) {
        List<PlantArea> plantAreaList = new ArrayList<>();
        int maxRidgeHeight = form.getFarmlandHeight() - (form.getFarmlandOffset() * 2);

        for (VegetableSelection s : form.getSelections()) {
            if (s.getVegetableId() == null) {
                continue; // IDがない行（未選択行）は計算せず次の行へ
            }
            Vegetable v = vegetableMapper.findById(s.getVegetableId());

            // --- 条数の自動計算ロジック ---
            int rows = 1; // 作業効率を考慮して最小限（1条）からスタート
            int length = s.getQuantity() * v.getUnitHeight(); // 1条にした時の長さ
            int maxPossibleRows = form.getRidgeWidth() / v.getUnitWidth();
            while (length > maxRidgeHeight && rows < maxPossibleRows) {
                rows++;
                length = (int) Math.ceil((double) s.getQuantity() / rows) * v.getUnitHeight();
            }

            PlantArea plantArea = new PlantArea(v, s.getQuantity(), rows);
            plantArea.calcArea();
            plantAreaList.add(plantArea);
        }
        return plantAreaList;
    }
}
