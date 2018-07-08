package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.models.Movie;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {
    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private RatingBar mVoteAverageTextView;
    private TextView mRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mPosterImageView = (ImageView) findViewById(R.id.image_details_poster);
        mOverviewTextView = (TextView) findViewById(R.id.tv_overview);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mVoteAverageTextView = (RatingBar) findViewById(R.id.rb_vote_average);
        mRating = (TextView) findViewById(R.id.tv_rating);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movieDetails")) {
                Movie mMovie = (Movie) intentThatStartedThisActivity.getParcelableExtra("movieDetails");
                mTitleTextView.setText(mMovie.getTitle());
                mReleaseDateTextView.setText(mMovie.getDate());
                mOverviewTextView.setText(mMovie.getOverview());
                String image_url = NetworkUtils.buildPosterUrl(mMovie.getPoster());
                mVoteAverageTextView.setRating((int) mMovie.getRating());
                Picasso.with(getApplicationContext()).load(image_url).into(mPosterImageView);
                String rating = String.valueOf(mMovie.getRating());
                mRating.setText(" ( " + rating + " )");
            }
        }
    }
}
