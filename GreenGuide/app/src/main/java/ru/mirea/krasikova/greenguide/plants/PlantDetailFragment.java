package ru.mirea.krasikova.greenguide.plants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import ru.mirea.krasikova.greenguide.R;
import ru.mirea.krasikova.domain.model.Plant;

public class PlantDetailFragment extends Fragment {

    private static final String ARG_PLANT_ID = "plant_id";

    private ImageView imageView;
    private TextView nameText;
    private TextView descriptionText;
    private TextView dateText;
    private PlantDetailViewModel viewModel;

    public static PlantDetailFragment newInstance(int plantId) {
        PlantDetailFragment fragment = new PlantDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PLANT_ID, plantId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_detail, container, false);

        imageView = view.findViewById(R.id.imagePlant);
        nameText = view.findViewById(R.id.textName);
        descriptionText = view.findViewById(R.id.textDescription);
        dateText = view.findViewById(R.id.textDate);
        Button backButton = view.findViewById(R.id.btnBackToMainMenu);

        int plantId = getArguments() != null ? getArguments().getInt(ARG_PLANT_ID, -1) : -1;
        if (plantId == -1) {
            requireActivity().getSupportFragmentManager().popBackStack();
            return view;
        }

        PlantDetailViewModelFactory factory = new PlantDetailViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(PlantDetailViewModel.class);

        // Подписка на LiveData
        viewModel.getPlantLiveData().observe(getViewLifecycleOwner(), plant -> {
            if (plant != null) {
                showPlantDetails(plant);
            }
        });

        viewModel.loadPlant(plantId);

        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void showPlantDetails(Plant plant) {
        nameText.setText(plant.getName());
        descriptionText.setText(plant.getDescription());
        dateText.setText("Добавлено: " + plant.getDateAdded());

        Glide.with(this)
                .load(plant.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }
}
