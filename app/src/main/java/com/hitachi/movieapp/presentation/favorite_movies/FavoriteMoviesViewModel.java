package com.hitachi.movieapp.presentation.favorite_movies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.repository.MovieRepository;
import com.hitachi.movieapp.data.repository.OnMovieDetailsFetchedListener;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavoriteMoviesViewModel extends ViewModel {
    private final MutableLiveData<List<Movie>> favoriteMovies;
    private final MovieRepository movieRepository;

    @Inject
    public FavoriteMoviesViewModel(
            MovieRepository movieRepository
    ) {
        this.movieRepository = movieRepository;
        this.favoriteMovies = new MutableLiveData<>();

    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        movieRepository.getFavoriteMovies().observeForever(favoriteMovies::setValue);
        return favoriteMovies;
    }


}
