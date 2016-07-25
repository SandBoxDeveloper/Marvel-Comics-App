package com.hulldiscover.zeus.marvelcomics.Data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the comic database.
 */
public class ComicContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.hulldiscover.zeus.marvelcomics";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, com.hulldiscover.zeus.marvelcomics/comic is a valid path for
    // looking at comic data. content://com.hulldiscover.zeus.marvelcomics/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    public static final String PATH_COMIC = "comic";

    /* Inner class that defines the table contents of the comic table */
    public static final class ComicEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMIC).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMIC;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMIC;

        // Table Name
        public static final String TABLE_NAME = "comic";

        // Comic id returned by the API, to identify the comic
        public static final String COLUMN_COMIC_ID = "comic_id";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "image";
        // Short description and long description of the comic, as provided by API.
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_PAGE_COUNT = "page_count";
        public static final String COLUMN_AUTHOR = "author";
        // Date, stored as long in milliseconds
        public static final String COLUMN_DATE = "date";


        public static Uri buildComicUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildComic(String comic) {
            return CONTENT_URI.buildUpon().appendPath(comic).build();
        }



    }
}
