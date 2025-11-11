package ru.mirea.krasikova.fragmentmanagerapp2;

public class Country {
    private final String name;
    private final String capital;
    private final int population;

    public Country(String name, String capital, int population) {
        this.name = name;
        this.capital = capital;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }

    public int getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return name;
    }
}