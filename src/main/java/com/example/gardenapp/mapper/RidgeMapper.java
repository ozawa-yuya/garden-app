package com.example.gardenapp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.gardenapp.model.Ridge;

@Mapper
public interface RidgeMapper {
    public void save(List<Ridge> ridges);
    public List<Ridge> findAll();
}
