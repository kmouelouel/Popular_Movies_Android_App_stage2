package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.MainActivity;
import com.example.android.popularmoviesstage1.R;

public class UserInterfaceViews {
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private GridView mGridView;
    private Context mContext;

    public UserInterfaceViews(Context context){
        mContext = context;
        mLoadingIndicator = (ProgressBar) ((MainActivity) mContext).findViewById(R.id.pb_Loading_indicator);
        mErrorMessageDisplay = (TextView) ((MainActivity) mContext).findViewById(R.id.tv_error_message_display);
        mGridView = (GridView) ((MainActivity) mContext).findViewById(R.id.gridview);
    }

    public void setLoadingToVisible(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public void setLoadingToInvisible(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }
    public void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mGridView.setVisibility(View.VISIBLE);

    }

public void showNoData(String text)
{
    mErrorMessageDisplay.setVisibility(View.VISIBLE);
    mErrorMessageDisplay.setText(text);
    mGridView.setVisibility(View.INVISIBLE);
}
    public void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.INVISIBLE);

    }
}