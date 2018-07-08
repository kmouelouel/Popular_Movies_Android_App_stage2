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

    public FetchMoviesTask(Context context, GridViewAdapter gridViewAdapter) {
        mGridViewAdapter = gridViewAdapter;
        mContext = context;
        mLoadingIndicator = (ProgressBar) ((MainActivity) mContext).findViewById(R.id.pb_Loading_indicator);
        ;
        mErrorMessageDisplay = (TextView) ((MainActivity) mContext).findViewById(R.id.tv_error_message_display);
        ;
        mGridView = (GridView) ((MainActivity) mContext).findViewById(R.id.gridview);
        ;


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
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movies != null) {
            showMovieDataView();
            mGridViewAdapter.setData(movies);
        } else {
            showErrorMessage();
        }
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mGridView.setVisibility(View.VISIBLE);

    }

    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.INVISIBLE);

    }

}
