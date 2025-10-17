package ru.mirea.krasikovaaa.data.storage;

import ru.mirea.krasikovaaa.data.storage.models.Movie;

public interface MovieStorage {
    public Movie get();
    public boolean save(Movie movie);
}