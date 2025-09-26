package ru.mirea.krasikova.greenguide.domain.repository;

import java.util.List;

import ru.mirea.krasikova.greenguide.domain.model.Plant;

public interface PlantRepository {
    List<Plant> getPlants();

    Plant getPlantById(int id);

    boolean addPlant(Plant plant);
}
