package com.hulldiscover.zeus.marvelcomics.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hulldiscover.zeus.marvelcomics.UI.DetailActivityFragment;
import com.hulldiscover.zeus.marvelcomics.Model.Comic;
import com.hulldiscover.zeus.marvelcomics.R;
import com.hulldiscover.zeus.marvelcomics.Sync.SyncAdapter;
import com.hulldiscover.zeus.marvelcomics.UI.BrowseComicsActivityFragment;

public class BrowseComicsActivity extends AppCompatActivity implements BrowseComicsActivityFragment.Callback {

    private static final String COMIC_DETAILS_FRAGMENT_TAG = "fragment_comic_details";
    private boolean mTwoPane;

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_comics);

        if (findViewById(R.id.comic_details_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.comic_details_container, new DetailActivityFragment(),
                                DetailActivityFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        // init Sync Adapter
        SyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public void onItemSelected(Comic movie) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.DETAIL_COMIC, movie);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            //fragment_movies
            // movie_details_container
            getSupportFragmentManager().beginTransaction()
                    //.replace(R.id.fragment_movies, fragment, DetailActivityFragment.TAG)
                    .replace(R.id.comic_details_container, fragment, COMIC_DETAILS_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailActivityFragment.DETAIL_COMIC, movie);
            startActivity(intent);
        }
    }
}
