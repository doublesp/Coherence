package com.doublesp.coherence.viewholders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.ItemGoalBinding;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.viewmodels.Goal;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by pinyaoting on 11/26/16.
 */

public class GoalViewHolder extends RecyclerView.ViewHolder {

    ItemGoalBinding binding;

    public GoalViewHolder(View itemView) {
        super(itemView);
        binding = ItemGoalBinding.bind(itemView);
    }

    public void setPosition(int position) {
        binding.setPos(position);
    }

    public void setViewModel(Goal viewModel) {
        binding.setViewModel(viewModel);

        // TODO: do palette with data bind
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                // insert the bitmap into the image view
                binding.ivGoalImage.setImageBitmap(bitmap);

                // Use generate() method from the Palette API to get the vibrant color from the bitmap
                // Set the result as the background color
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        // Get the "vibrant" color swatch based on the bitmap
                        Palette.Swatch vibrant = palette.getVibrantSwatch();
                        if (vibrant != null) {
                            int colorFrom = binding.tvGoalIndex.getTextColors().getDefaultColor();
                            int colorTo = vibrant.getRgb();
                            if (colorFrom == colorTo) {
                                return;
                            }
                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                            colorAnimation.setDuration(500);
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    int color = (int) animator.getAnimatedValue();

                                    // Update the title TextView with the proper text color
                                    binding.tvGoalTitle.setTextColor(color);
                                    binding.tvGoalIndex.setTextColor(color);

                                    // Set the background color of a layout based on the vibrant color
                                    binding.tvGoalTitle.setBackgroundTintList(ColorStateList.valueOf(color));
                                    binding.tvGoalIndex.setBackgroundTintList(ColorStateList.valueOf(color));
                                }
                            });
                            colorAnimation.start();
                        }
                    }
                });
            }
        };

        Glide.with(binding.ivGoalImage.getContext())
                .load(viewModel.getImageUrl())
                .asBitmap()
                .animate(R.anim.abc_fade_in)
                .fitCenter()
                .placeholder(R.drawable.background_3)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(target);
    }

    public void setHandler(GoalActionHandlerInterface handler) {
        binding.setHandler(handler);
    }

    public void executePendingBindings() {
        binding.executePendingBindings();
    }
}
