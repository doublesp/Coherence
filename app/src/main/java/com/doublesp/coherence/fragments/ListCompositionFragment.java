package com.doublesp.coherence.fragments;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentListCompositionBinding;
import com.doublesp.coherence.interfaces.presentation.HomeInjectorInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import javax.inject.Inject;
import javax.inject.Named;

public class ListCompositionFragment extends Fragment {

    static final int LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL = 5000;
    FragmentListCompositionBinding binding;
    int[] mBackgroundImageIds;
    int mBackgroundImageIndex;
    @Inject
    @Named("Composition")
    RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    @Inject
    ListFragmentActionHandlerInterface mActionHandler;

    public ListCompositionFragment() {
        // Required empty public constructor
    }

    public static ListCompositionFragment newInstance() {
        ListCompositionFragment fragment = new ListCompositionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setupBackgroundImageId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_composition, container, false);
        binding.rvIdeas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIdeas.setAdapter(mAdapter);
        binding.setHandler(mActionHandler);
        rotateImage();
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeInjectorInterface) {
            HomeInjectorInterface injector = (HomeInjectorInterface) context;
            injector.inject(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void setupBackgroundImageId() {
        mBackgroundImageIds = new int[5];
        mBackgroundImageIds[0] = R.drawable.background_0;
        mBackgroundImageIds[1] = R.drawable.background_1;
        mBackgroundImageIds[2] = R.drawable.background_2;
        mBackgroundImageIds[3] = R.drawable.background_3;
        mBackgroundImageIds[4] = R.drawable.background_4;
        mBackgroundImageIndex = 0;
    }

    void rotateImage() {
        binding.ivIdeaCompositionBackground.setImageResource(mBackgroundImageIds[mBackgroundImageIndex]);
        binding.ivIdeaCompositionBackground.startAnimation(fadeInOutAnimation());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBackgroundImageIndex = (mBackgroundImageIndex + 1) % mBackgroundImageIds.length;
                rotateImage();
            }
        }, LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL);
    }

    Animation fadeInOutAnimation() {
        int duration = LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL / 2;
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(duration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(duration);
        fadeOut.setDuration(duration);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        return animation;
    }

}
