package com.hulldiscover.zeus.marvelcomics.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hulldiscover.zeus.marvelcomics.DetailActivityFragment;
import com.hulldiscover.zeus.marvelcomics.R;

/**
 * Created by Zeus on 16/07/16.
 */
public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.DETAIL_MOVIE,
                    getIntent().getParcelableExtra(DetailActivityFragment.DETAIL_MOVIE));

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
}