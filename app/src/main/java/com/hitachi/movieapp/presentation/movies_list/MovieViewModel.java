package com.hitachi.movieapp.presentation.movies_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.model.response.MovieResponse;
import com.hitachi.movieapp.data.repository.MovieRepository;
import com.hitachi.movieapp.data.repository.OnMoviesFetchedListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MovieViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private final MutableLiveData<MovieResponse> moviesLiveData;
    private  ArrayList<Movie> movies;
    private  MutableLiveData<List<Movie>> sortedMoviesLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final LiveData<List<Movie>> favoriteMovies;

    @Inject
    public MovieViewModel(
            MovieRepository movieRepository
    ) {
        this.movieRepository = movieRepository;
        moviesLiveData = new MutableLiveData<>();
        sortedMoviesLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        movies = new ArrayList<>();
        this.favoriteMovies = movieRepository.getFavoriteMovies();

    }
    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public LiveData<MovieResponse> getMoviesLiveData() {
        return moviesLiveData;
    }


    public LiveData<List<Movie>> getSortedMovies() {
        return sortedMoviesLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void loadMovies(String query, int currentPage) {
        movieRepository.getMovies(query, currentPage, new OnMoviesFetchedListener() {
            @Override
            public void onFetched(MovieResponse movieResponse) {
                movies.addAll(movieResponse.getMovies());
                moviesLiveData.setValue(movieResponse);
            }

            @Override
            public void onError(String error) {
                errorLiveData.setValue(error);
            }
        });
    }

    public void removeMovies() {
        movies.clear();
    }



    public void sortMovies(String sortOption) {
        List<Movie> sortedMovies = new ArrayList<>(movies);
        if (sortOption.equals("Sort by Year")) {
            sortedMovies.sort(Comparator.comparing(Movie::getYear));
        } else if (sortOption.equals("Sort by Title")) {
            sortedMovies.sort(Comparator.comparing(Movie::getTitle));
        }
        sortedMoviesLiveData.setValue(sortedMovies);
    }

    public void loadNextPage(String query, int currentPage) {

        loadMovies(query, currentPage);
    }

    public void updateFavoriteStatus(Movie movie) {
        movieRepository.updateFavoriteStatus(movie);
    }
}
