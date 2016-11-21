package com.doublesp.coherence.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentMultipleIdeaPreviewBinding;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.HomeInjectorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaPreviewActionHandlerInterface;
import com.doublesp.coherence.layoutmanagers.CurveLayoutManager;
import com.doublesp.coherence.viewmodels.Idea;

import javax.inject.Inject;
import javax.inject.Named;

public class MultipleIdeaPreviewFragment extends DialogFragment {

    FragmentMultipleIdeaPreviewBinding binding;
    SnapHelper mSnapHelper;
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    IdeaInteractorInterface mIdeaInteractor;
    @Inject
    @Named("Preview")
    RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    @Inject
    IdeaPreviewActionHandlerInterface mActionHandler;

    public MultipleIdeaPreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MultipleIdeaPreviewFragment.
     */
    public static MultipleIdeaPreviewFragment newInstance() {
        MultipleIdeaPreviewFragment fragment = new MultipleIdeaPreviewFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiple_idea_preview,
                container, false);
        int orientation = getOrientation() == Configuration.ORIENTATION_LANDSCAPE
                ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL;
        mLayoutManager = new CurveLayoutManager(getActivity(), orientation);
        mSnapHelper = new LinearSnapHelper();
        binding.rvIdeaSelector.setLayoutManager(mLayoutManager);
        mSnapHelper.attachToRecyclerView(binding.rvIdeaSelector);
        binding.rvIdeaSelector.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View targetView = mSnapHelper.findSnapView(mLayoutManager);
                int position = mLayoutManager.getPosition(targetView);
                selectIdeaAtPos(position);
            }
        });
        binding.rvIdeaSelector.setAdapter(mAdapter);
        binding.setHandler(mActionHandler);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectIdeaAtPos(0);
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

    private void selectIdeaAtPos(int pos) {
        Idea idea = mIdeaInteractor.getIdeaAtPos(pos);
        binding.setViewModel(idea);
    }

    private int getOrientation() {
        return getActivity().getResources().getConfiguration().orientation;
    }
}
