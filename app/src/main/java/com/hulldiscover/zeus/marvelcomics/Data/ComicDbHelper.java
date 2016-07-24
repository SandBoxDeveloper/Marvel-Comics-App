package com.hulldiscover.zeus.marvelcomics.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.hulldiscover.zeus.marvelcomics.Data.ComicContract.ComicEntry;
/**
 * Manages a local database for comic data.
 */
public class ComicDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    // The name of the actual database file in the file system
    public static final String DATABASE_NAME = "comic.db";

    public ComicDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method to create new SQLite database
     * @param db SQLite database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold comics.
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + ComicEntry.TABLE_NAME + " (" +
                ComicEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ComicEntry.COLUMN_COMIC_ID + " INTEGER NOT NULL, " +
                ComicEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ComicEntry.COLUMN_IMAGE + " TEXT, " +
                ComicEntry.COLUMN_DESCRIPTION + " TEXT, " +
                ComicEntry.COLUMN_AUTHOR + " TEXT, " +
                ComicEntry.COLUMN_PRICE + " INTEGER, " +
                ComicEntry.COLUMN_PAGE_COUNT + " INTEGER, " +
                ComicEntry.COLUMN_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    /**
     * Method to upgrade SQLite database.
     *
     * This gets called when the database has already been created
     * but the version has changed.
     *
     * @param db SQLite database
     * @param oldVersion current version of SQLite database
     * @param newVersion new desired version of SQLite database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note - this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next line
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + ComicEntry.TABLE_NAME);
        onCreate(db);
    }
}
