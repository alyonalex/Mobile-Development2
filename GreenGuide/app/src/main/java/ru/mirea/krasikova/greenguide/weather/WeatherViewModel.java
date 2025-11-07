package ru.mirea.krasikova.greenguide.weather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.krasikova.domain.model.WeatherInfo;
import ru.mirea.krasikova.domain.usecases.GetWeatherUseCase;

public class WeatherViewModel extends ViewModel {

    private final GetWeatherUseCase getWeatherUseCase;

    private final MutableLiveData<Double> latLiveData = new MutableLiveData<>();
    private final MutableLiveData<Double> lonLiveData = new MutableLiveData<>();
    private final MediatorLiveData<WeatherInfo> weatherLiveData = new MediatorLiveData<>();

    public WeatherViewModel(GetWeatherUseCase getWeatherUseCase) {
        this.getWeatherUseCase = getWeatherUseCase;

        // MediatorLiveData отслеживает изменения координат
        weatherLiveData.addSource(latLiveData, lat -> updateWeather());
        weatherLiveData.addSource(lonLiveData, lon -> updateWeather());
    }

    public LiveData<WeatherInfo> getWeatherLiveData() {
        return weatherLiveData;
    }

    public void setCoordinates(double lat, double lon) {
        latLiveData.setValue(lat);
        lonLiveData.setValue(lon);
    }

    private void updateWeather() {
        Double lat = latLiveData.getValue();
        Double lon = lonLiveData.getValue();
        if (lat != null && lon != null) {
            WeatherInfo info = getWeatherUseCase.execute(lat, lon);
            weatherLiveData.setValue(info);
        }
    }
}
