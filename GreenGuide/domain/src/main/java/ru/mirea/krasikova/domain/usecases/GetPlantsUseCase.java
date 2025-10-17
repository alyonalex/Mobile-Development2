package ru.mirea.krasikova.domain.usecases;

import java.util.List;

import ru.mirea.krasikova.domain.model.Plant;
import ru.mirea.krasikova.domain.repository.PlantRepository;

public class GetPlantsUseCase {
    private final PlantRepository repository;

    public GetPlantsUseCase(PlantRepository repository) {
        this.repository = repository;
    }

    public List<Plant> execute() {
        return repository.getPlants();
    }
}
