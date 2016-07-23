package com.hulldiscover.zeus.marvelcomics;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.hulldiscover.zeus.marvelcomics.Model.Comic;

import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends android.support.v4.app.Fragment {

    public static final String TAG = DetailActivityFragment.class.getSimpleName();

    public static final String DETAIL_COMIC = "DETAIL_COMIC";

    private Comic mComic;

    private ImageView mImageViewCover;
    private ImageView mImageViewPoster;

    private TextView mComicTitle;
    private TextView mDescription;
    private TextView mReleaseDate;
    private TextView mPageCount;
    private TextView mPrice;
    private TextView mCreators;

    private CardView mReviewsCardview;

    private ObservableScrollView mDetailLayout;

    private Toast mToast;

    private ShareActionProvider mShareActionProvider;


    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fragments menu items will be displayed in the ActionBar.
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mComic = arguments.getParcelable(DetailActivityFragment.DETAIL_COMIC);
        }

        View rootView = inflater.inflate(R.layout.fragment_comic, container, false);
        mDetailLayout = (ObservableScrollView) rootView.findViewById(R.id.movie_scroll_view);

        if (mComic != null) {
            mDetailLayout.setVisibility(View.VISIBLE);
        } else {
            mDetailLayout.setVisibility(View.INVISIBLE);
        }

        mImageViewPoster = (ImageView) rootView.findViewById(R.id.img_comic_poster);
        mImageViewCover = (ImageView) rootView.findViewById(R.id.comic_thumbnail);
        mComicTitle = (TextView) rootView.findViewById(R.id.comic_title);
        mDescription = (TextView) rootView.findViewById(R.id.comic_description);
        mReleaseDate = (TextView) rootView.findViewById(R.id.comic_release_date);
        mPageCount = (TextView) rootView.findViewById(R.id.comic_no_pages);
        mPrice = (TextView) rootView.findViewById(R.id.comic_price);
        mCreators = (TextView) rootView.findViewById(R.id.comic_creators);

        if (mComic != null) {

            // comic image
            String image_url_cover = mComic.getImage_cover() + "/portrait_xlarge.jpg";
            String image_url_poster = mComic.getImage_poster() + "/portrait_xlarge.jpg";
            Glide.with(this).load(image_url_cover).into(mImageViewPoster);
            Glide.with(this).load(image_url_cover).into(mImageViewCover);

            // title
            mComicTitle.setText(mComic.getTitle());

            // date
            String release_date = mComic.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String date = DateUtils.formatDateTime(getActivity(),
                        formatter.parse(release_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                mReleaseDate.setText(getString(R.string.release_date) + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // pages
            mPageCount.setText(getString(R.string.pages) + mComic.getPage_count());

            // price
            mPrice.setText(getString(R.string.price) + mComic.getPrice());

            // creators
            /*String key = mComic.getAuthor().toString();
            String value = "";
            for (Map.Entry<String,String> entry : mComic.getAuthor().entrySet()) {
                key = entry.getKey();
                value = entry.getValue();

            }*/
            mCreators.setText("Authors: " +mComic.getAuthor());

            // description
            mDescription.setText(getString(R.string.description) + "\n\n" +mComic.getDescription());

        }
        return rootView;
    }







}

