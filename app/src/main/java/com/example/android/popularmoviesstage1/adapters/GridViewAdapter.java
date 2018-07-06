package com.example.android.popularmoviesstage1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.models.Movie;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<Movie> mMovies;

    public GridViewAdapter(Context context, List<Movie> movies) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMovies = movies;

    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Context getmContext() {
        return mContext;
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public List<Movie> getmMovies() {
        return mMovies;
    }

    public void setmMovies(List<Movie> mMovies) {
        this.mMovies = mMovies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.movie_list_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        final Movie movie = getItem(position);
        String image_url = NetworkUtils.buildPosterUrl(movie.getPoster());
        viewHolder = (ViewHolder) view.getTag();
        Picasso.with(getmContext()).load(image_url).into(viewHolder.imageView);

        return view;
    }

    public void add(Movie movie) {
        mMovies.add(movie);
        notifyDataSetChanged();
    }

    public void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    public void setData(List<Movie> data) {
        clear();
        for (Movie movie : data) {
            add(movie);
        }
    }

    public static class ViewHolder {
        public final ImageView imageView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.grid_item_image);
        }
    }


}
