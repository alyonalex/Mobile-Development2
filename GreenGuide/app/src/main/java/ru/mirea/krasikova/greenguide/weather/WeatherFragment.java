package ru.mirea.krasikova.greenguide.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ru.mirea.krasikova.greenguide.databinding.FragmentWeatherBinding;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;
    private WeatherViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this, new WeatherViewModelFactory()).get(WeatherViewModel.class);

        viewModel.getWeatherLiveData().observe(getViewLifecycleOwner(), weatherInfo -> {
            if (weatherInfo != null) {
                binding.cityText.setText("Город: " + weatherInfo.getCity());
                binding.countryText.setText("Страна: " + weatherInfo.getCountry());
                binding.temperatureText.setText(String.format("Температура: %.1f°C", weatherInfo.getTemperature()));
                binding.windSpeedText.setText(String.format("Ветер: %.1f м/с", weatherInfo.getWindSpeed()));
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            binding.errorText.setText((errorMessage != null && !errorMessage.isEmpty()) ? "Ошибка: " + errorMessage : "");
        });

        viewModel.loadWeatherByIp();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
