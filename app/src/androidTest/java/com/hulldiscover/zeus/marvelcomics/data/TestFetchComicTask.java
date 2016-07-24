package com.hulldiscover.zeus.marvelcomics.data;

/**
 * Created by Zeus on 24/07/16.
 */
/*
public class TestFetchComicTask extends AndroidTestCase {
    static final String ADD_COMIC_TITLE = "Batman vs Superman - Dawn of Justice";
    static final String ADD_LOCATION_CITY = "Sunnydale";
    static final double ADD_LOCATION_LAT = 34.425833;
    static final double ADD_LOCATION_LON = -119.714167;

    */
/*
        Students: uncomment testAddLocation after you have written the AddLocation function.
        This test will only run on API level 11 and higher because of a requirement in the
        content provider.
     *//*

    @TargetApi(11)
    public void testAddLocation() {
        // start from a clean state
        getContext().getContentResolver().delete(ComicContract.ComicEntry.CONTENT_URI,
                ComicContract.ComicEntry.COLUMN_TITLE + " = ?",
                new String[]{ADD_COMIC_TITLE});

        BrowseComicsActivityFragment.FetchComicsTask fwt = new BrowseComicsActivityFragment.FetchComicsTask(getContext(), null);
        long locationId = fwt.addLocation(ADD_COMIC_TITLE, ADD_LOCATION_CITY,
                ADD_LOCATION_LAT, ADD_LOCATION_LON);

        // does addLocation return a valid record ID?
        assertFalse("Error: addLocation returned an invalid ID on insert",
                locationId == -1);

        // test all this twice
        for ( int i = 0; i < 2; i++ ) {

            // does the ID point to our location?
            Cursor comicCursor = getContext().getContentResolver().query(
                    ComicContract.ComicEntry.CONTENT_URI,
                    new String[]{
                            ComicContract.ComicEntry._ID,
                            ComicContract.ComicEntry.COLUMN_TITLE,
                            ComicContract.ComicEntry.COLUMN_PAGE_COUNT,
                            ComicContract.ComicEntry.COLUMN_DESCRIPTION,
                    },
                    ComicContract.ComicEntry.COLUMN_TITLE + " = ?",
                    new String[]{ADD_COMIC_TITLE},
                    null);

            // these match the indices of the projection
            if (comicCursor.moveToFirst()) {
                assertEquals("Error: the queried value of locationId does not match the returned value" +
                        "from addLocation", comicCursor.getLong(0), locationId);
                assertEquals("Error: the queried value of location setting is incorrect",
                        comicCursor.getString(1), ADD_COMIC_TITLE);
                assertEquals("Error: the queried value of location city is incorrect",
                        comicCursor.getString(2), ADD_LOCATION_CITY);
                assertEquals("Error: the queried value of latitude is incorrect",
                        comicCursor.getDouble(3), ADD_LOCATION_LAT);
                assertEquals("Error: the queried value of longitude is incorrect",
                        comicCursor.getDouble(4), ADD_LOCATION_LON);
            } else {
                fail("Error: the id you used to query returned an empty cursor");
            }

            // there should be no more records
            assertFalse("Error: there should be only one record returned from a location query",
                    comicCursor.moveToNext());

            // add the location again
            long newLocationId = fwt.addLocation(ADD_COMIC_TITLE, ADD_LOCATION_CITY,
                    ADD_LOCATION_LAT, ADD_LOCATION_LON);

            assertEquals("Error: inserting a location again should return the same ID",
                    locationId, newLocationId);
        }
        // reset our state back to normal
        getContext().getContentResolver().delete(ComicContract.ComicEntry.CONTENT_URI,
                ComicContract.ComicEntry.COLUMN_TITLE + " = ?",
                new String[]{ADD_COMIC_TITLE});

        // clean up the test so that other tests can use the content provider
        getContext().getContentResolver().
                acquireContentProviderClient(ComicContract.ComicEntry.CONTENT_URI).
                getLocalContentProvider().shutdown();
    }
}
*/
