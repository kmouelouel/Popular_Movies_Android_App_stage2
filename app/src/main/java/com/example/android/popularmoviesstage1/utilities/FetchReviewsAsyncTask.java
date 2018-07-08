package com.example.android.popularmoviesstage1.utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmoviesstage1.models.Review;

import java.net.URL;
import java.util.List;

public class FetchReviewsAsyncTask extends AsyncTask<String, Void, List<Review>> {
    private static final String LOG_TAG = FetchReviewsAsyncTask.class.getSimpleName();
    private String mId;

    public FetchReviewsAsyncTask(String id) {
        this.mId = id;
    }

    @Override
    protected List<Review> doInBackground(String... strings) {
        URL reviewsRequestUrl = NetworkUtils.buildTrailersOrReviewsUrl(this.mId, "reviews");
        try {
            String jsonReviewsResponse = NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);
            final List<Review> reviews = OpenReviewsJsonUtils.getReviewsFromJson(jsonReviewsResponse);
            return reviews;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "error in collection the trailers");
            return null;
        }
    }
}
