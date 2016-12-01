package com.doublesp.coherence.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentSavedGoalsBinding;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;

import javax.inject.Inject;
import javax.inject.Named;

public class SavedGoalsFragment extends DialogFragment {

    FragmentSavedGoalsBinding binding;
    @Inject
    @Named("SavedGoal")
    RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    @Inject
    GoalInteractorInterface mInteractor;

    public SavedGoalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SavedGoalsFragment.
     */
    public static SavedGoalsFragment newInstance() {
        SavedGoalsFragment fragment = new SavedGoalsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_goals, container,
                false);
        binding.rvSavedGoals.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSavedGoals.setAdapter(mAdapter);
        binding.rvSavedGoals.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mInteractor.loadBookmarkedGoals();
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InjectorInterface) {
            InjectorInterface injector = (InjectorInterface) context;
            injector.inject(this);
            mInteractor.loadBookmarkedGoals();
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
        mInteractor.loadBookmarkedGoals();
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
