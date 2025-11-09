package ru.mirea.krasikova.data.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.krasikova.data.model.PlantEntity;
import ru.mirea.krasikova.data.storage.AppDatabase;
import ru.mirea.krasikova.data.storage.PlantStorage;
import ru.mirea.krasikova.domain.model.Plant;
import ru.mirea.krasikova.domain.repository.PlantRepository;

public class PlantRepositoryImpl implements PlantRepository {
    private final PlantStorage plantStorage;

    public PlantRepositoryImpl(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.plantStorage = db.plantStorage();
    }

    @Override
    public List<Plant> getPlants() {
        List<PlantEntity> entities = plantStorage.getAll();
        List<Plant> plants = new ArrayList<>();
        for (PlantEntity e : entities) {
            plants.add(new Plant(e.id, e.name, e.description, e.imageUrl, e.dateAdded));
        }
        return plants;
    }

    @Override
    public Plant getPlantById(int id) {
        PlantEntity e = plantStorage.getById(id);
        if (e == null) return null;
        return new Plant(e.id, e.name, e.description, e.imageUrl, e.dateAdded);
    }

    @Override
    public void addPlant(Plant plant) {
        PlantEntity entity = new PlantEntity();
        entity.name = plant.getName();
        entity.description = plant.getDescription();
        entity.imageUrl = plant.getImageUrl();
        entity.dateAdded = plant.getDateAdded();

        AppDatabase.getDatabaseWriteExecutor().execute(() -> plantStorage.insert(entity));
    }
}
