package com.doublesp.coherence.fragments;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.doublesp.coherence.R;
import com.doublesp.coherence.adapters.IdeasArrayAdapter;
import com.doublesp.coherence.databinding.FragmentGoalPreviewBinding;
import com.doublesp.coherence.interfaces.presentation.GoalDetailActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.viewmodels.Goal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import rx.Observer;

public class GoalPreviewFragment extends Fragment {

    static final String IDEA_PREVIEW_FRAGMENT_INDEX = "IDEA_PREVIEW_FRAGMENT_INDEX";

    FragmentGoalPreviewBinding binding;
    int mPos;
    @Inject
    GoalDetailActionHandlerInterface mActionHandler;
    @Inject
    GoalInteractorInterface mGoalInteractor;
    @Inject
    IdeasArrayAdapter mIdeasArrayAdapter;

    public GoalPreviewFragment() {
        // Required empty public constructor
    }

    public static GoalPreviewFragment newInstance(int pos) {
        GoalPreviewFragment fragment = new GoalPreviewFragment();
        Bundle args = new Bundle();
        args.putInt(IDEA_PREVIEW_FRAGMENT_INDEX, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPos = getArguments().getInt(IDEA_PREVIEW_FRAGMENT_INDEX);
            mGoalInteractor.loadDetailsForGoalAtPos(mPos);
            mGoalInteractor.subscribeToGoalStateChange(new Observer<ViewState>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ViewState viewState) {
                    switch (viewState.getState()) {
                        case R.id.state_loaded:
                            switch (viewState.getOperation()) {
                                case UPDATE:
                                    if (viewState.getStart() != -1) {
                                        return;
                                    }
                                    Goal updatedGoal = mGoalInteractor.getGoalAtPos(mPos);
                                    mIdeasArrayAdapter.notifyDataSetChanged();
                                    binding.setViewModel(updatedGoal);
                                    binding.executePendingBindings();
                            }
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goal_preview, container,
                false);
        binding.setHandler(mActionHandler);
        binding.rvGoalPreviewIdeas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvGoalPreviewIdeas.setAdapter(mIdeasArrayAdapter);
        binding.nsvGoalPreviewIdeasContainer.setNestedScrollingEnabled(false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Goal goal = mGoalInteractor.getGoalAtPos(mPos);
        binding.setPos(mPos);
        binding.setViewModel(goal);
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap,
                                        GlideAnimation<? super Bitmap> glideAnimation) {
                // insert the bitmap into the image view
                binding.ivIdeaBackgroundImage.setImageBitmap(bitmap);

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
                            ColorStateList colorStateList = ColorStateList.valueOf(color);
                            binding.tvGoalIndex.setBackgroundTintList(colorStateList);
                            binding.tvTitle.setTextColor(color);
                        }
                    }
                });
            }
        };

        if (goal.getImageUrl() != null) {
            Glide.with(binding.ivIdeaBackgroundImage.getContext())
                    .load(goal.getImageUrl())
                    .asBitmap()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(target);
        }

        binding.executePendingBindings();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InjectorInterface) {
            InjectorInterface injector = (InjectorInterface) context;
            injector.inject(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
