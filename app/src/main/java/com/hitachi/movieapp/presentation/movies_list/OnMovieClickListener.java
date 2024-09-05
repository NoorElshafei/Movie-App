package com.hitachi.movieapp.presentation.movies_list;

import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.data.model.response.MovieResponse;

public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }