package ru.mirea.krasikova.fragmentmanagerapp2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HeaderFragment extends Fragment {

    private CountryViewModel viewModel;
    private List<Country> countries;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CountryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_header, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countries = new ArrayList<>();
        countries.add(new Country("Венгрия", "Будапешт", 9603634 ));
        countries.add(new Country("Германия", "Берлин", 83491249));
        countries.add(new Country("Испания", "Мадрид", 49315949));
        countries.add(new Country("Россия", "Москва", 146119928));
        countries.add(new Country("Португалия", "Лиссабон", 10467366));
        countries.add(new Country("Франция", "Париж", 67421162));

        ListView listView = view.findViewById(R.id.countriesListView);

        ArrayAdapter<Country> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                countries
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, itemView, position, id) -> {
            Country selectedMovie = countries.get(position);

            viewModel.selectCountry(selectedMovie);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, DetailsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });
    }
}