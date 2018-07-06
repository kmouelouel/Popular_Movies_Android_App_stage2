package com.example.android.popularmoviesstage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MoviesContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;
    public final static UriMatcher uriMatcher = buildUriMatcher();
    private MoviesDbHelper dbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case MOVIES:
                cursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIES:
                long id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);
                } else {
                    db.close();
                    throw new android.database.SQLException("Failed to insert row into database " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowDelete = 0;
        switch (match) {
            case MOVIES:
                rowDelete = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                db.close();
                throw new UnsupportedOperationException("impossible to delete this data from database");
        }
        if (rowDelete != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        db.close();
        return rowDelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
