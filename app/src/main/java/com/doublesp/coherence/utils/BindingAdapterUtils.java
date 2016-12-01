package com.doublesp.coherence.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Paint;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.doublesp.coherence.R;

/**
 * Created by pinyaoting on 10/20/16.
 */

public class BindingAdapterUtils {

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .fitCenter()
                .placeholder(R.drawable.background_3)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    @BindingAdapter({"bind:roundedImageUrl"})
    public static void loadRoundedImage(ImageView view, String url) {
        Context context = view.getContext();
        Glide.with(context)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform(context))
                .into(view);
    }

    @BindingAdapter({"bind:text"})
    public static void setText(EditText view, CharSequence text) {
        if (text == null) {
            text = "";
        }
        view.setText(text);
        view.setSelection(text.length());
    }

    @BindingAdapter({"bind:selected"})
    public static void setSelected(ImageButton view, boolean selected) {
        view.setSelected(selected);
    }

    @BindingAdapter({"bind:crossout"})
    public static void setCrossout(TextView view, boolean crossout) {
        Context context = view.getContext();
        if (crossout) {
            view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            view.setPaintFlags(view.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
