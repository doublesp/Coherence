package com.doublesp.coherence.utils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.doublesp.coherence.R;

import android.os.Handler;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by pinyaoting on 11/29/16.
 */

public class ImageUtils {

    public static void rotateImage(final Handler handler, final ImageView imageView,
            final List<String> imageUrls, final int imageIndex, final int interveral) {
        Glide.with(imageView.getContext())
                .load(imageUrls.get(imageIndex))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(R.anim.animation_idea_review)
                .into(imageView);

        // TODO: [Monetization] insert image ads in image rotation
        if (imageUrls.size() < 2) {
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int newImageIndex = (imageIndex + 1) % imageUrls.size();
                rotateImage(handler, imageView, imageUrls, newImageIndex, interveral);
            }
        }, interveral);
    }

    public static String composeImageUri(String baseUri, String imageUri) {
        return new StringBuilder(baseUri).append(imageUri).toString();
    }
}
