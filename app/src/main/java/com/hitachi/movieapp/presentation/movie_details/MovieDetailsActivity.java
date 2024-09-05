package com.hitachi.movieapp.presentation.movie_details;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hitachi.movieapp.R;
import com.hitachi.movieapp.databinding.ActivityMovieDetailsBinding;
import com.hitachi.movieapp.presentation.movies_list.MainActivity;
import com.hitachi.movieapp.utils.NetworkUtils;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * This Activity displays detailed information about a selected movie.
 * It retrieves movie details from the MovieDetailsViewModel and displays them on the screen.
 */
@AndroidEntryPoint
public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding binding;
    private MovieDetailsViewModel movieDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use DataBinding for easier view access
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        String imdbID = getIntent().getStringExtra("imdbID");

        // Initialize ViewModel
        movieDetailsViewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);

        // Check for internet connectivity before fetching data
        if (NetworkUtils.isInternetAvailable(this)) {
            getDetailsOfMovie(imdbID);
        }
        else {
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(MovieDetailsActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        // Observe ViewModel for any errors encountered
        movieDetailsViewModel.getError().observe(this, errorMsg -> {
            Toast.makeText(MovieDetailsActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        });

    }

    /**
     * Fetches movie details for the provided IMDb ID from the ViewModel.
     *
     * @param imdbID The IMDb ID of the movie.
     */
    private void getDetailsOfMovie(String imdbID) {
        movieDetailsViewModel.getMovieDetails(imdbID).observe(this, movie -> {
            if (movie != null) {
                binding.progressBar.setVisibility(View.GONE);
                binding.textViewTitleDetails.setText(movie.getTitle());
                binding.textViewCastDetails.setText(movie.getActors());
                binding.textViewGenreDetails.setText(movie.getGenre());
                binding.textViewDirectorDetails.setText(movie.getDirector());
                binding.textViewPlotDetails.setText(movie.getPlot());
                Glide.with(this).load(movie.getPoster()).placeholder(R.drawable.place_holder).into(binding.imageViewPosterDetails);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(MovieDetailsActivity.this, "Failed to load movie details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}