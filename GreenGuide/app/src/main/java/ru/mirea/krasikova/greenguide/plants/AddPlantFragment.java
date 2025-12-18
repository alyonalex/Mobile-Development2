package ru.mirea.krasikova.greenguide.plants;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import ru.mirea.krasikova.greenguide.databinding.FragmentAddPlantBinding;

public class AddPlantFragment extends Fragment {

    private FragmentAddPlantBinding binding;
    private AddPlantViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAddPlantBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        AddPlantViewModelFactory factory = new AddPlantViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(AddPlantViewModel.class);

        NavController navController = NavHostFragment.findNavController(this);

        binding.btnSavePlant.setOnClickListener(v -> {
            String name = binding.inputPlantName.getText().toString().trim();
            String description = binding.inputPlantDescription.getText().toString().trim();
            String imageUrl = binding.inputPlantImageUrl.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description)) {
                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.addPlant(name, description, imageUrl);
            Toast.makeText(requireContext(), "Растение добавлено!", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        });

        binding.btnCancel.setOnClickListener(v -> navController.navigateUp());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
