package ru.mirea.krasikova.fragmentmanagerapp2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    private CountryViewModel viewModel;
    private TextView nameTextView;
    private TextView capitalTextView;
    private TextView populationTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CountryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextView = view.findViewById(R.id.nameTextView);
        capitalTextView = view.findViewById(R.id.capitalTextView);
        populationTextView = view.findViewById(R.id.populationTextView);

        viewModel.getSelectedCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                nameTextView.setText(country.getName());
                capitalTextView.setText("Столица: " + country.getCapital());
                populationTextView.setText("Численность населения: " + country.getPopulation());
            }
        });
    }
}