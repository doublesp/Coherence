package com.doublesp.coherence.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by pinyaoting on 11/10/16.
 */

@Database(name = MovieDatabase.NAME, version = MovieDatabase.VERSION)
public class MovieDatabase {
    public static final String NAME = "MovieDatabase";
    public static final int VERSION = 1;
}
