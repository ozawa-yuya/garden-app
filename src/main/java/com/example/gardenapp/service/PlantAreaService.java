package com.example.gardenapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gardenapp.form.InputForm;
import com.example.gardenapp.mapper.VegetableMapper;
import com.example.gardenapp.model.PlantArea;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlantAreaService {
    final VegetableMapper vegetableMapper;

    public List<PlantArea> creatPlantAreaList(InputForm form) {
        List<PlantArea> plantAreaList = new ArrayList<>();
        for (int i = 0; i < form.getSelections().size(); i++) {
            PlantArea plantArea = new PlantArea(vegetableMapper.findById(form.getSelections().get(i).getVegetableId()),
                    form.getSelections().get(i).getQuantity(), form.getSelections().get(i).getRowsCount());
            plantArea.calcArea();
            plantAreaList.add(plantArea);
        }
        return plantAreaList;
    }
}
