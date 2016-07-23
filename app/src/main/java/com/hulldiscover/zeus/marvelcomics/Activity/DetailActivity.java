package com.hulldiscover.zeus.marvelcomics.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hulldiscover.zeus.marvelcomics.DetailActivityFragment;
import com.hulldiscover.zeus.marvelcomics.R;

/**
 * Created by Zeus on 16/07/16.
 */
public class DetailActivity extends AppCompatActivity {

    private static final String MOVIE_FRAGMENT_TAG = "fragment_comic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail);
        setContentView(R.layout.activity_movie_details);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.DETAIL_COMIC,
                    getIntent().getParcelableExtra(DetailActivityFragment.DETAIL_COMIC));

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.comic_details_container, fragment, MOVIE_FRAGMENT_TAG)
                    .commit();


        }
    }
}
