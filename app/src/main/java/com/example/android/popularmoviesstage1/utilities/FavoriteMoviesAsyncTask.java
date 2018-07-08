package com.example.android.popularmoviesstage1.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.MainActivity;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.adapters.GridViewAdapter;
import com.example.android.popularmoviesstage1.data.MoviesContract;
import com.example.android.popularmoviesstage1.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMoviesAsyncTask extends AsyncTask<Void, Void, Cursor> {
    Context mContext;
    private GridViewAdapter mGridViewAdapter;
    private UserInterfaceViews userInterfaceViews;

    public FavoriteMoviesAsyncTask(Context context, GridViewAdapter gridViewAdapter) {
        mGridViewAdapter = gridViewAdapter;
        mContext = context;
        userInterfaceViews = new UserInterfaceViews(mContext);

    }

    //invoked on background thread;
    @Override
    protected Cursor doInBackground(Void... voids) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        return cursor;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        userInterfaceViews.setLoadingToVisible();
    }

    //invoked on UI Thread
    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        List<Movie> movies = convertCursorToListMovies(cursor);
        userInterfaceViews.setLoadingToInvisible();
        if (movies.size() != 0) {
            userInterfaceViews.showMovieDataView();
            mGridViewAdapter.setData(movies);
        } else {
            userInterfaceViews.showErrorMessage();

        }
    }

    private List<Movie> convertCursorToListMovies(Cursor mCursor) {
        List<Movie> movies = new ArrayList<>();
        Movie currentMovie;
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                currentMovie = new Movie();
                currentMovie.setTitle(mCursor.getString(
                        mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE)));
                currentMovie.setId(mCursor.getInt(
                        mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID)));
                currentMovie.setOverview(mCursor.getString(
                        mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_DESCRIPTION)));
                currentMovie.setPoster(mCursor.getString(
                        mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_PATH)));
                currentMovie.setDate(mCursor.getString(
                        mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE)));
                currentMovie.setRating(mCursor.getInt(
                        mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE)));

                movies.add(currentMovie);
            }
        }

        return movies;

    }

}