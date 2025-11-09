package ru.mirea.krasikova.greenguide.weather;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.krasikova.data.network.IpInfoService;
import ru.mirea.krasikova.data.network.OpenMeteoService;
import ru.mirea.krasikova.data.repository.WeatherRepositoryImpl;
import ru.mirea.krasikova.domain.repository.WeatherRepository;
import ru.mirea.krasikova.domain.usecases.GetWeatherUseCase;

public class WeatherViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        Retrofit ipRetrofit = new Retrofit.Builder()
                .baseUrl("https://ipinfo.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IpInfoService ipService = ipRetrofit.create(IpInfoService.class);

        Retrofit weatherRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenMeteoService weatherService = weatherRetrofit.create(OpenMeteoService.class);

        WeatherRepository repository = new WeatherRepositoryImpl(ipService, weatherService);

        GetWeatherUseCase useCase = new GetWeatherUseCase(repository);

        return (T) new WeatherViewModel(useCase);
    }
}

