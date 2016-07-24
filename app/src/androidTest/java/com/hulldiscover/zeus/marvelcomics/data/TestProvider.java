package com.hulldiscover.zeus.marvelcomics.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.hulldiscover.zeus.marvelcomics.Data.ComicContract;
import com.hulldiscover.zeus.marvelcomics.Data.ComicDbHelper;
import com.hulldiscover.zeus.marvelcomics.Data.ComicProvider;

/*
    Note: This is not a complete set of tests of the Sunshine ContentProvider, but it does test
    that at least the basic functionality has been implemented correctly.
    Students: Uncomment the tests in this class as you implement the functionality in your
    ContentProvider to make sure that you've implemented things reasonably correctly.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
       This helper function deletes all records from both database tables using the ContentProvider.
       It also queries the ContentProvider to make sure that the database has been successfully
       deleted, so it cannot be used until the Query and Delete functions have been written
       in the ContentProvider.
       Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
       the delete functionality in the ContentProvider.
     */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                ComicContract.ComicEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                ComicContract.ComicEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Comic table during delete", 0, cursor.getCount());
        cursor.close();
    }

    /*
        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    /*
        This test checks to make sure that the content provider is registered correctly.
        Students: Uncomment this test to make sure you've correctly registered the WeatherProvider.
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                ComicProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + ComicContract.CONTENT_AUTHORITY,
                    providerInfo.authority, ComicContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
            This test doesn't touch the database.  It verifies that the ContentProvider returns
            the correct type for each type of URI that it can handle.
            Students: Uncomment this test to verify that your implementation of GetType is
            functioning correctly.
         */
    public void testGetType() {
        // content://com.example.android.sunshine.app/weather/
        String type = mContext.getContentResolver().getType(ComicContract.ComicEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the WeatherEntry CONTENT_URI should return WeatherEntry.CONTENT_TYPE",
                ComicContract.ComicEntry.CONTENT_TYPE, type);

        /*String testLocation = "94074";
        // content://com.example.android.sunshine.app/weather/94074
        type = mContext.getContentResolver().getType(
                ComicContract.ComicEntry.buildWeatherLocation(testLocation));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the WeatherEntry CONTENT_URI with location should return WeatherEntry.CONTENT_TYPE",
                ComicContract.ComicEntry.CONTENT_TYPE, type);

        long testDate = 1419120000L; // December 21st, 2014
        // content://com.example.android.sunshine.app/weather/94074/20140612
        type = mContext.getContentResolver().getType(
                ComicContract.ComicEntry.buildWeatherLocationWithDate(testLocation, testDate));
        // vnd.android.cursor.item/com.example.android.sunshine.app/weather/1419120000
        assertEquals("Error: the WeatherEntry CONTENT_URI with location and date should return WeatherEntry.CONTENT_ITEM_TYPE",
                ComicContract.ComicEntry.CONTENT_ITEM_TYPE, type);*/
    }


    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic weather query functionality
        given in the ContentProvider is working correctly.
     */
    public void testBasicComicQuery() {
        // insert our test records into the database
        ComicDbHelper dbHelper = new ComicDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createNewComicValues();
        long locationRowId = TestUtilities.insertNewComicValues(mContext);

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues weatherValues = TestUtilities.createComicValues(locationRowId);

        long weatherRowId = db.insert(ComicContract.ComicEntry.TABLE_NAME, null, testValues);
        assertTrue("Unable to Insert WeatherEntry into the Database", weatherRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor weatherCursor = mContext.getContentResolver().query(
                ComicContract.ComicEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicComicQuery", weatherCursor, weatherValues);
    }

    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if your location queries are
        performing correctly.
     */
   /* public void testBasicLocationQueries() {
        // insert our test records into the database
        ComicDbHelper dbHelper = new ComicDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
        long locationRowId = TestUtilities.insertNorthPoleLocationValues(mContext);

        // Test the basic content provider query
        Cursor locationCursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicLocationQueries, location query", locationCursor, testValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertEquals("Error: Location Query did not properly set NotificationUri",
                    locationCursor.getNotificationUri(), LocationEntry.CONTENT_URI);
        }
    }*/

    /*
        This test uses the provider to insert and then update the data. Uncomment this test to
        see if your update comic table is functioning correctly.
     */
    public void testUpdateComicTable() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createNewComicValues();

        Uri locationUri = mContext.getContentResolver().
                insert(ComicContract.ComicEntry.CONTENT_URI, values);
        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(ComicContract.ComicEntry._ID, locationRowId);
        updatedValues.put(ComicContract.ComicEntry.COLUMN_TITLE, "Flashpoint Paradox");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor locationCursor = mContext.getContentResolver().query(ComicContract.ComicEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        locationCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                ComicContract.ComicEntry.CONTENT_URI, updatedValues, ComicContract.ComicEntry._ID + "= ?",
                new String[] { Long.toString(locationRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        locationCursor.unregisterContentObserver(tco);
        locationCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                ComicContract.ComicEntry.CONTENT_URI,
                null,   // projection
                ComicContract.ComicEntry._ID + " = " + locationRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateComicTable.  Error validating location entry update.",
                cursor, updatedValues);

        cursor.close();
    }


    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the insert functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createNewComicValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(ComicContract.ComicEntry.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(ComicContract.ComicEntry.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                ComicContract.ComicEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.",
                cursor, testValues);

        /*// Fantastic.  Now that we have a location, add some weather!
        //ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        //mContext.getContentResolver().registerContentObserver(WeatherEntry.CONTENT_URI, true, tco);

        Uri weatherInsertUri = mContext.getContentResolver()
        //        .insert(WeatherEntry.CONTENT_URI, weatherValues);
        assertTrue(weatherInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating WeatherEntry insert.",
                weatherCursor, weatherValues);

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        weatherValues.putAll(testValues);

        // Get the joined Weather and Location data
        weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocation(TestUtilities.TEST_LOCATION),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location Data.",
                weatherCursor, weatherValues);

        // Get the joined Weather and Location data with a start date
        weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithStartDate(
                        TestUtilities.TEST_LOCATION, TestUtilities.TEST_DATE),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location Data with start date.",
                weatherCursor, weatherValues);

        // Get the joined Weather data for a specific date
        weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithDate(TestUtilities.TEST_LOCATION, TestUtilities.TEST_DATE),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location data for a specific date.",
                weatherCursor, weatherValues);*/
    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our weather delete.
        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(ComicContract.ComicEntry.CONTENT_URI, true, weatherObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        weatherObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(weatherObserver);
    }


    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertComicValues(long locationRowId) {
        String currentTestDate = TestUtilities.TEST_DATE;
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues weatherValues = new ContentValues();
            weatherValues.put(ComicContract.ComicEntry.COLUMN_COMIC_ID, locationRowId);
            weatherValues.put(ComicContract.ComicEntry.COLUMN_DATE, currentTestDate);
            weatherValues.put(ComicContract.ComicEntry.COLUMN_PRICE, 1.1);
            weatherValues.put(ComicContract.ComicEntry.COLUMN_DESCRIPTION, "Small description");
            weatherValues.put(ComicContract.ComicEntry.COLUMN_AUTHOR, "Andre");
            returnContentValues[i] = weatherValues;
        }
        return returnContentValues;
    }

    // Student: Uncomment this test after you have completed writing the BulkInsert functionality
    // in your provider.  Note that this test will work with the built-in (default) provider
    // implementation, which just inserts records one-at-a-time, so really do implement the
    // BulkInsert ContentProvider function.
    public void testBulkInsert() {
        // first, let's create a location value
        ContentValues testValues = TestUtilities.createNewComicValues();
        Uri comicUri = mContext.getContentResolver().insert(ComicContract.ComicEntry.CONTENT_URI, testValues);
        long comicRowId = ContentUris.parseId(comicUri);

        // Verify we got a row back.
        assertTrue(comicRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                ComicContract.ComicEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testBulkInsert. Error validating ComicEntry.",
                cursor, testValues);

        // Now we can bulkInsert some weather.  In fact, we only implement BulkInsert for weather
        // entries.  With ContentProviders, you really only have to implement the features you
        // use, after all.
        ContentValues[] bulkInsertContentValues = createBulkInsertComicValues(comicRowId);

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver comicObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(ComicContract.ComicEntry.CONTENT_URI, true, comicObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(ComicContract.ComicEntry.CONTENT_URI, bulkInsertContentValues);

        // If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        comicObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(comicObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                ComicContract.ComicEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                ComicContract.ComicEntry.COLUMN_DATE + " ASC"  // sort order == by DATE ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating WeatherEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}