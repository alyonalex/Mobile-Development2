package ru.mirea.krasikova.greenguide.plants;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.krasikova.data.repository.PlantRepositoryImpl;
import ru.mirea.krasikova.domain.repository.PlantRepository;
import ru.mirea.krasikova.domain.usecases.GetPlantsUseCase;

public class PlantListViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public PlantListViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        PlantRepository repo = new PlantRepositoryImpl(context);
        GetPlantsUseCase getPlantsUseCase = new GetPlantsUseCase(repo);
        return (T) new PlantListViewModel(getPlantsUseCase);
    }
}
