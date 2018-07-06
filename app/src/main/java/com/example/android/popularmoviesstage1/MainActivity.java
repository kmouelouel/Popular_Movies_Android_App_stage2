package com.example.android.popularmoviesstage1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.adapters.GridViewAdapter;
import com.example.android.popularmoviesstage1.models.Movie;
import com.example.android.popularmoviesstage1.utilities.FavoriteMoviesAsyncTask;
import com.example.android.popularmoviesstage1.utilities.FetchMoviesTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LIFECYCLE_CALLBACKS_MENU_SETTING = "optionSelected";
    private static String SORT_PATH = "top_rated";
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private GridView mGridView;
    private GridViewAdapter gridViewAdapter;
    private int selectedOption = R.id.action_highest_rated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_Loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        //set the GridView :
        gridViewAdapter = new GridViewAdapter(MainActivity.this, new ArrayList<Movie>());
        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setAdapter(gridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie MovieSelectedData = gridViewAdapter.getItem(position);
                Context context = getApplicationContext();
                Class destination = MovieDetail.class;
                Intent intentToDisplayMovieDetails = new Intent(context, destination);
                intentToDisplayMovieDetails.putExtra("movieDetails", MovieSelectedData);
                startActivity(intentToDisplayMovieDetails);

            }
        });
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_MENU_SETTING)) {
                selectedOption = savedInstanceState.getInt(LIFECYCLE_CALLBACKS_MENU_SETTING);
                loadAdapterPerOptionSelected(selectedOption);
            }
        } else {
            loadMovieData(SORT_PATH);
        }

    }

    //test if you have an internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

    }

    private void loadMovieData(String sortPath) {
        if (sortPath.equals("top_rated") || sortPath.equals("popular")) {
            if (isNetworkAvailable()) {
                FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(MainActivity.this, gridViewAdapter);
                fetchMoviesTask.execute(sortPath);
            }
        } else if (sortPath.equals("favorites")) {
            FavoriteMoviesAsyncTask favoriteMoviesAsyncTask = new FavoriteMoviesAsyncTask(MainActivity.this, gridViewAdapter);
            favoriteMoviesAsyncTask.execute();
        } else {
            Log.e(TAG, "A problem with your internet connection!");
            mErrorMessageDisplay.setText("You do not have an internet connection...");
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.settings, menu);
        /* Return true so that the menu is displayed in the Toolbar */

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        gridViewAdapter.clear();
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                SORT_PATH = "popular";
                loadMovieData(SORT_PATH);
                return true;
            case R.id.action_highest_rated:
                SORT_PATH = "top_rated";
                loadMovieData(SORT_PATH);
                return true;
            case R.id.action_favorite_movies:
                SORT_PATH = "favorites";
                loadMovieData(SORT_PATH);
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }

    }


    private void loadAdapterPerOptionSelected(int selectedOption) {
        selectedOption = selectedOption;
        if (selectedOption == R.id.action_most_popular) {
            SORT_PATH = "popular";

        } else if (selectedOption == R.id.action_highest_rated) {
            SORT_PATH = "top_rated";
        } else if (selectedOption == R.id.action_favorite_movies) {
            SORT_PATH = "favorites";
        }
        loadMovieData(SORT_PATH);
    }

    //   Override onSaveInstanceState
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //  Call super.onSaveInstanceState
        super.onSaveInstanceState(outState);
        outState.putInt(LIFECYCLE_CALLBACKS_MENU_SETTING, selectedOption);
    }


}
