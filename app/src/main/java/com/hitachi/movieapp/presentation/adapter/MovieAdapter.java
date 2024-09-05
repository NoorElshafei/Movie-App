package com.hitachi.movieapp.presentation.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hitachi.movieapp.R;
import com.hitachi.movieapp.data.model.response.Movie;
import com.hitachi.movieapp.presentation.movies_list.OnFavoriteClickListener;
import com.hitachi.movieapp.presentation.movies_list.OnMovieClickListener;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final List<Movie> movieList;
    private final OnMovieClickListener listener;
    private OnFavoriteClickListener onFavoriteClickListener;
    private List<Movie> favoriteMovies;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_MOVIE = 1;
    private boolean isLoadingAdded = false;


    public MovieAdapter(List<Movie> movieList,
                        OnMovieClickListener listener,
                        OnFavoriteClickListener onFavoriteClickListener
    ) {
        this.onFavoriteClickListener = onFavoriteClickListener;
        this.favoriteMovies = new ArrayList<>();
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MOVIE) {
            return new MovieViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_movie, parent, false));
        } else {
            return new ProgressHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(position);
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        movieList.add(new Movie()); // Adding an empty item as the loading placeholder
        notifyItemInserted(movieList.size() - 1);
    }


    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = movieList.size() - 1;
        Movie item = getItem(position);
        if (item != null) {
            movieList.remove(position);
            notifyItemRemoved(position);
        }
    }

    private Movie getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieList.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_MOVIE;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateMovies(List<Movie> movies) {
        movieList.addAll(movies);
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> newMovies) {
        int startPosition = movieList.size();
        this.movieList.addAll(newMovies);
        notifyItemRangeInserted(startPosition, newMovies.size());
    }

    public void removeMovies() {
        movieList.clear();
        notifyDataSetChanged();
    }

    public void setFavouritMovies(List<Movie> movies) {
        favoriteMovies = movies;

    }

    public List<Movie> getAllMovies() {
        return movieList;

    }

    public void editMovie(int position) {
        if (!movieList.isEmpty()) {
            movieList.get(position).setFavorite(true);
            notifyItemChanged(position);
        }
    }


    public class MovieViewHolder extends BaseViewHolder {
        TextView title, year;
        ImageView poster, fav;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            year = itemView.findViewById(R.id.textViewYear);
            poster = itemView.findViewById(R.id.imageViewPoster);
            fav = itemView.findViewById(R.id.buttonFavorite);

        }

        @Override
        public void onBind(int position) {
            if (getItemViewType() == VIEW_TYPE_MOVIE) {
                Movie movie = movieList.get(position);
                title.setText(movie.getTitle());
                year.setText(movie.getYear());
                Glide.with(poster.getContext()).load(movie.getPoster()).into(poster);
                if (movie.isFavorite())
                    Glide.with(fav.getContext()).load(R.drawable.baseline_favorite_24).into(fav);
                else
                    Glide.with(fav.getContext()).load(R.drawable.baseline_favorite_border_24).into(fav);

                itemView.setOnClickListener(v -> listener.onMovieClick(movie));
                fav.setOnClickListener(v -> {
                    onFavoriteClickListener.onFavoriteClick(movie);
                    // Toggle favorite status
                    movie.setFavorite(!movie.isFavorite());
                    notifyItemChanged(position);
                });

            }
        }
    }

    public static class ProgressHolder extends BaseViewHolder {
        public ProgressHolder(View itemView) {
            super(itemView);
        }
    }
}
