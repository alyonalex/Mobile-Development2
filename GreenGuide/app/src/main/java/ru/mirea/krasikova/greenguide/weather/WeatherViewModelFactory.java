package ru.mirea.krasikova.greenguide.weather;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.krasikova.data.repository.WeatherRepositoryImpl;
import ru.mirea.krasikova.domain.usecases.GetWeatherUseCase;

public class WeatherViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        GetWeatherUseCase useCase = new GetWeatherUseCase(new WeatherRepositoryImpl());
        return (T) new WeatherViewModel(useCase);
    }
}
