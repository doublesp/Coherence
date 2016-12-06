package com.doublesp.coherence.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.doublesp.coherence.R;

public class TabUtils {

    public static void bindIcons(final Context context, ViewPager viewPager,
            final TabLayout layout) {
        layout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(context, R.color.colorText);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(
                                context, R.color.colorButtonUnselected);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

        setupTabs(context, layout);
    }

    private static void setupTabs(Context context, final TabLayout layout) {
        int selectedTabIconColor = ContextCompat.getColor(context, R.color.colorText);
        int tabIconColor = ContextCompat.getColor(context, R.color.colorButtonUnselected);
        TabLayout.Tab tab;

        tab = layout.getTabAt(0);
        tab.setIcon(R.drawable.ic_library_books);
        tab.getIcon().setColorFilter(selectedTabIconColor, PorterDuff.Mode.SRC_IN);

        tab = layout.getTabAt(1);
        tab.setIcon(R.drawable.ic_bookmark);
        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        tab = layout.getTabAt(2);
        tab.setIcon(R.drawable.ic_shopping_cart);
        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }
}
