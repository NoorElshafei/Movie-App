package com.hitachi.movieapp.data.network;

import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.model.response.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbApi {
    @GET("/")
    Call<MovieResponse> getMovies(
            @Query("apikey") String apiKey,
            @Query("s") String query,
            @Query("page") int page);

    @GET("/")
    Call<Movie> getMovieDetails(
            @Query("i") String imdbID,       // IMDb ID of the movie
            @Query("apikey") String apiKey   // Your OMDb API key
    );
}

