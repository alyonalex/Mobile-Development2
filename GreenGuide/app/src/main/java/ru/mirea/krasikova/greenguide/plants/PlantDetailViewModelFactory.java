package ru.mirea.krasikova.greenguide.plants;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import ru.mirea.krasikova.data.repository.PlantRepositoryImpl;
import ru.mirea.krasikova.data.storage.AppDatabase;
import ru.mirea.krasikova.domain.repository.PlantRepository;
import ru.mirea.krasikova.domain.usecases.GetPlantDetailUseCase;

public class PlantDetailViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;

    public PlantDetailViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        AppDatabase db = Room.databaseBuilder(
                application,
                AppDatabase.class,
                "plants.db"
        ).allowMainThreadQueries().build();

        PlantRepository repo = new PlantRepositoryImpl(application);
        GetPlantDetailUseCase useCase = new GetPlantDetailUseCase(repo);
        return (T) new PlantDetailViewModel(useCase);
    }
}
