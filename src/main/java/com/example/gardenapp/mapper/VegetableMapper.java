package com.example.gardenapp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.gardenapp.model.Vegetable;

@Mapper
public interface VegetableMapper {
    // 該当するIDの野菜データを取得する
    public Vegetable findById(int id);
    // データベースを一括取得する
    public List<Vegetable> findAll();
}
