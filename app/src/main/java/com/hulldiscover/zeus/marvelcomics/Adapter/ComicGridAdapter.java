package com.hulldiscover.zeus.marvelcomics.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hulldiscover.zeus.marvelcomics.Model.Comic;
import com.hulldiscover.zeus.marvelcomics.R;

import java.util.List;

/**
 * Created by Ismael on 10/06/2015.
 */
public class ComicGridAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private final Comic mLock = new Comic();

    private List<Comic> mObjects;

    public ComicGridAdapter(Context context, List<Comic> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public void add(Comic object) {
        synchronized (mLock) {
            mObjects.add(object);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            mObjects.clear();
        }
        notifyDataSetChanged();
    }

    public void setData(List<Comic> data) {
        clear();
        for (Comic comic : data) {
            add(comic);
        }
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Comic getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_movie, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Comic comic = getItem(position);

        String image_url = comic.getImage_cover() + "/portrait_xlarge." + comic.getImage_cover_extension();

        viewHolder = (ViewHolder) view.getTag();

        Glide.with(getContext()).load(image_url).into(viewHolder.imageView);
        viewHolder.titleView.setText(comic.getTitle());
        //viewHolder.comicPrice.setText(comic.getPrice());
        //viewHolder.pageCount.setText(comic.getPage_count());

        return view;
    }

    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView titleView;
        public final TextView comicPrice;
        public final TextView pageCount;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.grid_item_poster);
            titleView = (TextView) view.findViewById(R.id.year);
            comicPrice = (TextView) view.findViewById(R.id.vote_text);
            pageCount = (TextView) view.findViewById(R.id.pop_text);
        }
    }
}
