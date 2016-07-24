package com.hulldiscover.zeus.marvelcomics.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.hulldiscover.zeus.marvelcomics.Data.ComicContract;
import com.hulldiscover.zeus.marvelcomics.UI.BrowseComicsActivityFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ismael on 10/06/2015.
 */
public class Comic implements Parcelable {


    private int id;
    private String title; // title
    private String image_poster; // image poster
    private String image_cover; // image cover
    private String image_poster_extension; // poster extension
    private String image_cover_extension; // cover extension
    private String author; // name
    private String description; // description
    private int page_count; // pageCount
    private String date; // dates
    private String price;
    Map<String, String> creators; // creators

    // These are the names of the JSON objects that need to be extracted.
    final String COMIC_ID = "id";
    final String TITLE = "title";
    final String DESCRIPTION = "description";
    final String PAGE_COUNT = "pageCount";
    final String DATES = "dates";
    final String TYPE = "type";
    final String DATE = "date";
    final String PRICES = "prices";
    final String PRICE = "price";
    final String THUMBNAIL = "thumbnail";
    final String IMAGE = "images";
    final String PATH = "path";
    final String EXTENSION = "extension";
    final String CREATORS = "creators";
    final String ITEMS = "items";
    final String NAME = "name";
    final String ROLE = "role";



    public Comic() {

    }

    public Comic(JSONObject comic) throws JSONException {



        this.id = comic.getInt(COMIC_ID);
        this.title = comic.getString(TITLE);
        this.description = comic.getString(DESCRIPTION);
        this.page_count = comic.optInt(PAGE_COUNT);

        JSONArray dates_array = comic.getJSONArray(DATES);
        for(int i = 0; i < 1; i++) {
            JSONObject obj = dates_array.getJSONObject(i);
            String type = obj.optString(TYPE);
            String date = obj.optString(DATE);
            this.date = date;
        }

        JSONArray prices_array = comic.getJSONArray(PRICES);
        for(int i = 0; i < prices_array.length(); i++) {
            JSONObject obj = prices_array.getJSONObject(i);
            this.price = obj.optString(PRICE);
        }


        JSONArray imgs_array = comic.getJSONArray(IMAGE);
        for(int i = 0; i < imgs_array.length(); i++) {
            JSONObject obj = imgs_array.getJSONObject(i);
            String img = obj.getString(PATH);
            String extension = obj.getString(EXTENSION);
            this.image_poster = img;
            this.image_poster_extension = extension;
        }

        JSONObject img = comic.getJSONObject(THUMBNAIL);
        this.image_cover = img.optString(PATH);
        this.image_cover_extension = img.getString(EXTENSION);

        JSONObject creators_object = comic.getJSONObject(CREATORS);
        JSONArray creators_array = creators_object.getJSONArray(ITEMS);
        for(int i = 0; i < creators_array.length(); i++) {
            // setup array depending on the
            // number of creators
            creators = new HashMap<>(creators_array.length());

            JSONObject obj = creators_array.getJSONObject(i);

            String name = obj.getString(NAME);
            String role = obj.getString(ROLE);
            creators.put(name, role);
        }

        ContentValues comicValues = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.

        comicValues.put(ComicContract.ComicEntry.COLUMN_COMIC_ID, id);
        comicValues.put(ComicContract.ComicEntry.COLUMN_TITLE, title);
        comicValues.put(ComicContract.ComicEntry.COLUMN_DESCRIPTION, description);
        comicValues.put(ComicContract.ComicEntry.COLUMN_PAGE_COUNT, page_count);
        comicValues.put(ComicContract.ComicEntry.COLUMN_DATE, date);
        comicValues.put(ComicContract.ComicEntry.COLUMN_PRICE, price);
        comicValues.put(ComicContract.ComicEntry.COLUMN_AUTHOR, author);
        //cVVector.add(comicValues);
    }
    int inserted = 0;
    // add to database
    /*if (cVVector.size() > 0) {
        ContentValues[] cvArray = new ContentValues[cVVector.size()];
        cVVector.toArray(cvArray);
        inserted = mContext.getContentResolver().bulkInsert(ComicContract.Movies.CONTENT_URI, cvArray);
    }*/

    //}



    public Comic(Cursor cursor) {
        this.id = cursor.getInt(BrowseComicsActivityFragment.COL_COMIC_ID);
        this.title = cursor.getString(BrowseComicsActivityFragment.COL_TITLE);
        this.image_cover = cursor.getString(BrowseComicsActivityFragment.COL_IMAGE);
        this.author = cursor.getString(BrowseComicsActivityFragment.COL_AUTHOR);
        this.description = cursor.getString(BrowseComicsActivityFragment.COL_DESCRIPTION);
        this.page_count = cursor.getInt(BrowseComicsActivityFragment.COL_PAGE_COUNT);
        this.date = cursor.getString(BrowseComicsActivityFragment.COL_DATE);
        this.price = cursor.getString(BrowseComicsActivityFragment.COL_PRICE);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPage_count() {
        return page_count;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public String getImage_poster() {
        return image_poster;
    }

    public String getImage_cover() {
        return image_cover;
    }

    public String getImage_poster_extension() {
        return image_poster_extension;
    }

    public String getImage_cover_extension() {
        return image_cover_extension;
    }

    public Map<String, String> getAuthor() {
        return creators;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(image_cover);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeInt(page_count);
        dest.writeString(date);
        dest.writeString(price);
    }

    public static final Parcelable.Creator<Comic> CREATOR
            = new Parcelable.Creator<Comic>() {
        public Comic createFromParcel(Parcel in) {
            return new Comic(in);
        }

        public Comic[] newArray(int size) {
            return new Comic[size];
        }
    };

    private Comic(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image_cover = in.readString();
        author = in.readString();
        description = in.readString();
        page_count = in.readInt();
        date = in.readString();
        price = in.readString();
    }
}
