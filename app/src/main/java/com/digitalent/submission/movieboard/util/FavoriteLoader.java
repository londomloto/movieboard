package com.digitalent.submission.movieboard.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.digitalent.submission.movieboard.model.Favorite;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FavoriteLoader extends AsyncTask<String, Void, ArrayList<Favorite>> {
    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme("content")
            .authority("com.digitalent.submission.movieworld")
            .appendPath("favorite")
            .build();

    private final WeakReference<Context> weakContext;
    private final WeakReference<LoaderCallback> weakCallback;

    public FavoriteLoader(Context context, LoaderCallback callback) {
        weakContext = new WeakReference<>(context);
        weakCallback = new WeakReference<>(callback);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        weakCallback.get().preExecute();
    }

    @Override
    protected ArrayList<Favorite> doInBackground(String... types) {
        Uri uri = CONTENT_URI.buildUpon().clearQuery().appendQueryParameter("type", types[0]).build();

        Context context = weakContext.get();
        Cursor cursor = context.getContentResolver().query(uri,null, null, null, null);
        return FavoriteHelper.cursorToList(cursor);
    }

    @Override
    protected void onPostExecute(ArrayList<Favorite> favorites) {
        super.onPostExecute(favorites);
        weakCallback.get().postExecute(favorites);
    }

    public interface LoaderCallback {
        void preExecute();
        void postExecute(ArrayList<Favorite> favorites);
    }
}
