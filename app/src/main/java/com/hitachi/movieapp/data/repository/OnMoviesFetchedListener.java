package com.hitachi.movieapp.data.repository;

import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.model.response.MovieResponse;

import java.util.List;

public interface OnMoviesFetchedListener {
        void onFetched(MovieResponse movies);

        void onError(String error);
    }