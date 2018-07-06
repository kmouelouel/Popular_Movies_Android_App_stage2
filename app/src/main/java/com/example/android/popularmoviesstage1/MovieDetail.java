package com.example.android.popularmoviesstage1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.data.MoviesContract;
import com.example.android.popularmoviesstage1.data.MoviesDbHelper;
import com.example.android.popularmoviesstage1.databinding.ActivityMovieDetailBinding;
import com.example.android.popularmoviesstage1.models.Movie;
import com.example.android.popularmoviesstage1.models.Review;
import com.example.android.popularmoviesstage1.models.Trailer;
import com.example.android.popularmoviesstage1.utilities.FetchReviewsAsyncTask;
import com.example.android.popularmoviesstage1.utilities.FetchTrailersTask;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MovieDetail extends AppCompatActivity {
    public static final String TAG = MovieDetail.class.getSimpleName();
    //Binding Data
    ActivityMovieDetailBinding mBinding;
    //added variables for stage 2
    private MoviesDbHelper moviesDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        //  Set the Content View using DataBindingUtil to the activity_movie_detail layout
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movieDetails")) {
                final Movie mMovie = (Movie) intentThatStartedThisActivity.getParcelableExtra("movieDetails");
                displayMovieInfo(mMovie);
                moviesDbHelper = new MoviesDbHelper(this);
                if (checkIfMovieIsFavorite(mMovie)) {
                    changeColorOfFavoriteIcon(true);
                }
                //add event cliclListener to
                mBinding.favoriteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkIfMovieIsFavorite(mMovie)) {
                            changeColorOfFavoriteIcon(true);
                            saveMovieInDatabase(mMovie);
                        } else {
                            changeColorOfFavoriteIcon(false);
                            delelteMovieFromDatabase(mMovie);
                        }

                    }
                });
            }
        }

    }  //end onCreate

    private void displayMovieInfo(Movie movie) {
        //  mTitleTextView.setText(mMovie.getTitle());
        mBinding.tvTitle.setText(movie.getTitle());
        mBinding.tvOverview.setText(movie.getOverview());
        try {
            Date releaseDate = new SimpleDateFormat("yyyy-mm-dd").parse(movie.getDate());
            SimpleDateFormat formatNowYear = new SimpleDateFormat("yyyy");
            String currentYear = formatNowYear.format(releaseDate);
            mBinding.tvReleaseDate.setText(currentYear);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "error to convert String date to Date ");
        }
        String image_url = NetworkUtils.buildPosterUrl(movie.getPoster());
        Picasso.with(getApplicationContext()).load(image_url).into(mBinding.imageDetailsPoster);
        String rating = String.valueOf(movie.getRating());
        mBinding.tvRating.setText(rating + " /10");
        //fetch the trailers by making a request to the /movie/{id}/videos endpoint.
        new FetchTrailersTask(String.valueOf(movie.getId())) {
            @Override
            protected void onPostExecute(List<Trailer> trailers) {
                super.onPostExecute(trailers);
                addTrailersToLayout(trailers);
            }
        }.execute();
        // fetch reviews by making a request to the /movie/{id}/reviews endpoint
        new FetchReviewsAsyncTask(String.valueOf(movie.getId())) {
            @Override
            protected void onPostExecute(List<Review> reviews) {
                super.onPostExecute(reviews);
                addReviewsToLayout(reviews);
            }

        }.execute();
    }// end displayMovieInfo

    private void addTrailersToLayout(List<Trailer> trailers) {
        if (trailers != null && !trailers.isEmpty()) {
            for (Trailer trailer : trailers) {
                if (trailer.getType().equals(getString(R.string.trailer_type)) &&
                        trailer.getSite().equals(getString(R.string.trailer_site_youtube))) {
                    View view = getTrailerView(trailer);
                    mBinding.layoutTrailersList.addView(view);
                }
            }
        } else {
            hideTrailersSection();
        }
    }//end addTrailerToLayout

    private void addReviewsToLayout(List<Review> reviews) {
        if (reviews != null && !reviews.isEmpty()) {
            for (Review review : reviews) {
                View view = getReviewView(review);
                mBinding.layoutReviewsList.addView(view);
            }
        } else {
            hideReviewsSection();
        }
    }//end addReviewsToLayout

    private void hideTrailersSection() {
        mBinding.layoutTrailersList.setVisibility(View.INVISIBLE);
        mBinding.textTrailerTitle.setVisibility(View.INVISIBLE);
        mBinding.viewBeforeTrailer.setVisibility(View.INVISIBLE);
    }//end hideTrailersSection

    private void hideReviewsSection() {
        mBinding.layoutReviewsList.setVisibility(View.INVISIBLE);
        mBinding.textReviewsTitle.setVisibility(View.INVISIBLE);
        mBinding.viewBeforeReview.setVisibility(View.INVISIBLE);
    }//end hideReviewsSection

    private View getTrailerView(final Trailer trailer) {
        LayoutInflater inflater = LayoutInflater.from(MovieDetail.this);
        LinearLayout linearLayoutTrailers = mBinding.layoutTrailersList;
        View view = inflater.inflate(R.layout.trailer_list_item, linearLayoutTrailers, false);
        TextView trailerNameTextView = view.findViewById(R.id.tv_trailer_item_name);
        trailerNameTextView.setText(trailer.getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(NetworkUtils.buildYouTubeUrl(trailer.getKey())));
                startActivity(intent);
            }
        });
        return view;
    }//end getTrailerView

    private View getReviewView(final Review review) {
        LayoutInflater inflater = LayoutInflater.from(MovieDetail.this);
        LinearLayout linearLayoutReviews = mBinding.layoutReviewsList;
        View view = inflater.inflate(R.layout.review_list_item, linearLayoutReviews, false);
        TextView reviewContentTextView = view.findViewById(R.id.tv_content_review);
        TextView reviewAuthorTextView = view.findViewById(R.id.tv_author_name_review);
        reviewContentTextView.setText(review.getContent());
        reviewAuthorTextView.setText(review.getAuthor());

        return view;
    }//end getReviewView

    //impliminte method the methodes that allow users to mark a movie as a favorite
    private boolean checkIfMovieIsFavorite(Movie movie) {
        Cursor cursor = getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int movieId = cursor.getInt(
                        cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID));
                if (movieId == movie.getId()) {
                    return true;
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }// end checkIfMovieIsFavorited method

    private void changeColorOfFavoriteIcon(boolean isFavorite) {
        if (isFavorite) {
            mBinding.favoriteImage.setImageResource(R.drawable.ic_favorite);
        } else {
            mBinding.favoriteImage.setImageResource(R.drawable.ic_unfavorite);
        }
    }

    private void saveMovieInDatabase(Movie movieIsFavorite) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movieIsFavorite.getId());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE, movieIsFavorite.getTitle());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_DESCRIPTION, movieIsFavorite.getOverview());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_PATH, movieIsFavorite.getPoster());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE, movieIsFavorite.getDate());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE, movieIsFavorite.getRating());
        getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);
    }

    private void delelteMovieFromDatabase(Movie movieIsUnFavorite) {
        String selection = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {String.valueOf(movieIsUnFavorite.getId())};
        getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI, selection, selectionArgs);

    }

}
