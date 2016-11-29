package com.doublesp.coherence.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.doublesp.coherence.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    public static final String SHARED_WITH = "sharedWith";

    public static String getOwner(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                context);
        return sharedPreferences.getString(EMAIL, ANONYMOUS);
    }

    public static String getDefaultTitle(Context context) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm:ss");
        StringBuilder titleBuilder = new StringBuilder(
                context.getString(R.string.default_idea_prefix));
        titleBuilder.append(" ");
        titleBuilder.append(formatter.format(calendar.getTime()));

        return titleBuilder.toString();
    }
}
