package ru.mirea.krasikova.greenguide.weather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.krasikova.data.repository.WeatherRepositoryImpl;
import ru.mirea.krasikova.domain.model.WeatherInfo;
import ru.mirea.krasikova.domain.usecases.GetWeatherUseCase;

public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<WeatherInfo> weatherLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final GetWeatherUseCase getWeatherUseCase;

    public WeatherViewModel(GetWeatherUseCase getWeatherUseCase) {
        this.getWeatherUseCase = getWeatherUseCase;
    }

    public LiveData<WeatherInfo> getWeatherLiveData() {
        return weatherLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void loadWeatherByIp() {
        getWeatherUseCase.execute(new GetWeatherUseCase.Callback() {
            @Override
            public void onSuccess(WeatherInfo weatherInfo) {
                weatherLiveData.postValue(weatherInfo);
                errorLiveData.postValue(null);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.postValue(errorMessage);
            }
        });
    }
}

