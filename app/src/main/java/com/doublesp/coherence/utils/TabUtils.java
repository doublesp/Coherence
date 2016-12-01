package com.doublesp.coherence.utils;

import com.doublesp.coherence.R;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

/**
 * Created by pinyaoting on 11/16/16.
 */

public class TabUtils {

    public static void bindIcons(final Context context, ViewPager viewPager,
            final TabLayout layout) {
        layout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(context, R.color.colorButton);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(context, R.color.colorButtonUnselected);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

        layout.getTabAt(0).setIcon(R.drawable.ic_shopping_cart);
        layout.getTabAt(1).setIcon(R.drawable.ic_library_books);
    }

}
