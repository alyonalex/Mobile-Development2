package ru.mirea.krasikova.data.repository;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.mirea.krasikova.data.network.OpenMeteoApiService;
import ru.mirea.krasikova.domain.model.WeatherInfo;
import ru.mirea.krasikova.domain.repository.WeatherRepository;

public class WeatherRepositoryImpl implements WeatherRepository {
    private final OpenMeteoApiService api;

    public WeatherRepositoryImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        api = retrofit.create(OpenMeteoApiService.class);
    }

    @Override
    public WeatherInfo getCurrentWeather(double lat, double lon) {
        return new WeatherInfo("Москва","Россия", 20.5, 5.2);
    }
}
