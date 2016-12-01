package com.doublesp.coherence.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentGoalPreviewBinding;
import com.doublesp.coherence.interfaces.presentation.GoalDetailActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.viewmodels.Goal;

import org.parceler.Parcels;

import javax.inject.Inject;

public class GoalPreviewFragment extends DialogFragment {

    static final String IDEA_PREVIEW_FRAGMENT_VIEW_MODEL = "IDEA_PREVIEW_FRAGMENT_VIEW_MODEL";

    FragmentGoalPreviewBinding binding;
    Goal mGoal;
    @Inject
    GoalDetailActionHandlerInterface mActionHandler;

    public GoalPreviewFragment() {
        // Required empty public constructor
    }

    public static GoalPreviewFragment newInstance(Goal goal) {
        GoalPreviewFragment fragment = new GoalPreviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(IDEA_PREVIEW_FRAGMENT_VIEW_MODEL, Parcels.wrap(goal));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGoal = Parcels.unwrap(getArguments().getParcelable(IDEA_PREVIEW_FRAGMENT_VIEW_MODEL));
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
        binding.setViewModel(mGoal);
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
