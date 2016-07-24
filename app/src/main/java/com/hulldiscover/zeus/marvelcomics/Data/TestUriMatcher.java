package com.hulldiscover.zeus.marvelcomics.Data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;


public class TestUriMatcher extends AndroidTestCase {

    // content://com.hulldiscover.zeus.marvelcomics/comic
    private static final Uri TEST_COMIC_DIR = ComicContract.ComicEntry.CONTENT_URI;

    /*
        This function tests that the UriMatcher returns the correct integer value
        for each of the Uri types that the ContentProvider can handle.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = ComicProvider.buildUriMatcher();

        assertEquals("Error: The COMIC URI was matched incorrectly.",
                testMatcher.match(TEST_COMIC_DIR ), ComicProvider.COMIC);
    }
}
