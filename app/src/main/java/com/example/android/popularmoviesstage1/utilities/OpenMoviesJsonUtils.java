package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesstage1.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenMoviesJsonUtils {
    private static final String TAG = OpenMoviesJsonUtils.class.getSimpleName();
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ID = "id";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_POSTER = "poster_path";
    private static final String KEY_BACKDROP_IMAGE = "backdrop_path";
    private static List<Movie> movies;

    public static List<Movie> getMoviesFromJson(Context context, String moviesJsonStr) throws JSONException {
        movies = new ArrayList<>();
        try {
            JSONObject moviesJsonObject = new JSONObject(moviesJsonStr);
            if (moviesJsonObject.has("status_message")) {
                String statusMessage = moviesJsonObject.getString("status_message");
                Log.e(TAG, "parse json movie has an error message: " + statusMessage);
                return null;
            } else {
                // get  the results json Array
                JSONArray resultsArray = moviesJsonObject.getJSONArray(KEY_RESULTS);

                Movie movieData;
                for (int i = 0; i < resultsArray.length(); i++) {
                    /* Get the JSON object representing the movie */
                    JSONObject movieObject = resultsArray.getJSONObject(i);
                    movieData = getMovieData(movieObject);
                    movies.add(movieData);


                }
                return movies;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;

        }

    }

    public static Movie getMovieData(JSONObject movieObject) {
        Movie currentMovie = new Movie();
        try {

            currentMovie.setId(movieObject.getInt(KEY_ID));
            currentMovie.setPoster(movieObject.getString(KEY_POSTER));
            currentMovie.setBackdrop(movieObject.getString(KEY_BACKDROP_IMAGE));
            currentMovie.setDate(movieObject.getString(KEY_RELEASE_DATE));
            currentMovie.setOverview(movieObject.getString(KEY_OVERVIEW));
            currentMovie.setRating(movieObject.getInt(KEY_VOTE_AVERAGE));
            currentMovie.setTitle(movieObject.getString(KEY_TITLE));
            return currentMovie;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


}
