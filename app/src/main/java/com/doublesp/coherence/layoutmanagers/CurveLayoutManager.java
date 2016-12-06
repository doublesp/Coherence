package com.doublesp.coherence.layoutmanagers;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class CurveLayoutManager extends LinearLayoutManager {

    public Point mContentOffset;
    public int mChildWidth;
    public int mChildHeight;
    public boolean mChildMeasured;

    public CurveLayoutManager(Context context, int orientation) {
        super(context, orientation, false);
        mContentOffset = new Point(0, 0);
        mChildWidth = 0;
        mChildHeight = 0;
        mChildMeasured = false;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
            RecyclerView.State state) {
        if (getOrientation() == HORIZONTAL) {
            return 0;
        }
        mContentOffset.y += dy;
        int delta = super.scrollVerticallyBy(dy, recycler, state);
        fill(recycler);
        return delta;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler,
            RecyclerView.State state) {
        if (getOrientation() == VERTICAL) {
            return 0;
        }
        mContentOffset.x += dx;
        int delta = super.scrollHorizontallyBy(dx, recycler, state);
        fill(recycler);
        return delta;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        fill(recycler);
    }

    private void fill(RecyclerView.Recycler recycler) {
        // 1. detach
        detachAndScrapAttachedViews(recycler);

        int firstVisibleItemPosition = calculateFirstVisibleItemPosition();
        int sizeOfRecyclerView = 0;
        int accumulatedOffset = 0;

        switch (getOrientation()) {
            case HORIZONTAL:
                sizeOfRecyclerView = getWidth();
                accumulatedOffset =
                        (mChildMeasured && mChildWidth != 0) ? -mContentOffset.x % mChildWidth : 0;
                break;
            case VERTICAL:
                sizeOfRecyclerView = getHeight();
                accumulatedOffset =
                        (mChildMeasured && mChildHeight != 0) ? -mContentOffset.y % mChildHeight
                                : 0;
                break;
        }

        // 2. layout
        for (int i = firstVisibleItemPosition; ((i < getItemCount()) && (i >= 0)); i++) {
            View view = recycler.getViewForPosition(i);

            measureChildWithMargins(view, 0, 0);
            calculateAndSaveChildSize(view);
            addView(view);

            int xOffset;
            int yOffset;
            int radius = sizeOfRecyclerView / 2;
            switch (getOrientation()) {
                case HORIZONTAL:
                    xOffset = Math.abs((getDecoratedLeft(view) + mChildWidth / 2) - radius);
                    yOffset = radius - (int) Math.round(
                            Math.sqrt(radius * radius - xOffset * xOffset));
                    layoutDecorated(view, accumulatedOffset, yOffset,
                            accumulatedOffset + mChildWidth, yOffset + mChildHeight);
                    accumulatedOffset += mChildWidth;
                    break;
                case VERTICAL:
                    yOffset = Math.abs((getDecoratedTop(view) + mChildHeight / 2) - radius);
                    xOffset = (int) Math.round(Math.sqrt(radius * radius - yOffset * yOffset))
                            - radius;
                    layoutDecorated(view, xOffset, accumulatedOffset, xOffset + mChildWidth,
                            accumulatedOffset + mChildHeight);
                    accumulatedOffset += mChildHeight;
                    break;
            }
            if (accumulatedOffset > sizeOfRecyclerView) {
                break;
            }
        }

        // 3. recycle
        List<RecyclerView.ViewHolder> scrapList = recycler.getScrapList();
        for (int i = 0; i < scrapList.size(); i++) {
            View viewRemoved = scrapList.get(i).itemView;
            recycler.recycleView(viewRemoved);
        }
    }

    public void calculateAndSaveChildSize(View view) {
        if (mChildMeasured) return;

        mChildWidth = getDecoratedMeasuredWidth(view);
        mChildHeight = getDecoratedMeasuredHeight(view);
        mChildMeasured = true;
    }

    public int calculateFirstVisibleItemPosition() {
        if (!mChildMeasured || mChildWidth == 0 || mChildHeight == 0) {
            return 0;
        }
        switch (getOrientation()) {
            case HORIZONTAL:
                return mContentOffset.x / mChildWidth;
            case VERTICAL:
                return mContentOffset.y / mChildHeight;
        }
        return 0;
    }
}
