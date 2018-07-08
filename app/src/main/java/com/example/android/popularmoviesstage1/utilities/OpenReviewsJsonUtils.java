package com.example.android.popularmoviesstage1.utilities;

import android.util.Log;

import com.example.android.popularmoviesstage1.models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenReviewsJsonUtils {

    private static final String KEY_RESULTS = "results";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_URL = "url";
    private static final String TAG = OpenReviewsJsonUtils.class.getSimpleName();
    private static List<Review> reviews;

    public OpenReviewsJsonUtils() {

    }

    public static List<Review> getReviewsFromJson(String ReviewJsonObjectString) throws JSONException {
        reviews = new ArrayList<>();
        try {
            JSONObject reviewsJsonObject = new JSONObject(ReviewJsonObjectString);
            if (reviewsJsonObject.has("status_message")) {
                String statusMessage = reviewsJsonObject.getString("status_message");
                Log.e(TAG, "parse json Reviews have an error message: " + statusMessage);
                return null;
            } else {
                // get  the results json Array
                JSONArray resultsArray = reviewsJsonObject.getJSONArray(KEY_RESULTS);

                //   Trailer trailerObjet;
                for (int i = 0; i < resultsArray.length(); i++) {
                    /* Get the JSON object representing the movie */
                    JSONObject reviewJsonObject = resultsArray.getJSONObject(i);
                    Review reviewDataObject = getReviewDataFromJsonObject(reviewJsonObject);
                    reviews.add(reviewDataObject);
                }

                return reviews;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }//end getReviewsFromJson

    private static Review getReviewDataFromJsonObject(JSONObject reviewJsonObject) {
        Review currentReview = new Review();
        try {
            currentReview.setAuthor(reviewJsonObject.getString(KEY_AUTHOR));
            currentReview.setContent(reviewJsonObject.getString(KEY_CONTENT));
            return currentReview;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Impossible to extract a review object from JsonObject");
            return null;
        }
    }
}
