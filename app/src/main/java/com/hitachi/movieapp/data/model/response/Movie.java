package com.hitachi.movieapp.data.model.response;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * @Created_by: Noor Elshafei
 * @Date: 9/2/2024
 */

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey
    @NonNull
    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    @SerializedName("Genre")
    private String genre;
    @SerializedName("Director")
    private String director;
    @SerializedName("Writer")
    private String writer;
    @SerializedName("Actors")
    private String actors;
    @SerializedName("Plot")
    private String plot;
    @SerializedName("Poster")
    private String poster;
    @SerializedName("imdbID")
    private String imdbID;
    @SerializedName("Response")
    private String response;
    @SerializedName("Error")
    private String errorMsg;
    @SerializedName("Type")
    private String type;
    private boolean isFavorite;



    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }
    public String getWriter() {
        return writer;
    }


    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getPoster() {
        return poster;
    }

    public String getResponse() {
        return response;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getType() {
        return type;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setType(String type) {
        this.type = type;
    }
}
