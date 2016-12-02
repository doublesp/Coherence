package com.doublesp.coherence.utils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.doublesp.coherence.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pinyaoting on 11/29/16.
 */

public class ImageUtils {

    public static long FADEIN_DURATION = 250L;
    public static long FADEOUT_DURATION = 250L;
    public static long PAUSE_DURATION = 3000L;
    private static List<Integer> mBackgroundImageId;

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

    public static void loadDefaultImageRotation(final ImageView imageView) {
        final int index = 0;
        imageView.setImageResource(
                getBackgroundImageId().get(index));

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(
                imageView, "alpha", 0f, 1f);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(
                imageView, "alpha", 1f, 0f);
        fadeIn.setDuration(FADEIN_DURATION);
        fadeOut.setDuration(FADEOUT_DURATION);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeOut).after(PAUSE_DURATION).after(fadeIn);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            int mIndex = index;
            @Override
            public void onAnimationEnd(Animator animation) {
                mIndex = (mIndex + 1) % getBackgroundImageId().size();
                imageView.setImageResource(
                        getBackgroundImageId().get(mIndex));
                super.onAnimationEnd(animation);
                animatorSet.start();
            }
        });
        animatorSet.start();
    }

    public static String composeImageUri(String baseUri, String imageUri) {
        return new StringBuilder(baseUri).append(imageUri).toString();
    }

    public static List<Integer> getBackgroundImageId() {
        if (mBackgroundImageId == null) {
            mBackgroundImageId = new ArrayList<>();
            mBackgroundImageId.add(R.drawable.background_0);
            mBackgroundImageId.add(R.drawable.background_1);
            mBackgroundImageId.add(R.drawable.background_2);
            mBackgroundImageId.add(R.drawable.background_3);
            mBackgroundImageId.add(R.drawable.background_4);
        }
        return mBackgroundImageId;
    }
}
