package ru.mirea.krasikova.greenguide.data.repository;

import java.util.List;

import ru.mirea.krasikova.greenguide.data.test.TestDataSource;
import ru.mirea.krasikova.greenguide.domain.model.Plant;
import ru.mirea.krasikova.greenguide.domain.repository.PlantRepository;

public class PlantRepositoryImpl implements PlantRepository {
    @Override
    public List<Plant> getPlants() {
        return TestDataSource.plants;
    }

    @Override
    public Plant getPlantById(int id) {
        for (Plant p : TestDataSource.plants) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    @Override
    public boolean addPlant(Plant plant) {
        return TestDataSource.plants.add(plant);
    }
}