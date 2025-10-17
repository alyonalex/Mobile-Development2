package ru.mirea.krasikova.domain.usecases;

import ru.mirea.krasikova.domain.model.Plant;
import ru.mirea.krasikova.domain.repository.PlantRepository;

public class GetPlantDetailUseCase {
    private final PlantRepository repository;

    public GetPlantDetailUseCase(PlantRepository repository) {
        this.repository = repository;
    }

    public Plant execute(int id) {
        return repository.getPlantById(id);
    }
}
