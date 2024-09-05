package com.hitachi.movieapp.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    @SerializedName("Search")
    private List<Movie> movies;
    @SerializedName("Response")
    private String response;
    @SerializedName("Error")
    private String errorMsg;
    @SerializedName("totalResults")
    private String totalResults;


    public List<Movie> getMovies() {
        return movies;
    }

    public String getResponse() {
        return response;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}