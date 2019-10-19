package com.digitalent.submission.movieboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.digitalent.submission.movieboard.adapter.TabAdapter;
import com.digitalent.submission.movieboard.page.MovieFragment;
import com.digitalent.submission.movieboard.page.TvFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
    }

    private void setupView() {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new MovieFragment(), getString(R.string.movie_title));
        tabAdapter.addFragment(new TvFragment(), getString(R.string.tv_title));

        ViewPager viewPager = findViewById(R.id.main_view_pager);
        viewPager.setAdapter(tabAdapter);

        TabLayout tabLayout = findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
