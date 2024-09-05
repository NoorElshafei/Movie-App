package com.hitachi.movieapp.presentation.favorite_movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.databinding.ActivityFavouriteMoviesBinding;
import com.hitachi.movieapp.presentation.adapter.FavoriteAdapter;
import com.hitachi.movieapp.presentation.movie_details.MovieDetailsActivity;
import com.hitachi.movieapp.presentation.movies_list.OnMovieClickListener;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavoriteMoviesActivity extends AppCompatActivity implements OnMovieClickListener {
    private ActivityFavouriteMoviesBinding binding;
    private FavoriteAdapter adapter;
    private FavoriteMoviesViewModel favoriteMoviesViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavouriteMoviesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        favoriteMoviesViewModel = new ViewModelProvider(this).get(FavoriteMoviesViewModel.class);
        adapter = new FavoriteAdapter(new ArrayList<>(), this);
        binding.recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMovies.setAdapter(adapter);

        favoriteMoviesViewModel.getFavoriteMovies().observe(this, movies -> {
            adapter.updateMovies(movies);

        });

    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(FavoriteMoviesActivity.this, MovieDetailsActivity.class);
        intent.putExtra("imdbID", movie.getImdbID());
        startActivity(intent);
    }
}