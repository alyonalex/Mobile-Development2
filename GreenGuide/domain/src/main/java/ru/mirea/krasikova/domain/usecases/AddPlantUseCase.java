package ru.mirea.krasikova.domain.usecases;

import ru.mirea.krasikova.domain.model.Plant;
import ru.mirea.krasikova.domain.repository.PlantRepository;

public class AddPlantUseCase {
    private final PlantRepository repository;

    public AddPlantUseCase(PlantRepository repository) {
        this.repository = repository;
    }

    public boolean execute(Plant plant) {
        return repository.addPlant(plant);
    }
}
