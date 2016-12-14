package com.doublesp.coherence.utils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.doublesp.coherence.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageUtils {

    public static long FADEIN_DURATION = 250L;
    public static long FADEOUT_DURATION = 250L;
    public static long PAUSE_DURATION = 3000L;
    public static long ENTIRE_DURATION = FADEIN_DURATION + FADEOUT_DURATION + PAUSE_DURATION;
    public static float FINAL_SCALE = 1.2f;
    public static int FINAL_XOFFSET = -100;
    public static int FINAL_YOFFSET = -50;
    private static List<Integer> mBackgroundImageId;

    public static void loadImageWithProminentColor(
            final ImageView imageView, final ViewGroup container, String imageUrl) {
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap,
                                        GlideAnimation<? super Bitmap> glideAnimation) {
                // insert the bitmap into the image view
                imageView.setImageBitmap(bitmap);

                // Use generate() method from the Palette API to get the vibrant color from the
                // bitmap
                // Set the result as the background color
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        // Get the "vibrant" color swatch based on the bitmap
                        Palette.Swatch vibrant = palette.getVibrantSwatch();
                        if (vibrant != null) {
                            int colorFrom = ((ColorDrawable) container.getBackground()).getColor();
                            int colorTo = vibrant.getRgb();

                            ValueAnimator colorAnimation = ValueAnimator.ofObject(
                                    new ArgbEvaluator(), colorFrom, colorTo);
                            colorAnimation.setDuration(500);
                            colorAnimation.addUpdateListener(
                                    new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animator) {
                                            int color = (int) animator.getAnimatedValue();
                                            container.setBackgroundColor(color);
                                        }
                                    });
                            colorAnimation.start();
                        }
                    }
                });
            }
        };

        Glide.with(imageView.getContext())
                .load(imageUrl)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(target);
    }

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

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(imageView, "scaleX", FINAL_SCALE);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(imageView, "scaleY", FINAL_SCALE);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(
                imageView, "translationX", FINAL_XOFFSET);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(
                imageView, "translationY", FINAL_YOFFSET);
        scaleUpX.setDuration(ENTIRE_DURATION);
        scaleUpY.setDuration(ENTIRE_DURATION);
        translateX.setDuration(ENTIRE_DURATION);
        translateY.setDuration(ENTIRE_DURATION);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(
                imageView, "alpha", 0f, 1f);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(
                imageView, "alpha", 1f, 0f);
        fadeIn.setDuration(FADEIN_DURATION);
        fadeOut.setDuration(FADEOUT_DURATION);

        final AnimatorSet fadeSet = new AnimatorSet();
        fadeSet.play(fadeOut).after(PAUSE_DURATION).after(fadeIn);
        final AnimatorSet scaleSet = new AnimatorSet();
        scaleSet.play(scaleUpX).with(scaleUpY).with(translateX).with(translateY);

        fadeSet.addListener(new AnimatorListenerAdapter() {
            int mIndex = index;

            @Override
            public void onAnimationEnd(Animator animation) {
                mIndex = (mIndex + 1) % getBackgroundImageId().size();
                imageView.setImageResource(
                        getBackgroundImageId().get(mIndex));
                super.onAnimationEnd(animation);
                fadeSet.start();
                scaleSet.start();
            }
        });
        fadeSet.start();
        scaleSet.start();
    }

    public static int topImagePadding(WindowManager wm, Resources r) {
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int statusBarHeightPixels = Math.round(
                r.getDimension(R.dimen.status_bar_height) * metrics.density);
        int viewportHeightPixels = metrics.heightPixels - statusBarHeightPixels;
        float offset = r.getDimension(R.dimen.big_top_image_height);
        float cutThroughOffset = r.getFraction(
                R.fraction.top_image_cut_ratio, Math.round(offset), 1);
        int extraPadding = r.getInteger(R.integer.bigtop_image_extra_padding);
        return Math.round(offset - cutThroughOffset + extraPadding);
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
        }
        return mBackgroundImageId;
    }

    public static int getIlluminatedColor(int color) {
        return (color & 0x00FFFFFF) | 0xB3000000;
    }

    public static int getTransparentColor(int color) {
        return (color & 0x00FFFFFF) | 0x80000000;
    }

    public static int getColorForPosition(int position) {
        int colorResource = R.color.colorUpOverlay;
        switch (position % 4) {
            case 0:
                colorResource = R.color.colorUpOverlay;
                break;
            case 1:
                colorResource = R.color.colorLeftOverlay;
                break;
            case 2:
                colorResource = R.color.colorRightOverlay;
                break;
            case 3:
                colorResource = R.color.colorDownOverlay;
                break;
            default:
                break;
        }
        return colorResource;
    }

}
