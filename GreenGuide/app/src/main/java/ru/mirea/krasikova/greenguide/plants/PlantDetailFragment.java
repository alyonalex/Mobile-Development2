package ru.mirea.krasikova.greenguide.plants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import ru.mirea.krasikova.greenguide.R;
import ru.mirea.krasikova.greenguide.databinding.FragmentPlantDetailBinding;
import ru.mirea.krasikova.domain.model.Plant;

public class PlantDetailFragment extends Fragment {

    private FragmentPlantDetailBinding binding;
    private PlantDetailViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentPlantDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        int plantId = getArguments() != null ? getArguments().getInt("plant_id") : -1;
        if (plantId == -1) {
            Navigation.findNavController(view).navigateUp();
            return view;
        }

        PlantDetailViewModelFactory factory = new PlantDetailViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(PlantDetailViewModel.class);

        viewModel.getPlantLiveData().observe(getViewLifecycleOwner(), this::showPlantDetails);
        viewModel.loadPlant(plantId);

        // Назад к списку растений
        binding.btnBackToMainMenu.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp()
        );

        return view;
    }

    private void showPlantDetails(Plant plant) {
        if (plant == null) return;

        binding.textName.setText(plant.getName());
        binding.textDescription.setText(plant.getDescription());
        binding.textDate.setText("Добавлено: " + plant.getDateAdded());

        Glide.with(this)
                .load(plant.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.imagePlant);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
