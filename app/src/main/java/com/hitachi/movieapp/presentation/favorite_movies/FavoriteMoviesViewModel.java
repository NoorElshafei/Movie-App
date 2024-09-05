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
    private final LiveData<List<Movie>> favoriteMovies;


    @Inject
    public FavoriteMoviesViewModel(
            MovieRepository movieRepository
    ) {
        this.favoriteMovies = movieRepository.getFavoriteMovies();

    }
    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }



}
