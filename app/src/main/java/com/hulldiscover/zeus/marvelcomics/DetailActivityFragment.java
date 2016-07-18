package com.hulldiscover.zeus.marvelcomics;

import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

    private TextView mTitleView;
    private TextView mDescription;
    private TextView mDateView;

    private ScrollView mDetailLayout;

    private Toast mToast;

    private ShareActionProvider mShareActionProvider;


    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mComic = arguments.getParcelable(DetailActivityFragment.DETAIL_MOVIE);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mDetailLayout = (ScrollView) rootView.findViewById(R.id.detail_layout);

        if (mComic != null) {
            mDetailLayout.setVisibility(View.VISIBLE);
        } else {
            mDetailLayout.setVisibility(View.INVISIBLE);
        }

        mImageView = (ImageView) rootView.findViewById(R.id.detail_image);

        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mDescription = (TextView) rootView.findViewById(R.id.detail_description);
        mDateView = (TextView) rootView.findViewById(R.id.detail_date);


        if (mComic != null) {

            String image_url = mComic.getImage() + "/portrait_xlarge.jpg";

            Glide.with(this).load(image_url).into(mImageView);

            mTitleView.setText(mComic.getTitle());
            mDescription.setText(mComic.getDescription());

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

