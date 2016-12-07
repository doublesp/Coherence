package com.doublesp.coherence.viewholders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.doublesp.coherence.databinding.SimpleItemIdeaBinding;
import com.doublesp.coherence.utils.ImageUtils;
import com.doublesp.coherence.viewmodels.Idea;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by pinyaoting on 12/6/16.
 */

public class SimpleIdeaViewHolder extends RecyclerView.ViewHolder {

    SimpleItemIdeaBinding binding;

    public SimpleIdeaViewHolder(View itemView) {
        super(itemView);
        binding = SimpleItemIdeaBinding.bind(itemView);
    }

    public void setPosition(int position) {
        binding.setPos(position);
        int color = ImageUtils.getColorForPosition(position);
        binding.flSimpleItemIdea.setBackgroundColor(color);
    }

    public void setViewModel(Idea viewModel) {
        binding.setViewModel(viewModel);
        // TODO: do palette with data bind
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap,
                                        GlideAnimation<? super Bitmap> glideAnimation) {
                // insert the bitmap into the image view
                binding.ivIdea.setImageBitmap(bitmap);

                // Use generate() method from the Palette API to get the vibrant color from the
                // bitmap
                // Set the result as the background color
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        // Get the "vibrant" color swatch based on the bitmap
                        Palette.Swatch vibrant = palette.getVibrantSwatch();
                        if (vibrant != null) {
                            int color = vibrant.getRgb();
                            int colorWithAlpha = (color & 0x00FFFFFF) | 0x40000000;
                            binding.flSimpleItemIdea.setBackgroundColor(colorWithAlpha);
                        }
                    }
                });
            }
        };

        if (viewModel == null ||
                viewModel.getMeta() == null ||
                viewModel.getMeta().getImageUrl() == null) {
            return;
        }

        Glide.with(binding.ivIdea.getContext())
                .load(viewModel.getMeta().getImageUrl())
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(target);
    }

    public void executePendingBindings() {
        binding.executePendingBindings();
    }
}