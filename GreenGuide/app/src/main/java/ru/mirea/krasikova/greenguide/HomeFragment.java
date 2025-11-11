package ru.mirea.krasikova.greenguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.krasikova.data.repository.AuthRepositoryImpl;
import ru.mirea.krasikova.domain.repository.AuthRepository;
import ru.mirea.krasikova.greenguide.R;
import ru.mirea.krasikova.greenguide.plants.PlantListFragment;
import ru.mirea.krasikova.greenguide.weather.WeatherFragment;

public class HomeFragment extends Fragment {

    private Button btnProfile, btnPlants, btnWeather;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnProfile = view.findViewById(R.id.btnProfile);
        btnPlants = view.findViewById(R.id.btnPlants);
        btnWeather = view.findViewById(R.id.btnWeather);

        btnPlants.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PlantListFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnWeather.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new WeatherFragment())
                    .addToBackStack(null)
                    .commit();
        });

        Button btnProfile = view.findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
