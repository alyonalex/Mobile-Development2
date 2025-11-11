package ru.mirea.krasikova.fragmentmanagerapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class DetailsFragment extends Fragment {
    private SharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        TextView tv = view.findViewById(R.id.textDetails);

        viewModel.getSomeValue().observe(getViewLifecycleOwner(), data -> {
            Log.d(DetailsFragment.class.getSimpleName(), data);
            tv.setText("Вы выбрали страну: " + data);
        });
    }
}