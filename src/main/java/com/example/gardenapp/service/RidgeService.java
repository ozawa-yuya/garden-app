package com.example.gardenapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gardenapp.mapper.RidgeMapper;
import com.example.gardenapp.model.Ridge;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RidgeService {
    private final RidgeMapper ridgeMapper;

    public void saveRidgeList(List<Ridge> ridgeList) {
        ridgeMapper.save(ridgeList);
    }
}
