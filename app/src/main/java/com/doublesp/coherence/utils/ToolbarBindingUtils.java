package com.doublesp.coherence.utils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by pinyaoting on 10/25/16.
 */

public class ToolbarBindingUtils {

    public static void bind(AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}
