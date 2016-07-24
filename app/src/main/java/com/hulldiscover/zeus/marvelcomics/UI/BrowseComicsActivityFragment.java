package com.hulldiscover.zeus.marvelcomics.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hulldiscover.zeus.marvelcomics.Activity.AboutActivity;
import com.hulldiscover.zeus.marvelcomics.Adapter.ComicGridAdapter;
import com.hulldiscover.zeus.marvelcomics.BuildConfig;
import com.hulldiscover.zeus.marvelcomics.Data.ComicContract;
import com.hulldiscover.zeus.marvelcomics.Model.Comic;
import com.hulldiscover.zeus.marvelcomics.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class BrowseComicsActivityFragment extends Fragment {

    private GridView mGridView;

    private ComicGridAdapter mComicGridAdapter;

    private static final String STATE_MODE = "state_mode";

    private static final String SORT_SETTING_KEY = "sort_setting";
    private static final String TITLE_ASC = "title";
    private static final String ON_SALE_DATE = "onsaleDate";
    private static final String ISSUE_ASC = "issueNumber";
    private static final String MODIFIED_ASC = "modified";
    private static final String MOVIES_KEY = "movies";

    private String mSortBy = TITLE_ASC; // default sort order

    private ArrayList<Comic> mComics = null;

    private static final String[] COMIC_COLUMNS = {
            ComicContract.ComicEntry._ID,
            ComicContract.ComicEntry.COLUMN_TITLE,
            ComicContract.ComicEntry.COLUMN_IMAGE,
            ComicContract.ComicEntry.COLUMN_DESCRIPTION,
            ComicContract.ComicEntry.COLUMN_AUTHOR,
            ComicContract.ComicEntry.COLUMN_PRICE,
            ComicContract.ComicEntry.COLUMN_PAGE_COUNT,
            ComicContract.ComicEntry.COLUMN_DATE
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    public static final int COL_COMIC_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_IMAGE = 2;
    public static final int COL_DESCRIPTION = 3;
    public static final int COL_AUTHOR = 4;
    public static final int COL_PRICE = 5;
    public static final int COL_PAGE_COUNT = 6;
    public static final int COL_DATE = 7;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * for when an item has been selected.
         */
        public void onItemSelected(Comic comic);
    }

    public BrowseComicsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This line is added in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_fragment_main, menu);
        inflater.inflate(R.menu.activity_browse_comics, menu);

        MenuItem action_order_by_title = menu.findItem(R.id.action_oder_by_title);
        MenuItem action_order_by_onsaleDate = menu.findItem(R.id.action_oder_by_onsaleDate);
        //MenuItem action_order_by_issueNumber = menu.findItem(R.id.action_oder_by_issueNumber);

        MenuItem menu_order_by_title = menu.findItem(R.id.menu_sort_title);
        MenuItem menu_order_by_onSaleDate = menu.findItem(R.id.menu_sort_saleDate);
        MenuItem menu_order_by_issueNumber = menu.findItem(R.id.menu_sort_issueNumber);
        MenuItem menu_order_by_modified = menu.findItem(R.id.menu_sort_modified);

        if (mSortBy.contentEquals(TITLE_ASC)) {
            if (!menu_order_by_title.isChecked()) {
                menu_order_by_title.setChecked(true);
            }
        } else if (mSortBy.contentEquals(ON_SALE_DATE)) {
            if (!menu_order_by_onSaleDate.isChecked()) {
                menu_order_by_onSaleDate.setChecked(true);
            }
        } else if (mSortBy.contentEquals(ISSUE_ASC)) {
            if (!menu_order_by_issueNumber.isChecked()) {
                menu_order_by_issueNumber.setChecked(true);
            }
        } else if (mSortBy.contentEquals(MODIFIED_ASC)) {
            if (!menu_order_by_modified.isChecked()) {
                menu_order_by_modified.setChecked(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_scroll_to_top:
                scrollToTop(true);
                return true;

            case R.id.menu_about:
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_sort_title:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = TITLE_ASC;
                updateComics(mSortBy);
                return true;

            case R.id.menu_sort_saleDate:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = ON_SALE_DATE;
                updateComics(mSortBy);
                return true;

            case R.id.menu_sort_issueNumber:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = ISSUE_ASC;
                updateComics(mSortBy);
                return true;

            case R.id.menu_sort_modified:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = MODIFIED_ASC;
                updateComics(mSortBy);
                return true;

            /*case R.id.action_about:
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) view.findViewById(R.id.gridview_movies);

        // The ComicAdapter will take data from a source and
        // use it to populate the GridView it's attached to.
        mComicGridAdapter = new ComicGridAdapter(getActivity(), new ArrayList<Comic>());

        // Get a reference to the GridView, and attach this adapter to it.
        mGridView.setAdapter(mComicGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comic comic = mComicGridAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(comic);
            }
        });

        // TODO: Add Saved Instance
        // If there's instance state, mine it for useful information.
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_SETTING_KEY)) {
                mSortBy = savedInstanceState.getString(SORT_SETTING_KEY);
            }

            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                mComics = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                mComicGridAdapter.setData(mComics);
            } else {
                updateComics(mSortBy);
            }
        } else {
            updateComics(mSortBy);
        }

        return view;
    }

    public void scrollToTop(boolean smooth) {
        if (smooth)
            mGridView.smoothScrollToPosition(0);
        else
            mGridView.smoothScrollToPosition(0);
    }

    private void updateComics(String sort_by) {
        new FetchComicsTask().execute(sort_by);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!mSortBy.contentEquals(TITLE_ASC)) {
            outState.putString(SORT_SETTING_KEY, mSortBy);
        }
        if (mComics != null) {
            outState.putParcelableArrayList(MOVIES_KEY, mComics);
        }
        super.onSaveInstanceState(outState);
    }

    public class FetchComicsTask extends AsyncTask<String, Void, List<Comic>> {

        private final String LOG_TAG = FetchComicsTask.class.getSimpleName();

        final String RESULT = "results";
        final String DATA = "data";
        final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
        final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";

        private List<Comic> getComicsDataFromJson(String jsonStr) throws JSONException {
            JSONObject comicJson = new JSONObject(jsonStr);

            // get data JSONObject from API
            JSONObject json_data= comicJson.getJSONObject(DATA);

            // get results JSONArray from json_data
            JSONArray comicArray = json_data.getJSONArray(RESULT);

            List<Comic> results = new ArrayList<>();

            for(int i = 0; i < comicArray.length(); i++) {
                JSONObject comic = comicArray.getJSONObject(i);
                Comic comicModel = new Comic(comic);
                results.add(comicModel);
            }

            return results;
        }

        @Override
        protected List<Comic> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://gateway.marvel.com:80/v1/public/comics?";
                // http://gateway.marvel.com:80/v1/public/comics?format=comic&formatType=comic&limit=100&ts=1&apikey=54306733de0f5cd1418aa05a85fa062a&hash=359e14db6b6a7bed5c31d81b2c00f36b
                final String ORDER_BY_PARAM = "orderBy";
                final String LIMIT = "100";
                final String API_KEY_PARAM = BuildConfig.API_KEY;
                //"54306733de0f5cd1418aa05a85fa062a"
                final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
                final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("format", "comic")
                        .appendQueryParameter("formatType", "comic")
                        .appendQueryParameter(ORDER_BY_PARAM, params[0])
                        .appendQueryParameter("limit", LIMIT)
                        .appendQueryParameter("ts", "1")
                        .appendQueryParameter("apikey", API_KEY_PARAM)
                        .appendQueryParameter("hash", "359e14db6b6a7bed5c31d81b2c00f36b")
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getComicsDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<Comic> comics) {
            if (comics != null) {
                if (mComicGridAdapter != null) {
                    mComicGridAdapter.setData(comics);
                }
                mComics = new ArrayList<>();
                mComics.addAll(comics);
            }
        }
    }
}
