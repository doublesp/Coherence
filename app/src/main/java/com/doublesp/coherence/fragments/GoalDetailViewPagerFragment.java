package com.doublesp.coherence.fragments;

import com.doublesp.coherence.R;
import com.doublesp.coherence.adapters.GoalPreviewFragmentPagerAdapter;
import com.doublesp.coherence.databinding.FragmentGoalDetailViewPagerBinding;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

public class GoalDetailViewPagerFragment extends Fragment {

    static final String GOAL_DETAIL_VIEW_PAGER_INDEX = "GOAL_DETAIL_VIEW_PAGER_INDEX";
    FragmentGoalDetailViewPagerBinding binding;
    GoalPreviewFragmentPagerAdapter mPagerAdapter;
    int startIndex;

    @Inject
    GoalInteractorInterface mGoalInteractor;

    public GoalDetailViewPagerFragment() {
        // Required empty public constructor
    }

    public static GoalDetailViewPagerFragment newInstance(int pos) {
        GoalDetailViewPagerFragment fragment = new GoalDetailViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt(GOAL_DETAIL_VIEW_PAGER_INDEX, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            startIndex = getArguments().getInt(GOAL_DETAIL_VIEW_PAGER_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_goal_detail_view_pager, container, false);
        mPagerAdapter = new GoalPreviewFragmentPagerAdapter(
                getChildFragmentManager(), mGoalInteractor);
        binding.viewpager.setAdapter(mPagerAdapter);
        binding.viewpager.setCurrentItem(startIndex, true);
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
