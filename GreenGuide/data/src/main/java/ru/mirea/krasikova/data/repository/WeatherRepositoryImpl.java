package ru.mirea.krasikova.data.repository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.krasikova.data.model.IpInfoResponse;
import ru.mirea.krasikova.data.model.WeatherResponse;
import ru.mirea.krasikova.data.network.IpInfoService;
import ru.mirea.krasikova.data.network.OpenMeteoService;
import ru.mirea.krasikova.domain.model.WeatherInfo;
import ru.mirea.krasikova.domain.repository.WeatherRepository;

public class WeatherRepositoryImpl implements WeatherRepository {
    private final IpInfoService ipInfoService;
    private final OpenMeteoService openMeteoService;

    public WeatherRepositoryImpl(IpInfoService ipInfoService, OpenMeteoService openMeteoService) {
        this.ipInfoService = ipInfoService;
        this.openMeteoService = openMeteoService;
    }

    @Override
    public void getWeatherByIp(RepositoryCallback<WeatherInfo> callback) {
        ipInfoService.getIpInfo().enqueue(new Callback<IpInfoResponse>() {
            @Override
            public void onResponse(Call<IpInfoResponse> call, Response<IpInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    IpInfoResponse ipInfo = response.body();

                    try {
                        String[] locParts = ipInfo.loc.split(",");
                        double lat = Double.parseDouble(locParts[0]);
                        double lon = Double.parseDouble(locParts[1]);

                        openMeteoService.getCurrentWeather(lat, lon, true)
                                .enqueue(new Callback<WeatherResponse>() {
                                    @Override
                                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> resp) {
                                        if (resp.isSuccessful() && resp.body() != null) {
                                            WeatherResponse.CurrentWeather w = resp.body().current_weather;
                                            WeatherInfo weatherInfo = new WeatherInfo(
                                                    ipInfo.city,
                                                    ipInfo.country,
                                                    w.temperature,
                                                    w.windspeed
                                            );
                                            callback.onSuccess(weatherInfo);
                                        } else {
                                            callback.onError("Ошибка при получении погоды");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                                        callback.onError("Сетевая ошибка: " + t.getMessage());
                                    }
                                });
                    } catch (Exception e) {
                        callback.onError("Ошибка при обработке координат: " + e.getMessage());
                    }
                } else {
                    callback.onError("Ошибка при получении IP-информации");
                }
            }

            @Override
            public void onFailure(Call<IpInfoResponse> call, Throwable t) {
                callback.onError("Сетевая ошибка: " + t.getMessage());
            }
        });
    }
}
