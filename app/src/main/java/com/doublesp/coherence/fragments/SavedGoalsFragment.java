package com.doublesp.coherence.fragments;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentSavedGoalsBinding;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

public class SavedGoalsFragment extends Fragment {

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_goals, container, false);
        binding.rvSavedGoals.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
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
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
