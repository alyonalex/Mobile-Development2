package ru.mirea.krasikova.greenguide.domain.usecases;

import ru.mirea.krasikova.greenguide.domain.repository.WeatherInfo;
import ru.mirea.krasikova.greenguide.domain.repository.WeatherRepository;

public class GetWeatherUseCase {
    private final WeatherRepository repository;

    public GetWeatherUseCase(WeatherRepository repository) {
        this.repository = repository;
    }

    public WeatherInfo execute(double lat, double lon) {
        return repository.getCurrentWeather(lat, lon);
    }
}
