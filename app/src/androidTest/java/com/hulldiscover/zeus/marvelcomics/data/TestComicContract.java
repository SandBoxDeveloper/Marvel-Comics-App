package com.hulldiscover.zeus.marvelcomics.data;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.hulldiscover.zeus.marvelcomics.Data.ComicContract;

public class TestComicContract extends AndroidTestCase {
    // intentionally includes a slash to make sure Uri is getting quoted correctly
    private static final String TEST_COMIC_LOCATION = "/comic";
    private static final long TEST_COMIC = 1;

    public void testBuildComicLocation() {
        Uri locationUri = ComicContract.ComicEntry.buildComic(TEST_COMIC_LOCATION);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildComicUri in " +
                        "ComicContract.",
                locationUri);

        assertEquals("Error: Comic location not properly appended to the end of the Uri",
                TEST_COMIC_LOCATION, locationUri.getLastPathSegment());

        assertEquals("Error: Comic location Uri doesn't match our expected result",
                locationUri.toString(),
                "content://com.hulldiscover.zeus.marvelcomics/comic/%2Fcomic");

        //Adding weather with location to the uribuilder
    }

}
