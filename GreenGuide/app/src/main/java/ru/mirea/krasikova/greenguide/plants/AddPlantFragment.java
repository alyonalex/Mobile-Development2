package ru.mirea.krasikova.greenguide.plants;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.krasikova.greenguide.R;

public class AddPlantFragment extends Fragment {

    private EditText nameInput, descriptionInput, imageUrlInput;
    private Button saveButton, cancelButton;
    private AddPlantViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_plant, container, false);

        nameInput = view.findViewById(R.id.inputPlantName);
        descriptionInput = view.findViewById(R.id.inputPlantDescription);
        imageUrlInput = view.findViewById(R.id.inputPlantImageUrl);
        saveButton = view.findViewById(R.id.btnSavePlant);
        cancelButton = view.findViewById(R.id.btnCancel);

        AddPlantViewModelFactory factory = new AddPlantViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(this, factory).get(AddPlantViewModel.class);

        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String imageUrl = imageUrlInput.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description)) {
                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.addPlant(name, description, imageUrl);

            Toast.makeText(requireContext(), "Растение добавлено!", Toast.LENGTH_SHORT).show();

            // Возврат к списку
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        cancelButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
