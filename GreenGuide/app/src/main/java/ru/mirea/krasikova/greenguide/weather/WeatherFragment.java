package ru.mirea.krasikova.greenguide.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.krasikova.greenguide.R;
import ru.mirea.krasikova.greenguide.MainActivity;

public class WeatherFragment extends Fragment {

    private WeatherViewModel viewModel;
    private TextView cityText;
    private TextView countryText;
    private TextView temperatureText;
    private TextView windSpeedText;
    private TextView errorText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        cityText = view.findViewById(R.id.cityText);
        countryText = view.findViewById(R.id.countryText);
        temperatureText = view.findViewById(R.id.temperatureText);
        windSpeedText = view.findViewById(R.id.windSpeedText);
        errorText = view.findViewById(R.id.errorText);

        WeatherViewModelFactory factory = new WeatherViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(WeatherViewModel.class);

        viewModel.getWeatherLiveData().observe(getViewLifecycleOwner(), weatherInfo -> {
            if (weatherInfo != null) {
                cityText.setText("Город: " + weatherInfo.getCity());
                countryText.setText("Страна: " + weatherInfo.getCountry());
                temperatureText.setText(String.format("Температура: %.1f°C", weatherInfo.getTemperature()));
                windSpeedText.setText(String.format("Ветер: %.1f м/с", weatherInfo.getWindSpeed()));
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                errorText.setText("Ошибка: " + errorMessage);
            } else {
                errorText.setText("");
            }
        });

        viewModel.loadWeatherByIp();

        Button backButton = view.findViewById(R.id.btnBackToMainMenu);
        backButton.setOnClickListener(v -> {
            // Возврат к предыдущему экрану
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
