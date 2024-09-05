package com.hitachi.movieapp.data.repository;

import com.hitachi.movieapp.data.model.response.Movie;

public interface OnMovieDetailsFetchedListener {
        void onFetched(Movie movie);

        void onError(String error);
    }