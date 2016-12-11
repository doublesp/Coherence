package com.doublesp.coherence.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.doublesp.coherence.R;
import com.doublesp.coherence.adapters.IdeaReviewArrayAdapter;
import com.doublesp.coherence.databinding.FragmentIdeaReviewBinding;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.doublesp.coherence.fragments.ListCompositionFragment.LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL;

public class IdeaReviewFragment extends DialogFragment {

    static final String IDEA_REVIEW_FRAGMENT_VIEW_MODEL = "IDEA_REVIEW_FRAGMENT_VIEW_MODEL";
    static final int IDEA_REVIEW_FRAGMENT_BACKGROUND_IMAGE_ROTATION_INTERVAL = 3000;

    FragmentIdeaReviewBinding binding;
    Plan mPlan;
    List<String> mBackgroundImageUrls;
    int mBackgroundImageIndex;

    public IdeaReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment IdeaReviewFragment.
     */
    public static IdeaReviewFragment newInstance(Plan plan) {
        IdeaReviewFragment fragment = new IdeaReviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(IDEA_REVIEW_FRAGMENT_VIEW_MODEL, Parcels.wrap(plan));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlan = Parcels.unwrap(getArguments().getParcelable(IDEA_REVIEW_FRAGMENT_VIEW_MODEL));
            mBackgroundImageUrls = new ArrayList<>();
            for (Idea idea : mPlan.getIdeas()) {
                mBackgroundImageUrls.add(idea.getMeta().getImageUrl());
            }
            mBackgroundImageIndex = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_idea_review,
                container, false);
        binding.rvIdeaSelector.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIdeaSelector.setAdapter(new IdeaReviewArrayAdapter(mPlan));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rotateImage();
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
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void rotateImage() {
        Glide.with(binding.ivIdeaBackgroundImage.getContext())
                .load(mBackgroundImageUrls.get(mBackgroundImageIndex))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(R.anim.animation_idea_review)
                .into(binding.ivIdeaBackgroundImage);
        // TODO: [Monetization] insert image ads in image rotation
        if (mBackgroundImageUrls.size() < 2) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBackgroundImageIndex = (mBackgroundImageIndex + 1) % mBackgroundImageUrls.size();
                rotateImage();
            }
        }, LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL);
    }

}
