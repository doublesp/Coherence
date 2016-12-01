package com.doublesp.coherence.fragments;

import static com.doublesp.coherence.fragments.ListCompositionFragment
        .LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentGoalSearchBinding;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.utils.AnimationUtils;
import com.doublesp.coherence.viewmodels.Plan;

import org.parceler.Parcels;

import javax.inject.Inject;
import javax.inject.Named;

public class GoalSearchFragment extends DialogFragment {

    static final String GOAL_SEARCH_FRAGMENT_VIEW_MODEL = "GOAL_SEARCH_FRAGMENT_VIEW_MODEL";
    FragmentGoalSearchBinding binding;
    int[] mBackgroundImageIds;
    int mBackgroundImageIndex;
    @Inject
    @Named("GoalAction")
    GoalActionHandlerInterface mActionHandler;
    @Inject
    @Named("Goal")
    RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    @Inject
    GoalInteractorInterface mGoalInteractor;

    public GoalSearchFragment() {
        // Required empty public constructor
    }

    public static GoalSearchFragment newInstance() {
        return GoalSearchFragment.newInstance(null);
    }

    public static GoalSearchFragment newInstance(Plan plan) {
        GoalSearchFragment fragment = new GoalSearchFragment();
        Bundle args = new Bundle();
        if (plan != null) {
            args.putParcelable(GOAL_SEARCH_FRAGMENT_VIEW_MODEL, Parcels.wrap(plan));
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Plan plan = Parcels.unwrap(
                    getArguments().getParcelable(GOAL_SEARCH_FRAGMENT_VIEW_MODEL));
            if (plan != null) {
                mGoalInteractor.searchGoalByIdeas(plan.getIdeas());
            } else {
                mGoalInteractor.search(null);
            }
            setupBackgroundImageId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_goal_search, container, false);
        binding.rvIdeaSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIdeaSearchResults.setAdapter(mAdapter);
        binding.etIdeaSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mActionHandler.afterTextChanged(editable);
            }
        });
        rotateImage();
        return binding.getRoot();
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
        if (getDialog() != null) {
            updateLayoutParams();
        }
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoalInteractor.clearGoal();
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
        binding.ivIdeaSearchBackground.setImageResource(
                mBackgroundImageIds[mBackgroundImageIndex]);
        binding.ivIdeaSearchBackground.startAnimation(AnimationUtils.fadeInOutAnimation(
                LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBackgroundImageIndex = (mBackgroundImageIndex + 1) % mBackgroundImageIds.length;
                rotateImage();
            }
        }, LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL);
    }

    private void updateLayoutParams() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
