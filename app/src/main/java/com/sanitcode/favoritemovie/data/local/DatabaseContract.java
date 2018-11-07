package com.sanitcode.favoritemovie.data.local;

import android.net.Uri;
import static com.sanitcode.favoritemovie.data.local.FavoriteColumn.TABLE_MOVIE;

public class DatabaseContract {

    private static final String AUTHORITY = "com.sanitcode.favoritemovie";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();
}
