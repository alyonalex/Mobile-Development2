package ru.mirea.krasikova.greenguide.plants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.krasikova.domain.model.Plant;
import ru.mirea.krasikova.domain.usecases.GetPlantDetailUseCase;

public class PlantDetailViewModel extends ViewModel {
    private final GetPlantDetailUseCase getPlantDetailUseCase;
    private final MutableLiveData<Plant> plantLiveData = new MutableLiveData<>();

    public PlantDetailViewModel(GetPlantDetailUseCase getPlantDetailUseCase) {
        this.getPlantDetailUseCase = getPlantDetailUseCase;
    }

    public LiveData<Plant> getPlantLiveData() {
        return plantLiveData;
    }

    public void loadPlant(int id) {
        new Thread(() -> {
            Plant plant = getPlantDetailUseCase.execute(id);
            plantLiveData.postValue(plant);
        }).start();
    }
}


