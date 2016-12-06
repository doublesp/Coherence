package com.doublesp.coherence.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = RecipeDatabase.NAME, version = RecipeDatabase.VERSION)
public class RecipeDatabase {
    public static final String NAME = "RecipeDatabase";
    public static final int VERSION = 1;
}
