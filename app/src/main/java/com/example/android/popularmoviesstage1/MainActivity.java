package com.example.android.popularmoviesstage1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.android.popularmoviesstage1.utilities.UserInterfaceViews;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LIFECYCLE_CALLBACKS_MENU_SETTING = "optionSelected";
    private static final String MOVIE_DATA = "movieDetails";
    private static final String TOP_RATED_FILTER = "top_rated";
    private static final String POPULAR_FILTER = "popular";
    private static final String FAVORITE_FILTER = "favorite";
    private static String SORT_PATH = TOP_RATED_FILTER;
    @BindView(R.id.pb_Loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.gridView)
    GridView mGridView;

    private GridViewAdapter gridViewAdapter;
    private int selectedOption = R.id.action_highest_rated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //set the GridView :
        gridViewAdapter = new GridViewAdapter(MainActivity.this, new ArrayList<Movie>());
        //    mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setAdapter(gridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie MovieSelectedData = gridViewAdapter.getItem(position);
                Context context = getApplicationContext();
                Class destination = MovieDetail.class;
                Intent intentToDisplayMovieDetails = new Intent(context, destination);
                intentToDisplayMovieDetails.putExtra(MOVIE_DATA, MovieSelectedData);
                startActivity(intentToDisplayMovieDetails);

            }
        });

        if (savedInstanceState != null) {
            loadAdapterPerOptionSelected(
                    savedInstanceState.getInt(LIFECYCLE_CALLBACKS_MENU_SETTING, selectedOption));

        } else {
            loadMovieData(SORT_PATH);
        }
    }// end OnCreate


    //test if you have an internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

    }

    private void loadMovieData(String sortPath) {
        gridViewAdapter.clear();
        if (sortPath.equals(FAVORITE_FILTER)) {
            this.selectedOption = R.id.action_favorite_movies;
            FavoriteMoviesAsyncTask favoriteMoviesAsyncTask = new FavoriteMoviesAsyncTask(MainActivity.this, gridViewAdapter);
            favoriteMoviesAsyncTask.execute();
        } else if (sortPath.equals(TOP_RATED_FILTER) || sortPath.equals(POPULAR_FILTER)) {
            if (isNetworkAvailable()) {
                this.selectedOption = (sortPath.equals(TOP_RATED_FILTER)) ? R.id.action_highest_rated : R.id.action_most_popular;
                FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(MainActivity.this, gridViewAdapter);
                fetchMoviesTask.execute(sortPath);
            } else {
                Log.e(TAG, "A problem with your internet connection!");
                mErrorMessageDisplay.setText(R.string.no_internet);
                mErrorMessageDisplay.setVisibility(View.VISIBLE);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                this.selectedOption = 0;
            }
        }
        UpdateTitle(this.selectedOption);

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
        loadAdapterPerOptionSelected(item.getItemId());
        return super.onOptionsItemSelected(item);

    }


    private void loadAdapterPerOptionSelected(int inSelectedOption) {
        selectedOption = inSelectedOption;
        UpdateTitle(inSelectedOption);
        if (inSelectedOption == R.id.action_highest_rated) {
            SORT_PATH = TOP_RATED_FILTER;

        } else if (inSelectedOption == R.id.action_most_popular) {
            SORT_PATH = POPULAR_FILTER;
        } else if (inSelectedOption == R.id.action_favorite_movies) {
            SORT_PATH = FAVORITE_FILTER;
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

    private void UpdateTitle(int inSelectedOption) {
        switch (inSelectedOption) {
            case R.id.action_highest_rated:
                this.setTitle("Popular Movies - " + "Top Rated");
                break;
            case R.id.action_most_popular:
                this.setTitle("Popular Movies - " + "Popular");
                break;
            case R.id.action_favorite_movies:
                this.setTitle("Popular Movies - " + "Favorites");
                break;
            default:
                this.setTitle("Popular Movies");
        }

    }

}
