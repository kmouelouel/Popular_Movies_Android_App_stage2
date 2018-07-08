package com.example.android.popularmoviesstage1.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmoviesstage1.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String API_KEY = BuildConfig.API_KEY;
    private final static String API_KEY_PARAM = "api_key";

    public static URL buildUrl(String sortPath) {
        //https://api.themoviedb.org/3/movie/top_rated?api_key=api_key
        Uri.Builder builtUri = new Uri.Builder();
        builtUri.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(sortPath)
                .appendQueryParameter(API_KEY_PARAM, API_KEY);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String buildPosterUrl(String posterPath) {
        return "http://image.tmdb.org/t/p/w342/" + posterPath;
    }

    //Stage 2: build URL To fetch Trailers or reviews

    public static URL buildTrailersOrReviewsUrl(String id, String trailerOrReview) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(id)
                .appendPath(trailerOrReview)
                .appendQueryParameter(API_KEY_PARAM, API_KEY);
        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Error in building URL to fetch trailers or reviews");
        }
        return url;
    }

    public static String buildYouTubeUrl(String key) {
        String baseUrl = "https://www.youtube.com/watch?v=";
        return baseUrl + key;
    }


}
