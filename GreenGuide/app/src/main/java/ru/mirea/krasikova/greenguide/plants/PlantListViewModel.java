package ru.mirea.krasikova.greenguide.plants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mirea.krasikova.domain.model.Plant;
import ru.mirea.krasikova.domain.usecases.GetPlantsUseCase;

public class PlantListViewModel extends ViewModel {
    private final GetPlantsUseCase getPlantsUseCase;
    private final MutableLiveData<List<Plant>> plantsLiveData = new MutableLiveData<>();

    public PlantListViewModel(GetPlantsUseCase getPlantsUseCase) {
        this.getPlantsUseCase = getPlantsUseCase;
        loadPlants();
    }

    public LiveData<List<Plant>> getPlantsLiveData() {
        return plantsLiveData;
    }

    public void loadPlants() {
        // загрузка в фоновом потоке
        new Thread(() -> {
            List<Plant> plants = getPlantsUseCase.execute();
            plantsLiveData.postValue(plants);
        }).start();
    }
}
