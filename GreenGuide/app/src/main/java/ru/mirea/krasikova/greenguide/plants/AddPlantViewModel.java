package ru.mirea.krasikova.greenguide.plants;

import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.krasikova.domain.model.Plant;
import ru.mirea.krasikova.domain.usecases.AddPlantUseCase;

public class AddPlantViewModel extends ViewModel {

    private final AddPlantUseCase addPlantUseCase;

    public AddPlantViewModel(AddPlantUseCase addPlantUseCase) {
        this.addPlantUseCase = addPlantUseCase;
    }

    public void addPlant(String name, String description, String imageUrl) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        Plant plant = new Plant(0, name, description, imageUrl, currentDate);

        addPlantUseCase.execute(plant);
    }
}
