package ru.mirea.krasikova.domain.repository;

import java.util.List;

import ru.mirea.krasikova.domain.model.Plant;

public interface PlantRepository {
    List<Plant> getPlants();

    Plant getPlantById(int id);

    boolean addPlant(Plant plant);
}
