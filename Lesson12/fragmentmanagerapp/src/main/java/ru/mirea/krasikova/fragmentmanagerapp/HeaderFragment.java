package ru.mirea.krasikova.fragmentmanagerapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HeaderFragment extends Fragment {
    private SharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_header, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        ListView listView = view.findViewById(R.id.listView);

        String[] countries = {"Венгрия", "Великобритания", "Германия", "Испания", "Канада", "Марокко",
                "Норвегия", "Португалия", "Россия", "Турция", "ОАЭ", "Уругвай", "Франция"};
        listView.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, countries));

        listView.setOnItemClickListener((AdapterView<?> parent, View v, int pos, long id) ->
                viewModel.setSomeValue(countries[pos]));
    }
}