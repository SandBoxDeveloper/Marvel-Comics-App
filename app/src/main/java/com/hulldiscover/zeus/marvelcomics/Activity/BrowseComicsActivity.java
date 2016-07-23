package com.hulldiscover.zeus.marvelcomics.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hulldiscover.zeus.marvelcomics.DetailActivityFragment;
import com.hulldiscover.zeus.marvelcomics.Model.Comic;
import com.hulldiscover.zeus.marvelcomics.R;
import com.hulldiscover.zeus.marvelcomics.UI.BrowseComicsActivityFragment;

public class BrowseComicsActivity extends AppCompatActivity implements BrowseComicsActivityFragment.Callback {

    private static final String MOVIE_DETAILS_FRAGMENT_TAG = "fragment_movie_details";
    private boolean mTwoPane;

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_comics);

        if (findViewById(R.id.movie_details_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, new DetailActivityFragment(),
                                DetailActivityFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onItemSelected(Comic movie) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.DETAIL_MOVIE, movie);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            //fragment_movies
            // movie_details_container
            getSupportFragmentManager().beginTransaction()
                    //.replace(R.id.fragment_movies, fragment, DetailActivityFragment.TAG)
                    .replace(R.id.movie_details_container, fragment, MOVIE_DETAILS_FRAGMENT_TAG)
                    .commit();
        } else {
            //TODO: DetailActivity
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailActivityFragment.DETAIL_MOVIE, movie);
            startActivity(intent);
        }
    }
}
