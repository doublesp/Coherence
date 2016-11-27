package com.doublesp.coherence.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ConstantsAndUtils {
    public static final String USER_LISTS = "userLists";
    public static final String SHOPPING_LISTS = "shoppingLists";
    public static final String USERS = "users";
    public static final String ANONYMOUS = "anonymous";
    public static final String TIMESTAMP = "timestamp";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String LIST_ID = "listId";
    public static final String USER_FRIENDS = "userFriends";

    public static String getOwner(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                context);
        return sharedPreferences.getString(EMAIL, ANONYMOUS);
    }
}
