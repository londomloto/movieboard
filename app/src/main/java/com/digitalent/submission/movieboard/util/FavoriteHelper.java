package com.digitalent.submission.movieboard.util;

import android.database.Cursor;

import com.digitalent.submission.movieboard.model.Favorite;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class FavoriteHelper {

    public static ArrayList<Favorite> cursorToList(Cursor cursor) {
        ArrayList<Favorite> list = new ArrayList<>();

        if (cursor != null) {
            while(cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow("release_date"));
                String overview = cursor.getString(cursor.getColumnIndexOrThrow("overview"));
                String poster = cursor.getString(cursor.getColumnIndexOrThrow("poster"));

                Favorite favorite = new Favorite(id, type, title, releaseDate, overview, poster);
                list.add(favorite);
            }
        }

        return list;
    }

}
