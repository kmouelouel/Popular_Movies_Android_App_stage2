package com.example.android.popularmoviesstage1.data;

import android.net.Uri;
import android.provider.BaseColumns;

import javax.crypto.spec.OAEPParameterSpec;

public class MoviesContract {
    public static final String AUTHORITY = "com.example.android.popularmoviesstage1";
    public static final String PATH = "movies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private MoviesContract() {

    }

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH)
                .build();
        //  created a static final members for the table name and each of the db columns
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_DESCRIPTION = "movieDescription";
        public static final String COLUMN_MOVIE_POSTER_PATH = "moviePosterPath";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movieVoteAverage";

        public static final String CREATE_TABLE_MOVIES = "CREATE TABLE " +
                MoviesEntry.TABLE_NAME + "(" +
                MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_MOVIE_DESCRIPTION + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE + " LONG NOT NULL" +
                ");";

    }
}
