package com.example.android.popularmoviesstage1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Movie implements Parcelable {
    private int id;
    private String title; // original_title
    private String poster; // poster_path
    private String backdropImage; // backdrop_path
    private String overview;
    private int rating; // vote_average
    private String date; // release_date

    public Movie() {

    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        poster = in.readString();
        backdropImage = in.readString();
        overview = in.readString();
        rating = in.readInt();
        date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setBackdrop(String backdrop) {
        this.backdropImage = backdrop;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getBackdrop() {
        return backdropImage;
    }

    public String getOverview() {
        return overview;
    }

    public int getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(backdropImage);
        dest.writeString(overview);
        dest.writeInt(rating);
        dest.writeString(date);
    }
}
