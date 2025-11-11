package ru.mirea.krasikova.greenguide.plants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.mirea.krasikova.greenguide.R;
import ru.mirea.krasikova.data.storage.SharedPrefsUserStorage;
import ru.mirea.krasikova.domain.model.Plant;

public class PlantListFragment extends Fragment {

    private PlantListViewModel viewModel;
    private PlantAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_list, container, false);

        SharedPrefsUserStorage userStorage = new SharedPrefsUserStorage(requireContext());
        String userType = userStorage.getUserType();

        RecyclerView recyclerView = view.findViewById(R.id.plantsRecycler);
        adapter = new PlantAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        PlantListViewModelFactory factory = new PlantListViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(PlantListViewModel.class);

        // наблюдаем за обновлениями данных
        viewModel.getPlantsLiveData().observe(getViewLifecycleOwner(), plants -> {
            adapter.submitList(plants);
        });

        // переход к деталям растения
        adapter.setOnItemClickListener(plant -> {
            Fragment detailFragment = PlantDetailFragment.newInstance(plant.getId());
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // кнопка добавления
        FloatingActionButton addButton = view.findViewById(R.id.addPlantButton);
        if (!userType.equals("authorized")) {
            addButton.setVisibility(View.GONE);
        }

        addButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddPlantFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // кнопка назад
        Button backButton = view.findViewById(R.id.btnBackToMainMenu);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadPlants();
    }
}
