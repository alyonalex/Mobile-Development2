package ru.mirea.krasikova.greenguide.plants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import ru.mirea.krasikova.greenguide.R;
import ru.mirea.krasikova.greenguide.databinding.FragmentPlantListBinding;

public class PlantListFragment extends Fragment {

    private FragmentPlantListBinding binding;
    private PlantListViewModel viewModel;
    private PlantAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentPlantListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        adapter = new PlantAdapter();
        binding.plantsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.plantsRecycler.setAdapter(adapter);

        PlantListViewModelFactory factory = new PlantListViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(PlantListViewModel.class);

        viewModel.getPlantsLiveData().observe(getViewLifecycleOwner(), plants -> adapter.submitList(plants));

        adapter.setOnItemClickListener(plant -> {
            Bundle args = new Bundle();
            args.putInt("plant_id", plant.getId());

            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_list_to_detail, args);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadPlants();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
