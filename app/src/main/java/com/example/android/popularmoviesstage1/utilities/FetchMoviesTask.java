package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.MainActivity;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.adapters.GridViewAdapter;
import com.example.android.popularmoviesstage1.models.Movie;

import java.net.URL;
import java.util.List;

public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
    Context mContext;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private GridView mGridView;
    private GridViewAdapter mGridViewAdapter;
    private UserInterfaceViews userInterfaceViews;

    public FetchMoviesTask(Context context, GridViewAdapter gridViewAdapter) {
        mGridViewAdapter = gridViewAdapter;
        mContext = context;
        userInterfaceViews = new UserInterfaceViews(mContext);
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        String sortPath = params[0];
        URL moviesRequestURL = NetworkUtils.buildUrl(sortPath);
        try {
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestURL);
            List<Movie> MoviesJsonData = OpenMoviesJsonUtils.getMoviesFromJson(mContext, jsonResponse);
            return MoviesJsonData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        userInterfaceViews.setLoadingToVisible();
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        userInterfaceViews.setLoadingToInvisible();
        if (movies != null) {
            userInterfaceViews.showMovieDataView();
            mGridViewAdapter.setData(movies);
        } else {
            userInterfaceViews.showErrorMessage();
        }
    }


}
