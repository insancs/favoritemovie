package com.sanitcode.favoritemovie;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.sanitcode.favoritemovie.adapter.MovieAdapter;
import com.sanitcode.favoritemovie.api.ApiCall;
import com.sanitcode.favoritemovie.api.ApiClient;
import com.sanitcode.favoritemovie.data.local.FavoriteColumn;
import com.sanitcode.favoritemovie.data.model.MovieFavorite;
import com.sanitcode.favoritemovie.data.model.ResultItems;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sanitcode.favoritemovie.BuildConfig.APIKEY;
import static com.sanitcode.favoritemovie.data.local.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.recycler_favorite)
    RecyclerView recyclerView;

    Call<ResultItems> movieResultCall;
    MovieAdapter movieAdapter;
    ArrayList<ResultItems> items;
    ArrayList<MovieFavorite> movieFavorites;
    ApiCall apiCall;

    private final int MOVIE_ID = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        movieAdapter = new MovieAdapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getSupportLoaderManager().initLoader(MOVIE_ID, null, this);
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        movieFavorites = new ArrayList<>();
        items = new ArrayList<>();
        return new CursorLoader(this, CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        movieFavorites = getItem(data);
        for (MovieFavorite m : movieFavorites) {
            getFavoriteMovies(m.getId());
            movieAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
        movieFavorites = getItem(null);
    }

    private ArrayList<MovieFavorite> getItem(Cursor cursor) {
        ArrayList<MovieFavorite> movieFavoriteArrayList = new ArrayList<>();
        cursor.moveToFirst();
        MovieFavorite favorite;
        if (cursor.getCount() > 0) {
            do {
                favorite = new MovieFavorite(cursor.getString(cursor.getColumnIndexOrThrow(
                        FavoriteColumn.FAVORITE_ID)));
                movieFavoriteArrayList.add(favorite);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        return movieFavoriteArrayList;
    }

    private void getFavoriteMovies(String id) {
        apiCall = ApiClient.getClient().create(ApiCall.class);
        movieResultCall = apiCall.getMovieById(id, APIKEY);

        movieResultCall.enqueue(new Callback<ResultItems>() {
            @Override
            public void onResponse(@NonNull Call<ResultItems> call, @NonNull Response<ResultItems> response) {
                items.add(response.body());
                movieAdapter.setMovieResult(items);
                recyclerView.setAdapter(movieAdapter);
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ResultItems> call, @NonNull Throwable t) {
                items = null;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (items != null) {
            items.clear();
            movieAdapter.setMovieResult(items);
            recyclerView.setAdapter(movieAdapter);
        }
        getSupportLoaderManager().restartLoader(MOVIE_ID, null, this);
    }
}
