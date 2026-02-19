package com.example.gardenapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gardenapp.mapper.VegetableMapper;
import com.example.gardenapp.model.Vegetable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VegetableService {
    private final VegetableMapper vegetableMapper;
    
    public List<Vegetable> getVegetableList() {
        return vegetableMapper.findAll();
    }
}
