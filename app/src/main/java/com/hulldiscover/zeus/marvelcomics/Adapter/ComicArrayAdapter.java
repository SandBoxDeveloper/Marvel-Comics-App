/*
 * Copyright (C) 2016 Anupam Das
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hulldiscover.zeus.marvelcomics.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hulldiscover.zeus.marvelcomics.R;
import com.hulldiscover.zeus.marvelcomics.UI.BrowseComicsActivityFragment;


public class ComicArrayAdapter extends CursorAdapter {
    private static final String LOG_TAG = ComicArrayAdapter.class.getSimpleName();


    public ComicArrayAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /*
       This is where we fill-in the views with the contents of the cursor.
    */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        final String url = cursor.getString(BrowseComicsActivityFragment.COL_IMAGE);
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .into(viewHolder.imageView);
        viewHolder.imageView.setAdjustViewBounds(true);


        String date = cursor.getString(BrowseComicsActivityFragment.COL_DATE);
        int pos = date.indexOf('-');
        viewHolder.year.setText(date.substring(0, pos >= 0 ? pos : 0));

        String rating = cursor.getString(BrowseComicsActivityFragment.COL_PAGE_COUNT);
        double vote = Double.parseDouble(rating);
        rating = String.valueOf((double) Math.round(vote * 10d) / 10d);

        viewHolder.userRating.setText(rating + "/10");

        String popularity = cursor.getString(BrowseComicsActivityFragment.COL_DESCRIPTION);
        //pos = popularity.indexOf(".");
        //viewHolder.pop_text.setText(popularity.substring(0, pos >= 0 ? pos : 0));
        viewHolder.pop_text.setText(popularity);
    }

    public static class ViewHolder {

        final ImageView imageView;
        final TextView year;
        final TextView userRating;
        final TextView pop_text;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.grid_item_poster);
            year = (TextView) view.findViewById(R.id.year);
            userRating = (TextView) view.findViewById(R.id.vote_text);
            pop_text = (TextView) view.findViewById(R.id.pop_text);
        }
    }
}