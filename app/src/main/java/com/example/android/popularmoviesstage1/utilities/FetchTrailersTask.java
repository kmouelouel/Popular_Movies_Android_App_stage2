package com.example.android.popularmoviesstage1.utilities;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.android.popularmoviesstage1.models.Trailer;

import java.net.URL;
import java.util.List;

public class FetchTrailersTask extends AsyncTask<String, Void, List<Trailer>> {
    private static final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
    private String id;

    public FetchTrailersTask(String id) {
        this.id = id;
    }

    @Override
    protected List<Trailer> doInBackground(String... params) {
        URL trailersRequestUrl = NetworkUtils.buildTrailersOrReviewsUrl(id, "videos");
        try {
            String jsonTailersResponse = NetworkUtils.getResponseFromHttpUrl(trailersRequestUrl);
            final List<Trailer> trailers = OpenTrailersJsonUtils.getTrailersFromJson(jsonTailersResponse);
            return trailers;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "error in collection the trailers");
            return null;
        }
    }


}
