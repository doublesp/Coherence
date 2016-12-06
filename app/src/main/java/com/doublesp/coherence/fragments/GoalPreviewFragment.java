package com.doublesp.coherence.fragments;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentGoalPreviewBinding;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.GoalDetailActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.viewmodels.Goal;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import javax.inject.Inject;

import rx.Observer;

public class GoalPreviewFragment extends DialogFragment {

    static final String IDEA_PREVIEW_FRAGMENT_INDEX = "IDEA_PREVIEW_FRAGMENT_INDEX";

    FragmentGoalPreviewBinding binding;
    int mPos;
    @Inject
    GoalDetailActionHandlerInterface mActionHandler;
    @Inject
    GoalInteractorInterface mGoalInteractor;
    @Inject
    IdeaInteractorInterface mIdeaInteractor;

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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Goal goal = mGoalInteractor.getGoalAtPos(mPos);
        binding.setPos(mPos);
        binding.setViewModel(goal);
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

}
