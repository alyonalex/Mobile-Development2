package ru.mirea.krasikova.greenguide.domain.usecases;

import java.util.List;

import ru.mirea.krasikova.greenguide.domain.model.Plant;
import ru.mirea.krasikova.greenguide.domain.repository.PlantRepository;

public class GetPlantsUseCase {
    private final PlantRepository repository;

    public GetPlantsUseCase(PlantRepository repository) {
        this.repository = repository;
    }

    public List<Plant> execute() {
        return repository.getPlants();
    }
}
