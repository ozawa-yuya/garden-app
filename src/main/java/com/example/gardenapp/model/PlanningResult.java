package com.example.gardenapp.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlanningResult {
  private final Farmland farmland;
  private final List<PlantArea> unplacedVegetables;
}
