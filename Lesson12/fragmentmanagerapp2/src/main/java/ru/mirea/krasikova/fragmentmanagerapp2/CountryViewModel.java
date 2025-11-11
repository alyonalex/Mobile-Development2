package ru.mirea.krasikova.fragmentmanagerapp2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CountryViewModel extends ViewModel {
    private final MutableLiveData<Country> selectedCountry = new MutableLiveData<>();

    public void selectCountry(Country country) {
        selectedCountry.setValue(country);
    }

    public LiveData<Country> getSelectedCountry() {
        return selectedCountry;
    }
}
