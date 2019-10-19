package com.digitalent.submission.movieboard.page;


import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.digitalent.submission.movieboard.R;
import com.digitalent.submission.movieboard.adapter.ListFavoriteAdapter;
import com.digitalent.submission.movieboard.model.Favorite;
import com.digitalent.submission.movieboard.util.FavoriteLoader;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, FavoriteLoader.LoaderCallback {
    private static final String STATE_FAVORITES = "STATE_FAVORITES";

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeLayout;
    private ListFavoriteAdapter listAdapter;
    private Boolean pullRefresh = false;
    private HandlerThread handlerThread;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Resources resources = view.getResources();

        progressBar = view.findViewById(R.id.progress_movie);
        swipeLayout = view.findViewById(R.id.swipe_movie);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(
                resources.getColor(android.R.color.holo_green_dark),
                resources.getColor(android.R.color.holo_red_dark),
                resources.getColor(android.R.color.holo_blue_dark),
                resources.getColor(android.R.color.holo_orange_dark)
        );

        listAdapter = new ListFavoriteAdapter(getContext());
        listAdapter.setFavorites(new ArrayList<Favorite>());
        listAdapter.notifyDataSetChanged();

        RecyclerView recyclerView = view.findViewById(R.id.rv_movie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);

        if (getContext() != null) {
            handlerThread = new HandlerThread("MovieObserver");
            handlerThread.start();

            MovieObserver movieObserver = new MovieObserver(
                    new Handler(handlerThread.getLooper()),
                    getContext(),
                    this);

            getContext().getContentResolver().registerContentObserver(FavoriteLoader.CONTENT_URI, true, movieObserver);
        }

        if (savedInstanceState == null) {
            loadFavorites();
        } else {
            ArrayList<Favorite> favorites = savedInstanceState.getParcelableArrayList(STATE_FAVORITES);
            if (favorites != null) {
                listAdapter.setFavorites(favorites);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_FAVORITES, listAdapter.getFavorites());
    }

    @Override
    public void onDestroyView() {
        if (handlerThread != null) {
            handlerThread.quit();
        }
        super.onDestroyView();
    }

    private void loadFavorites() {
        new FavoriteLoader(getContext(), this).execute("movie");
    }

    private void showLoading(Boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onRefresh() {
        pullRefresh = true;
        loadFavorites();
    }

    @Override
    public void preExecute() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!pullRefresh) {
                        showLoading(true);
                    }
                }
            });
        }
    }

    @Override
    public void postExecute(ArrayList<Favorite> favorites) {
        showLoading(false);
        swipeLayout.setRefreshing(false);
        pullRefresh = false;

        if (favorites.size() > 0) {
            listAdapter.setFavorites(favorites);
        } else {
            listAdapter.setFavorites(new ArrayList<Favorite>());
            showMessage(getString(R.string.nodata));
        }
    }

    private void showMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private static class MovieObserver extends ContentObserver {

        private final Context context;
        private final FavoriteLoader.LoaderCallback callback;

        private MovieObserver(Handler handler, Context context, FavoriteLoader.LoaderCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new FavoriteLoader(context, callback).execute("movie");
        }
    }
}
