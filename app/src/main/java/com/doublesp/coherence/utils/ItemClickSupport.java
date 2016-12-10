package com.doublesp.coherence.utils;

import com.doublesp.coherence.R;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemClickSupport {
    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemSwipeTouchListener mOnItemSwipeTouchListener;

    private RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(final View view) {
            view.setOnTouchListener(new OnSwipeAndTouchListener(view.getContext()) {
                @Override
                public boolean onSingleTap() {
                    if (mOnItemClickListener != null) {
                        RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
                        mOnItemClickListener.onItemClicked(
                                mRecyclerView, holder.getAdapterPosition(), view);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onLongTap() {
                    if (mOnItemLongClickListener != null) {
                        RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
                        return mOnItemLongClickListener.onItemLongClicked(
                                mRecyclerView, holder.getAdapterPosition(), view);

                    }
                    return false;
                }

                @Override
                public void onSwipeRight() {
                    if (mOnItemSwipeTouchListener != null) {
                        RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
                        mOnItemSwipeTouchListener.onSwipeRight(
                                mRecyclerView, holder.getAdapterPosition(), view);
                    }
                }

                @Override
                public void onSwipeLeft() {
                    if (mOnItemSwipeTouchListener != null) {
                        RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
                        mOnItemSwipeTouchListener.onSwipeLeft(
                                mRecyclerView, holder.getAdapterPosition(), view);
                    }
                }

                @Override
                public void onSwipeUp() {
                    if (mOnItemSwipeTouchListener != null) {
                        RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
                        mOnItemSwipeTouchListener.onSwipeUp(
                                mRecyclerView, holder.getAdapterPosition(), view);
                    }
                }

                @Override
                public void onSwipeDown() {
                    if (mOnItemSwipeTouchListener != null) {
                        RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
                        mOnItemSwipeTouchListener.onSwipeRight(
                                mRecyclerView, holder.getAdapterPosition(), view);
                    }
                }
            });
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
    };

    private ItemClickSupport(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(R.id.item_click_support, this);
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    public static ItemClickSupport addTo(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support == null) {
            support = new ItemClickSupport(view);
        }
        return support;
    }

    public static ItemClickSupport removeFrom(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support != null) {
            support.detach(view);
        }
        return support;
    }

    public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    public ItemClickSupport setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
        return this;
    }

    public ItemClickSupport setOnItemSwipeTouchListener(OnItemSwipeTouchListener listener) {
        mOnItemSwipeTouchListener = listener;
        return this;
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(mAttachListener);
        view.setTag(R.id.item_click_support, null);
    }

    public interface OnItemClickListener {

        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }

    public interface OnItemLongClickListener {

        boolean onItemLongClicked(RecyclerView recyclerView, int position, View v);
    }

    public interface OnItemSwipeTouchListener {

        void onSwipeRight(RecyclerView recyclerView, int position, View v);

        void onSwipeLeft(RecyclerView recyclerView, int position, View v);

        void onSwipeUp(RecyclerView recyclerView, int position, View v);

        void onSwipeDown(RecyclerView recyclerView, int position, View v);
    }
}
