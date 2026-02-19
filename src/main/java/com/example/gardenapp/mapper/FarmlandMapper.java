package com.example.gardenapp.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.gardenapp.model.Farmland;

@Mapper
public interface FarmlandMapper {
  void insert(Farmland farmland);
}
