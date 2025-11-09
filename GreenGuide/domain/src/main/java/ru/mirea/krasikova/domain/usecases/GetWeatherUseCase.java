package ru.mirea.krasikova.domain.usecases;

import ru.mirea.krasikova.domain.model.WeatherInfo;
import ru.mirea.krasikova.domain.repository.WeatherRepository;

public class GetWeatherUseCase {
    private final WeatherRepository repository;

    public GetWeatherUseCase(WeatherRepository repository) {
        this.repository = repository;
    }


    public void execute(Callback callback) {
        repository.getWeatherByIp(new WeatherRepository.RepositoryCallback<WeatherInfo>() {
            @Override
            public void onSuccess(WeatherInfo info) {
                callback.onSuccess(info);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public interface Callback {
        void onSuccess(WeatherInfo weatherInfo);
        void onError(String errorMessage);
    }
}

