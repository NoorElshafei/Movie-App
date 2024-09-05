package com.hitachi.movieapp.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hitachi.movieapp.BuildConfig;
import com.hitachi.movieapp.data.local.AppDatabase;
import com.hitachi.movieapp.data.local.MovieDao;
import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.model.response.MovieResponse;
import com.hitachi.movieapp.data.network.OmdbApi;
import com.hitachi.movieapp.data.network.RetrofitClient;
import com.hitachi.movieapp.utils.MyApplication;
import com.hitachi.movieapp.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final OmdbApi omdbApi;
    private final MovieDao movieDao;
    private static final String API_KEY = BuildConfig.OMDB_API_KEY;

    @Inject
    public MovieRepository(
            MovieDao movieDao,
            OmdbApi omdbApi) {
        this.movieDao = movieDao;
        this.omdbApi = omdbApi;
    }

    public void getMovies(String query, int page, OnMoviesFetchedListener listener) {

        omdbApi.getMovies(API_KEY, query, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                String responseString = Objects.requireNonNull(response.body()).getResponse();
                if (response.isSuccessful() && responseString.equals("True")) {

                    listener.onFetched(response.body());
                } else {
                    listener.onError(response.body().getErrorMsg());

                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                if (t instanceof IOException) {
                    // Network or connectivity error, such as no internet connection
                    listener.onError("No internet connection");
                } else
                    listener.onError(t.getMessage());

            }
        });
    }

    public void getMovieDetails(String imdbID, OnMovieDetailsFetchedListener listener) {
        Call<Movie> call = omdbApi.getMovieDetails(imdbID, API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    listener.onFetched(response.body());
                } else {
                    listener.onError("Error fetching movie details");
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public void updateFavoriteStatus(Movie movie) {
        if (movie.isFavorite())
            new Thread(() -> movieDao.deleteById(movie.getImdbID())).start();
        else
            new Thread(() -> movieDao.insert(movie)).start();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return movieDao.getFavoriteMovies();
    }


}
