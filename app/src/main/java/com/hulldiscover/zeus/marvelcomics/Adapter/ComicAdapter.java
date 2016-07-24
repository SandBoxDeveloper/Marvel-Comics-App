package com.hulldiscover.zeus.marvelcomics.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.hulldiscover.zeus.marvelcomics.Data.ComicContract;
import com.hulldiscover.zeus.marvelcomics.R;


public class ComicAdapter extends CursorAdapter {
    /**
     * {@link ComicAdapter} exposes a list of weather forecasts
     * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
     */

    public ComicAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
        int idx_title = cursor.getColumnIndex(ComicContract.ComicEntry.COLUMN_TITLE);
        int idx_price = cursor.getColumnIndex(ComicContract.ComicEntry.COLUMN_PRICE);
        int idx_author = cursor.getColumnIndex(ComicContract.ComicEntry.COLUMN_AUTHOR);
        int idx_desc = cursor.getColumnIndex(ComicContract.ComicEntry.COLUMN_DESCRIPTION);


        return cursor.getString(idx_title) +
                " - " + cursor.getDouble(idx_price) +
                " - " + cursor.getDouble(idx_author) +
                " - " + cursor.getString(idx_desc);
    }

    /*
        These views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_comic, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view;
        tv.setText(convertCursorRowToUXFormat(cursor));
    }
}
