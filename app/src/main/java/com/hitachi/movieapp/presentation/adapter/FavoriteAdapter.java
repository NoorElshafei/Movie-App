package com.hitachi.movieapp.presentation.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hitachi.movieapp.R;
import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.presentation.movies_list.OnFavoriteClickListener;
import com.hitachi.movieapp.presentation.movies_list.OnMovieClickListener;

import java.util.List;

/**
 * Adapter for displaying a list of favorite movies in a RecyclerView.
 * Handles view binding, click events, and updates to the movie list.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    // List of favorite movies to display
    private final List<Movie> movieList;
    // Callback for when a movie item is clicked
    private final OnMovieClickListener listener;

    public FavoriteAdapter(List<Movie> movieList,
                           OnMovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MovieViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateMovies(List<Movie> movies) {
        movieList.addAll(movies);
        notifyDataSetChanged();
    }


    public class MovieViewHolder extends BaseViewHolder {
        TextView title, year;
        ImageView poster, fav;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            year = itemView.findViewById(R.id.textViewYear);
            poster = itemView.findViewById(R.id.imageViewPoster);
        }

        @Override
        public void onBind(int position) {

                Movie movie = movieList.get(position);
                title.setText(movie.getTitle());
                year.setText(movie.getYear());
                Glide.with(poster.getContext()).load(movie.getPoster()).into(poster);
                itemView.setOnClickListener(v -> listener.onMovieClick(movie));

            }
        }

}
