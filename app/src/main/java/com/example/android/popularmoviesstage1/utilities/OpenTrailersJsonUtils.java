package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesstage1.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OpenTrailersJsonUtils {
    private static final String TAG = OpenTrailersJsonUtils.class.getSimpleName();
    private static final String KEY_ID = "id";
    private static final String KEY_KEY = "key";
    private static final String KEY_NAME = "name";
    private static final String KEY_SITE = "site";
    private static final String KEY_TYPE = "type";
    private static final String KEY_RESULTS = "results";
    private static List<Trailer> trailers;

    public OpenTrailersJsonUtils() {

    }

    public static List<Trailer> getTrailersFromJson(String TrailersJsonStr) throws JSONException {
        trailers = new ArrayList<>();
        try {
            JSONObject trailersJsonObject = new JSONObject(TrailersJsonStr);
            if (trailersJsonObject.has("status_message")) {
                String statusMessage = trailersJsonObject.getString("status_message");
                Log.e(TAG, "parse json Tailers have an error message: " + statusMessage);
                return null;
            } else {
                // get  the results json Array
                JSONArray resultsArray = trailersJsonObject.getJSONArray(KEY_RESULTS);

                //   Trailer trailerObjet;
                for (int i = 0; i < resultsArray.length(); i++) {
                    /* Get the JSON object representing the movie */
                    JSONObject TrailerJsonObject = resultsArray.getJSONObject(i);
                    Trailer trailerObjet = getTrailerFromJsonObject(TrailerJsonObject);
                    trailers.add(trailerObjet);
                }

                return trailers;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static Trailer getTrailerFromJsonObject(JSONObject trailerJsonObject) {
        Trailer currentTrailer = new Trailer();

        try {
            currentTrailer.setKey(trailerJsonObject.getString(KEY_KEY));
            currentTrailer.setName(trailerJsonObject.getString(KEY_NAME));
            currentTrailer.setSite(trailerJsonObject.getString(KEY_SITE));
            currentTrailer.setType(trailerJsonObject.getString(KEY_TYPE));
            return currentTrailer;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


}

