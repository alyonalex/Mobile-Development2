package ru.mirea.krasikovaaa.domain.usecases;

import ru.mirea.krasikovaaa.domain.models.MovieDomain;
import ru.mirea.krasikovaaa.domain.repository.MovieRepository;
public class SaveMovieToFavoriteUseCase {
    private MovieRepository movieRepository;
    public SaveMovieToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public boolean execute(MovieDomain movie){
        return movieRepository.saveMovie(movie);
    }
}
