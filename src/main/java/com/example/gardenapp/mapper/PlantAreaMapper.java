package com.example.gardenapp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.gardenapp.model.PlantArea;
import com.example.gardenapp.model.Ridge;

@Mapper
public interface PlantAreaMapper {
    // 各畝の配置レイアウトを保存するメソッド
    public void saveAll(List<Ridge> ridges);
    // 指定された畝に配置された作付エリアを返す
    public List<PlantArea> findAllByRidgeId(int ridgeId);
    public void save(List<PlantArea> areas);
}
