package com.example.gardenapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gardenapp.form.InputForm;
import com.example.gardenapp.mapper.FarmlandMapper;
import com.example.gardenapp.mapper.PlantAreaMapper;
import com.example.gardenapp.mapper.RidgeMapper;
import com.example.gardenapp.model.Farmland;
import com.example.gardenapp.model.PlantArea;
import com.example.gardenapp.model.Ridge;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FarmlandService {
    final RidgeService ridgeService;
    final FarmlandMapper farmlandMapper;
    final RidgeMapper ridgeMapper;
    final PlantAreaMapper plantAreaMapper;

    public Farmland generateRidge(InputForm form) {
        Farmland farmland = new Farmland(
                form.getFarmlandWidth(),
                form.getFarmlandHeight(),
                0, 0,
                form.getRidgeGap(),
                form.getFarmlandOffset());
        farmland.generateRidges(form.getRidgeWidth(), form.getRidgeHeight());
        return farmland;
    }

    @Transactional
    public void saveFullPlan(Farmland farmland) {
        // 1. 農地(親)を保存
        farmlandMapper.insert(farmland);
        Integer farmlandId = farmland.getId();

        // 2. 畝(子)に農地IDをセットして保存
        for (Ridge ridge : farmland.getRidgeList()) {
            ridge.setFarmlandId(farmlandId);
        }
        if (!farmland.getRidgeList().isEmpty()) {
            ridgeMapper.save(farmland.getRidgeList());
        }

        // 3. 作付エリア(孫)に発番された畝IDをセットして一括保存
        List<PlantArea> allPlantAreas = new ArrayList<>();
        for (Ridge ridge : farmland.getRidgeList()) {
            for (PlantArea area : ridge.getPlantAreaList()) {
                area.setRidgeId(ridge.getId()); // リレーされたID
                allPlantAreas.add(area);
            }
        }
        if (!allPlantAreas.isEmpty()) {
            plantAreaMapper.save(allPlantAreas);
        }
    }

}
