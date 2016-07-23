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

    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";

    private Comic mComic;

    private ImageView mImageView;
    private ImageView mImageViewPoster;

    private TextView mTitleView;
    private TextView mDescription;
    private TextView mDateView;
    private TextView mPageCount;
    private TextView mPrice;
    private TextView mAuthors;

    private CardView mReviewsCardview;
    private CardView mTrailersCardview;

    //private ScrollView mDetailLayout;
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
            mComic = arguments.getParcelable(DetailActivityFragment.DETAIL_MOVIE);
        }

        //View rootView = inflater.inflate(R.layout.activity_detail, container, false);
        //View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        //View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        //mDetailLayout = (ScrollView) rootView.findViewById(R.id.detail_layout);
        mDetailLayout = (ObservableScrollView) rootView.findViewById(R.id.movie_scroll_view);

        if (mComic != null) {
            mDetailLayout.setVisibility(View.VISIBLE);
        } else {
            mDetailLayout.setVisibility(View.INVISIBLE);
        }

        /*mImageView = (ImageView) rootView.findViewById(R.id.detail_image);

        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mDescription = (TextView) rootView.findViewById(R.id.detail_description);
        mDateView = (TextView) rootView.findViewById(R.id.detail_date);*/

        mImageView = (ImageView) rootView.findViewById(R.id.movie_cover);
        mImageViewPoster = (ImageView) rootView.findViewById(R.id.movie_poster);

        mTitleView = (TextView) rootView.findViewById(R.id.comic_title);
        mDescription = (TextView) rootView.findViewById(R.id.comic_description);
        mDateView = (TextView) rootView.findViewById(R.id.comic_release_date);
        mPageCount = (TextView) rootView.findViewById(R.id.movie_average_rating);
        mPrice = (TextView) rootView.findViewById(R.id.comic_price);
        mAuthors = (TextView) rootView.findViewById(R.id.comic_authors);


        if (mComic != null) {

            String image_url_cover = mComic.getImage_cover() + "/portrait_xlarge.jpg";
            String image_url_poster = mComic.getImage_poster() + "/portrait_xlarge.jpg";

            Glide.with(this).load(image_url_cover).into(mImageView);
            Glide.with(this).load(image_url_poster).into(mImageViewPoster);

            mTitleView.setText(mComic.getTitle());
            mDescription.setText("Description: \n\n" +mComic.getDescription());
            mPageCount.setText("Pages: " +mComic.getPage_count());
            mPrice.setText("Price: " +mComic.getPrice());
            mAuthors.setText("Authors: " +mComic.getAuthor());

            String movie_date = mComic.getDate();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String date = DateUtils.formatDateTime(getActivity(),
                        formatter.parse(movie_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                mDateView.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }


        return rootView;
    }







}

