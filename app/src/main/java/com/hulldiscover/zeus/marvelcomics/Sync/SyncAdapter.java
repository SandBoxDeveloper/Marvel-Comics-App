package com.hulldiscover.zeus.marvelcomics.Sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;

import com.hulldiscover.zeus.marvelcomics.BuildConfig;
import com.hulldiscover.zeus.marvelcomics.Data.ComicContract;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Zeus on 24/07/16.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = SyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int COMIC_NOTIFICATION_ID = 3004;


    private static final String[] NOTIFY_COMIC_PROJECTION = new String[] {
            ComicContract.ComicEntry.COLUMN_COMIC_ID,
            ComicContract.ComicEntry.COLUMN_TITLE,
            ComicContract.ComicEntry.COLUMN_IMAGE,
            ComicContract.ComicEntry.COLUMN_DESCRIPTION,
            ComicContract.ComicEntry.COLUMN_AUTHOR,
            ComicContract.ComicEntry.COLUMN_PRICE,
            ComicContract.ComicEntry.COLUMN_PAGE_COUNT,
            ComicContract.ComicEntry.COLUMN_DATE,
    };

    // these indices must match the projection
    private static final int INDEX_COMIC_ID = 0;
    private static final int INDEX_TITLE = 1;
    private static final int INDEX_IMAGE = 2;
    private static final int INDEX_DESCRIPTION = 3;
    private static final int INDEX_AUTHOR = 4;
    private static final int INDEX_PRICE = 5;
    private static final int INDEX_PAGE_COUNT = 6;
    private static final int INDEX_DATE = 7;


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String comicJsonStr = null;

        final String BASE_URL = "http://gateway.marvel.com:80/v1/public/comics?";
        final String FORMAT = "comic";
        final String LIMIT = "100";
        final String TS = "1";
        final String HASH = "359e14db6b6a7bed5c31d81b2c00f36b";

        try {
            // Construct the URL for the Marvel query
            final String FORMAT_PARAM = "format";
            final String FORMAT_TYPE_PARAM = "formatType";
            final String ORDER_BY_PARAM = "orderBy";
            final String LIMIT_PARAM = "limit";
            final String TS_PARAM = "ts";
            final String API_KEY_PARAM = "apikey";
            final String HASH_PARAM = "hash";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(FORMAT_PARAM, FORMAT)
                    .appendQueryParameter(FORMAT_TYPE_PARAM, FORMAT)
                    .appendQueryParameter(LIMIT_PARAM, LIMIT)
                    .appendQueryParameter(TS_PARAM, TS)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                    .appendQueryParameter(HASH_PARAM, HASH)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to Marvel API, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
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
                // Stream was empty.  No point in parsing.
                return;
            }
            comicJsonStr = buffer.toString();
            getComicDataFromJson(comicJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the comic data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
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
        return;
    }

    /**
     * Take the String representing the complete comic in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private void getComicDataFromJson(String forecastJsonStr)
            throws JSONException {

        // Now we have a String representing the complete comic in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

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

        int comic_id;
        String title; // title
        String image_poster; // image poster
        String image_cover; // image cover
        String image_poster_extension; // poster extension
        String image_cover_extension; // cover extension
        String author =""; // name
        String description; // description
        int page_count; // pageCount
        String date =""; // dates
        String price ="";
        Map<String, String> creators; // creators


        // Comic information.  Each comic info is an element of the "results" array.
        final String RESULT = "results";
        final String DATA = "data";

        try {
            JSONObject comicJson = new JSONObject(forecastJsonStr);
            // get data JSONObject from API
            JSONObject json_data= comicJson.getJSONObject(DATA);
            // get results JSONArray from json_data
            JSONArray comicArray = json_data.getJSONArray(RESULT);


            comic_id = comicJson.getInt(COMIC_ID);
            Log.d(LOG_TAG, Integer.toString(comic_id));
            title = comicJson.getString(TITLE);
            description = comicJson.getString(DESCRIPTION);
            page_count = comicJson.optInt(PAGE_COUNT);

            JSONArray dates_array = comicJson.getJSONArray(DATES);
            for(int i = 0; i < 1; i++) {
                JSONObject obj = dates_array.getJSONObject(i);
                String type = obj.optString(TYPE);
                String _date = obj.optString(DATE);
                date = _date;
            }

            JSONArray prices_array = comicJson.getJSONArray(PRICES);
            for(int i = 0; i < prices_array.length(); i++) {
                JSONObject obj = prices_array.getJSONObject(i);
                price = obj.optString(PRICE);
            }


            JSONArray imgs_array = comicJson.getJSONArray(IMAGE);
            for(int i = 0; i < imgs_array.length(); i++) {
                JSONObject obj = imgs_array.getJSONObject(i);
                String img = obj.getString(PATH);
                String extension = obj.getString(EXTENSION);
                image_poster = img;
                image_poster_extension = extension;
            }

            JSONObject img = comicJson.getJSONObject(THUMBNAIL);
            image_cover = img.optString(PATH);
            image_cover_extension = img.getString(EXTENSION);

            JSONObject creators_object = comicJson.getJSONObject(CREATORS);
            JSONArray creators_array = creators_object.getJSONArray(ITEMS);
            for(int i = 0; i < creators_array.length(); i++) {
                // setup array depending on the
                // number of creators
                creators = new HashMap<>(creators_array.length());

                JSONObject obj = creators_array.getJSONObject(i);

                String name = obj.getString(NAME);
                String role = obj.getString(ROLE);
                creators.put(name, role);
                author = name;
            }

            //long comicId = addComic(comic_id, title, image_cover, description, author, price, page_count, date);

            // Insert the new comic information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(comicArray.length());

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            for(int i = 0; i < comicArray.length(); i++) {

                ContentValues comicValues = new ContentValues();

                comicValues.put(ComicContract.ComicEntry.COLUMN_COMIC_ID, comic_id);
                comicValues.put(ComicContract.ComicEntry.COLUMN_TITLE, title);
                comicValues.put(ComicContract.ComicEntry.COLUMN_IMAGE, image_cover);
                comicValues.put(ComicContract.ComicEntry.COLUMN_DESCRIPTION, description);
                comicValues.put(ComicContract.ComicEntry.COLUMN_AUTHOR, author);
                comicValues.put(ComicContract.ComicEntry.COLUMN_PRICE, price);
                comicValues.put(ComicContract.ComicEntry.COLUMN_PAGE_COUNT, page_count);
                comicValues.put(ComicContract.ComicEntry.COLUMN_DATE, date);

                cVVector.add(comicValues);
            }

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(ComicContract.ComicEntry.CONTENT_URI, cvArray);

                // delete old data so we don't build up an endless history
                getContext().getContentResolver().delete(ComicContract.ComicEntry.CONTENT_URI,
                        ComicContract.ComicEntry.COLUMN_DATE + " <= ?",
                        new String[] {Long.toString(dayTime.setJulianDay(julianStartDay-1))});

                //notifyComic();
            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /*private void notifyComic() {
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if ( displayNotifications ) {

            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            long lastSync = prefs.getLong(lastNotificationKey, 0);

            if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
                // Last sync was more than 1 day ago, let's send a notification with the comic details.

                Uri comicUri = ComicContract.ComicEntry.buildComicUri(1);

                // we'll query our contentProvider, as always
                Cursor cursor = context.getContentResolver().query(comicUri, NOTIFY_COMIC_PROJECTION, null, null, null);

                if (cursor.moveToFirst()) {
                    int weatherId = cursor.getInt(INDEX_COMIC_ID);
                    String title = cursor.getString(INDEX_TITLE);
                    String image = cursor.getString(INDEX_IMAGE);
                    String desc = cursor.getString(INDEX_DESCRIPTION);
                    String author = cursor.getString(INDEX_AUTHOR);
                    String price = cursor.getString(INDEX_PRICE);
                    String page_count = cursor.getString(INDEX_PAGE_COUNT);
                    String date = cursor.getString(INDEX_DATE);

                    //TODO: Add notification icon
                    int iconId;
                    Resources resources = context.getResources();
                    String app_name = context.getString(R.string.app_name);

                    // Define the text of the Comic.
                    String contentText = String.format(context.getString(R.string.notification));

                    // NotificationCompatBuilder is a very convenient way to build backward-compatible
                    // notifications.  Just throw in some data.
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getContext())
                                    .setColor(resources.getColor(R.color.colorPrimary))
                                    .setContentTitle(app_name)
                                    .setContentText(contentText);

                    // Make something interesting happen when the user clicks on the notification.
                    // In this case, opening the app is sufficient.
                    Intent resultIntent = new Intent(context, BrowseComicsActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    // COMIC_NOTIFICATION_ID allows you to update the notification later on.
                    mNotificationManager.notify(COMIC_NOTIFICATION_ID, mBuilder.build());

                    //refreshing last sync
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(lastNotificationKey, System.currentTimeMillis());
                    editor.commit();
                }
                cursor.close();
            }
        }
    }*/

    /**
     * Helper method to handle insertion of a new comic in the comic database.
     *
     * @param comicID The ID of the comic.
     * @param comicTitle The title string of the comic.
     * @param comicImage image path of comic
     * @param comicDescription the detailed description of the comic
     * @param comicAuthor author(s) of the comic
     * @param comicPrice the price of the comic
     * @param comicPageCount the page count of the comic
     * @param comicDate published date of comic
     * @return The ID of the added comic.
     */
    long addComic(int comicID, String comicTitle, String comicImage, String comicDescription,
                  String comicAuthor, String comicPrice, int comicPageCount, String comicDate) {
        long comicId;

        // First, check if the comic with this title name exists in the db
        Cursor comicCursor = getContext().getContentResolver().query(
                ComicContract.ComicEntry.CONTENT_URI,
                new String[]{ComicContract.ComicEntry._ID},
                ComicContract.ComicEntry.COLUMN_TITLE + " = ?",
                new String[]{comicTitle},
                null);

        if (comicCursor.moveToFirst()) {
            int comicIdIndex = comicCursor.getColumnIndex(ComicContract.ComicEntry._ID);
            comicId = comicCursor.getLong(comicIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues comicValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            comicValues.put(ComicContract.ComicEntry.COLUMN_COMIC_ID, comicID);
            comicValues.put(ComicContract.ComicEntry.COLUMN_TITLE, comicTitle);
            comicValues.put(ComicContract.ComicEntry.COLUMN_IMAGE, comicImage);
            comicValues.put(ComicContract.ComicEntry.COLUMN_DESCRIPTION, comicDescription);
            comicValues.put(ComicContract.ComicEntry.COLUMN_AUTHOR, comicAuthor);
            comicValues.put(ComicContract.ComicEntry.COLUMN_PRICE, comicPrice);
            comicValues.put(ComicContract.ComicEntry.COLUMN_PAGE_COUNT, comicPageCount);
            comicValues.put(ComicContract.ComicEntry.COLUMN_DATE, comicDate);


            // Finally, insert comic data into the database.
            Uri insertedUri = getContext().getContentResolver().insert(
                    ComicContract.ComicEntry.CONTENT_URI,
                    comicValues
            );

            // The resulting URI contains the ID for the row.  Extract the comicId from the Uri.
            comicId = ContentUris.parseId(insertedUri);
        }

        comicCursor.close();

        return comicId;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
